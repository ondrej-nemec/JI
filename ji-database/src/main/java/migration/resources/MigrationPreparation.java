package migration.resources;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import migration.MigrationException;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.QueryBuilder;

public class MigrationPreparation {
	
	private final String migrationTable;
	
	private final String separator;
	
	public MigrationPreparation(String migrationTable, String idSeparator) {
		this.migrationTable = migrationTable;
		this.separator = idSeparator;
	}

	public List<String> getFilesToMigrate(List<String> loadedFiles, boolean isRevert, QueryBuilder builder) throws SQLException, MigrationException {
		// validace
		List<String> migrated = selectMigrations(builder);
		if (migrated.size() > loadedFiles.size()) {
			throw new MigrationException();
		}
		
		int indexOfLastMigrated = indexOfLastMigrated(loadedFiles, migrated);
		// no migration in db - no to revert, all for migrate
		if (indexOfLastMigrated == -1) {
			return (isRevert ? new LinkedList<>() : loadedFiles);
		}
		// all migrated - no for migrate, all for revert
		if (indexOfLastMigrated + 1 == loadedFiles.size()) {
			return (isRevert ? loadedFiles : new LinkedList<>());
		}
		if (isRevert) {
			return loadedFiles.subList(0, indexOfLastMigrated + 1);
		} else {
			return loadedFiles.subList(indexOfLastMigrated + 1, loadedFiles.size());
		}
	}
	
	private int indexOfLastMigrated(List<String> loadedFiles, List<String> migrated) throws MigrationException {
		int index = -1;
		for (int i = 0; i < migrated.size(); i++) {
			String fileId = new IdSeparator(loadedFiles.get(i), separator).getId();
			if (!migrated.get(i).equals(fileId)) {
				throw new MigrationException();
			}
			index++;
		}
		return index;
	}
	
	private List<String> selectMigrations(QueryBuilder builder) throws SQLException {
		try {
			return builder
				.select("id")
				.from(migrationTable)
				.fetchAll((row)->{
					return row.getValue("id").toString();
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

}
