package querybuilder.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import querybuilder.AlterTableQueryBuilder;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;

public class MySqlAlterTableBuilder implements AlterTableQueryBuilder {

	public MySqlAlterTableBuilder(Connection connection, String name) {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public AlterTableQueryBuilder addColumn(String name, ColumnType type, ColumnSetting... settings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlterTableQueryBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlterTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlterTableQueryBuilder deleteColumn(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlterTableQueryBuilder deleteForeingKey(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlterTableQueryBuilder modifyColumnType(String name, ColumnType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlterTableQueryBuilder renameColumn(String originName, String newName, ColumnType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSql() {
		// TODO Auto-generated method stub
		return null;
	}

}
