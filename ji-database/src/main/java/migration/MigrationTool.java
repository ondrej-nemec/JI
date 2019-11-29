package migration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import common.DateTime;
import common.FileExtension;
import common.Logger;
import common.exceptions.NotImplementedYet;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.QueryBuilder;
import querybuilder.SelectQueryBuilder;
import text.BufferedReaderFactory;

public class MigrationTool {
	
	private final static String NAME_SEPARATOR = "__";
	
	private boolean external = false;
	
	private final Logger logger;
	
	private final QueryBuilder builder;
	
	private final String folder;
	
	private final String migrationTable;
	
	/*
	 * 1 - nahrat jmena souboru do mapy - klic je jmeno, hodnota zvoleny "migrator" -> sql/class
	 *   - !! java soubory nepridavat -> ? prvni kompilace
	 *   - !! sesortovat podle id
	 *   - otestovat, zda muze byt pouzito jako id
	 *   - viz loadResources
	 * 2 - kontrola existence záznamù migrace (tabulka), pøípadnì její vytvoøení
	 *   - nejspíš select nad ní, a vrácení listu záznamu, pokud se bude vytváøet, tak prázdný seznam
	 * 3 - projiti seznamu souboru, pokud jmeno neni v seznamu zaznamu, zkontrolovat, ze neni dalsi zaznam a provest
	 *   - zapsat soubor do zaznamu migrace
	 *   - pro provadeni migrace a zapsani do zaznamu migraci pouzit transakci
	 * 
	 *  v sql souborech bude moznost dat oddelovac - pro foward a revert cast
	 */
	
	public MigrationTool(QueryBuilder builder, String folder, Logger logger) {
		this(builder, folder, "migrations", logger);
	}
	
	public MigrationTool(QueryBuilder builder, String folder, String migrationTable, Logger logger) {
		this.logger = logger;
		this.builder = builder;
		this.folder = folder;
		this.migrationTable = migrationTable;
	}
	
	public void migrate() throws Exception {
		File file = getMigrationDir(folder);
		Map<String, String> map = loadFiles(file.listFiles());
		doMigrations(file, map, folder, false);
	}
	
	public void revert() {
		
	}
	
	public void revert(String id) {
		
	}
	
	public void revert(int steps) {
		
	}
	
	/******** FOUND MIGRATION DIR *********/
	
	protected File getMigrationDir(String folder) throws IOException {
		try {
			File file = getResourceFolderFiles(folder);
			if (file.listFiles() != null) {
				external = false;
				return file;
			}
		} catch (Exception e) { /* ignored */ }
		
		try {
			File file = getFolderFiles(folder);
			if (file.listFiles() != null) {
				external = true;
				return file;
			}
		} catch (Exception e) { /* ignored */ }
		
		throw new IOException("No folder founded: " + folder);
	}
	
	private File getResourceFolderFiles(String folder) {
	    ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    URL url = loader.getResource(folder);
	    String path = url.getPath();
	    return new File(path);
	}
	
	private File getFolderFiles(String folder) {
		return new File(folder);
	}
	
	/******** LOAD FILES **********/
	
