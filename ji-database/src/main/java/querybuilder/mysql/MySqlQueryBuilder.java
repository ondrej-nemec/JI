package querybuilder.mysql;

import java.sql.Connection;

import common.functions.Implode;
import querybuilder.AlterTableQueryBuilder;
import querybuilder.CreateTableQueryBuilder;
import querybuilder.CreateViewQueryBuilder;
import querybuilder.DeleteQueryBuilder;
import querybuilder.ExecuteQueryBuilder;
import querybuilder.Functions;
import querybuilder.InsertQueryBuilder;
import querybuilder.QueryBuilder;
import querybuilder.SelectQueryBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlQueryBuilder extends QueryBuilder {

	public MySqlQueryBuilder(Connection connection) {
		super(connection);
	}
	
	@Override
	protected SelectQueryBuilder query(String query) {
		return new MySqlSelectBuilder(query, connection);
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
		return new MySqlExecuteBuilder(connection, String.format("DROP TABLE %s", table));
	}

	@Override
	public CreateTableQueryBuilder createTable(String name) {
		return new MySqlCreateTableBuilder(connection, name);
	}

	@Override
	public AlterTableQueryBuilder alterTable(String name) {
		return new MySqlAlterTableBuilder(connection, name);
	}

	@Override
	public ExecuteQueryBuilder deleteView(String table) {
		return new MySqlExecuteBuilder(connection, String.format("DROP VIEW %s", table));
	}

	@Override
	public CreateViewQueryBuilder createView(String name) {
		return new MySqlCreateViewBuilder(connection, name, false);
	}

	@Override
	public CreateViewQueryBuilder alterView(String name) {
		return new MySqlCreateViewBuilder(connection, name, true);
	}

	@Override
	public ExecuteQueryBuilder createIndex(String name, String table, String... colums) {
		return new MySqlExecuteBuilder(
			connection,
			String.format("CREATE INDEX %s ON %s(%s)", name, table, Implode.implode(", ", colums))
		);
	}

	@Override
	public ExecuteQueryBuilder deleteIndex(String name, String table) {
		return new MySqlExecuteBuilder(
				connection,
				String.format("ALTER TABLE %s DROP INDEX %s", table, name)
			);
	}

	@Override
	public Functions getSqlFunctions() {
		return new MySqlFunctions();
	}

}
