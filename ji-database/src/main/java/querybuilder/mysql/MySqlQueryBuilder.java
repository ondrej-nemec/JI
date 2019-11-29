package querybuilder.mysql;

import java.sql.Connection;

import querybuilder.AlterTableQueryBuilder;
import querybuilder.CreateTableQueryTable;
import querybuilder.CreateViewQueryTable;
import querybuilder.DeleteQueryBuilder;
import querybuilder.ExecuteQueryBuilder;
import querybuilder.InsertQueryBuilder;
import querybuilder.QueryBuilder;
import querybuilder.SelectQueryBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlQueryBuilder extends QueryBuilder {

	public MySqlQueryBuilder(Connection connection) {
		super(connection);
	}

	@Override
	public DeleteQueryBuilder delete(String table) {
		return new MySqlDeleteBuilder(connection, table);
	}

	@Override
	public InsertQueryBuilder insert(String table) {
		return new MySqlInsertBuilder(connection, table);
	}

	@Override
	public UpdateQueryBuilder update(String table) {
		return new MySqlUpdateBuilder(connection, table);
	}

	@Override
	public SelectQueryBuilder select(String select) {
		return new MySqlSelectBuilder(connection, select);
	}

	@Override
	public ExecuteQueryBuilder deleteTable(String table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateTableQueryTable createTable(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlterTableQueryBuilder alterTable(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecuteQueryBuilder deleteView(String table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateViewQueryTable createView(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateViewQueryTable alterView(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecuteQueryBuilder createIndex(String name, String table, String... colums) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExecuteQueryBuilder deleteIndex(String name, String table) {
		// TODO Auto-generated method stub
		return null;
	}

}