	protected Map<String, String> loadFiles(File[] files) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		Map<String, String> loadedFiles = new TreeMap<>();
		for (File file : files) {
			FileExtension fe = new FileExtension(file.getName());
			loadedFiles.put(fe.getName(), fe.getExtension());
			
			switch (fe.getExtension()) {
				case "sql": break;
				case "class": break;
				case "java":
					compiler.run(null, null, null, file.getPath()); // streamy, kam se zapisuje
					break;
				default: break;
			}
		}
		return loadedFiles;
	}
	
	/******** MIGRATION *********/

	private void doMigrations(File dir, Map<String, String> files, String path, boolean revert) throws Exception {
		throw new NotImplementedYet();
		/*
		// prevork
		List<String> ids = migratedIds();
		URL[] urls = new URL[]{dir.toURI().toURL()};
		// each migration
		try (URLClassLoader loader = new URLClassLoader(urls);) {
			for (Object key : files.keySet()) {
				doTransaction(
					builder.getConnection(),
					key, ids, files, revert, path, loader
				);
			}
		}
		*/
	}
	
	/* only for foward and only once */
	protected List<String> migratedIds() throws SQLException {
		try {
			return builder
				.select("id")
				.from(migrationTable)
				.fetchAll((row)->{
					return row.getValue("id");
				});
		} catch (Exception ignored) {
			builder
				.createTable(migrationTable)
				.addColumn("id", ColumnType.string(100), ColumnSetting.NOT_NULL, ColumnSetting.PRIMARY_KEY)
				.addColumn("Description", ColumnType.string(100), ColumnSetting.NOT_NULL)
				.addColumn("DateTime", ColumnType.string(100), ColumnSetting.NOT_NULL)
				.execute();
		}
		return new LinkedList<>();
	}
	
	/* can be used for forward and revert */
	protected void doTransaction(
			Connection con, Object key, List<String> migrationsInDb, Map<String, String> files,
			boolean revert, String path, ClassLoader loader) throws Exception {
		try {
			con.setAutoCommit(false);
			transaction(key, migrationsInDb, files, revert, path, loader);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		}
	}
	
	/*********************/
	
	private void transaction(
			Object key, List<String> ids, Map<String, String> files,
			boolean revert, String path, ClassLoader loader
		) throws Exception {
		if (ids.contains(key)) {
			return;
		}
		String type = files.get(key.toString()).toString();
		String name = key.toString();
		switch (type) {
			case "sql": 
				migrate(name, builder, revert);
				break;
			case "class":
			case "java":
				migrate(name, loader, external ? "" : path + ".", revert);
				break;
			default: break;
		}
		String[] names = parseName(name);
		builder
			.insert(migrationTable)
			.addValue("id", names[0])
			.addValue("Description", names[1])
			.addValue("DateTime", DateTime.format("YYYY-mm-dd H:m:s"))
			.execute();
	}
	
	private String[] parseName(String name) {
		if (!name.contains(NAME_SEPARATOR)) {
			throw new RuntimeException("File name is in incorrect format: " + name);
		}		
		String[] names = name.split(NAME_SEPARATOR);
		if (names.length != 2) {
			throw new RuntimeException("File name is in incorrect format: " + name);
		}
		return names;
	}
	
	/************** migration execution - foward or revert *****************/

	/**
	 * execute java migration depending on @param revert
	 */
	private void migrate(String name, ClassLoader loader, String path, boolean revert) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Migration m = (Migration)loader.loadClass(path + name).newInstance();
		if (revert) {
			m.revert(builder);
		} else {
			m.migrate(builder);
		}
		logger.debug("Migration applied " + path + name);
	}
	
	/**
	 * execute content of sql file
	 */
	private void migrate(String name, QueryBuilder builder, boolean revert) throws SQLException, IOException {
		Statement stat = builder.getConnection().createStatement();
		String[] batches = loadContent(name, revert).split(";");
		for (String batch : batches) {
			stat.addBatch(batch);
		}
		stat.executeBatch();
	}
	
	/**
	 * Load content of sql file depending on @param revert
	 */
	private String loadContent(String file, boolean revert) throws IOException {
		try (BufferedReader br = BufferedReaderFactory.buffer(getClass().getResourceAsStream(file))) {
			StringBuilder sql = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sql.append(line);
				line = br.readLine();
			}
			
			String[] mig = sql.toString().split("--- REVERT ---");
			if (revert && mig.length > 1) {
				return mig[1];
			} else if (revert && mig.length < 2) {
				throw new RuntimeException(String.format("Migration %s has not revert part", file));
			} else if (!revert && mig.length == 1) {
				return sql.toString();
			} else if (!revert && mig.length > 1) {
				return mig[0];
			}
			return sql.toString();
		}
	}
	
}
