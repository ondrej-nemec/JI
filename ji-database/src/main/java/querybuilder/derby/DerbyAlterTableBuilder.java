package querybuilder.derby;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import querybuilder.AlterTableQueryBuilder;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.OnAction;

public class DerbyAlterTableBuilder implements AlterTableQueryBuilder {
	
	private final Connection connection;
	
	private final StringBuilder sql;
	
	private final String tableName;
	
	private final List<String> separatedQueries = new LinkedList<>();
	
	private boolean first = true;

	public DerbyAlterTableBuilder(Connection connection, String name) {
		this.connection = connection;
		this.tableName = name;
		this.sql = new StringBuilder();
	}
	
	@Override
	public AlterTableQueryBuilder addColumn(String name, ColumnType type, ColumnSetting... settings) {
		addColumn(name, type, null, settings);
		return this;
	}

	@Override
	public AlterTableQueryBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings) {
		first();
		sql.append("ADD ").append(name).append(" ");
		sql.append(EnumToDerbyString.typeToString(type));
		sql.append(EnumToDerbyString.defaultValueToString(defaultValue));
		StringBuilder append = new StringBuilder();
		for (ColumnSetting setting : settings) {
			sql.append(EnumToDerbyString.settingToString(setting, name, append));
		}
		sql.append(append.toString());
		return this;
	}

	@Override
	public AlterTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		first();
		sql.append(String.format("ADD CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES %s(%s)", column, column, referedTable, referedColumn));
		return this;
	}

	@Override
	public AlterTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate) {
		addForeingKey(column, referedTable, referedColumn);
		sql.append(String.format(
				" ON DELETE %s ON UPDATE %s",
				EnumToDerbyString.onActionToString(onDelete),
				EnumToDerbyString.onActionToString(onUpdate)
		));
		return this;
	}

	@Override
	public AlterTableQueryBuilder deleteColumn(String name) {
		first();
		sql.append(String.format("DROP COLUMN %s", name));
		return this;
	}

	@Override
	public AlterTableQueryBuilder deleteForeingKey(String name) {
		first();
		sql.append(String.format("DROP FOREIGN KEY FK_%s", name));
		return this;
	}

	@Override
	public AlterTableQueryBuilder modifyColumnType(String name, ColumnType type) {
		first();
		sql.append(String.format("ALTER COLUMN %s SET DATA TYPE %s", name, EnumToDerbyString.typeToString(type)));
		// ALTER TABLE bl.USERSPROPERTIES ALTER COLUMN Value SET DATA TYPE CHAR(32000)
		/*
		ALTER TABLE MY_TABLE ADD COLUMN NEW_COLUMN BLOB(2147483647);
		UPDATE MY_TABLE SET NEW_COLUMN=MY_COLUMN;
		ALTER TABLE MY_TABLE DROP COLUMN MY_COLUMN;
		RENAME COLUMN MY_TABLE.NEW_COLUMN TO MY_COLUMN;
		*/
		return this;
	}

	@Override
	public AlterTableQueryBuilder renameColumn(String originName, String newName, ColumnType type) {
		// first();
		//sql.append(String.format("RENAME COLUMN %s %s %s", originName, newName, EnumToDerbyString.typeToString(type)));
		separatedQueries.add(String.format(" RENAME COLUMN %s.%s TO %s", tableName, originName, newName));
		return this;
	}

	@Override
	public void execute() throws SQLException {
		try (Statement stat = connection.createStatement();) {
		    String mainSql = createMainSql();
		    if (!mainSql.isEmpty()) {
		    	stat.addBatch(mainSql);
		    }
		    for (String batch : separatedQueries) {
		        stat.addBatch(batch);
		    }
		    stat.executeBatch();
		}
		/*try (Statement stat = connection.createStatement();) {
			System.out.println(getSql());
			stat.execute(getSql());
		}*/
	}

	@Override
	public String getSql() {
	    StringBuilder b = new StringBuilder();
	    String mainSql = createMainSql();
	    if (!mainSql.isEmpty()) {
	    	b.append(mainSql).append(";");
	    }
	    separatedQueries.forEach((q)->{
	        b.append(q).append(";");
	    });
		return b.toString();
	}
	
	private String createMainSql() {
		if (!sql.toString().isEmpty()) {
			return String.format("ALTER TABLE %s %s" , tableName, sql.toString());
	    }
		return "";
	}
	
	private void first() {
		if (!first) {
			sql.append(", ");
		} else {
			sql.append("");
		}
		first = false;
	}

}
