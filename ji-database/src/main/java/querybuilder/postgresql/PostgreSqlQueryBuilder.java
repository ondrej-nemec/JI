package querybuilder.postgresql;

import java.sql.Connection;

import common.Implode;
import querybuilder.AlterTableQueryBuilder;
import querybuilder.CreateTableQueryBuilder;
import querybuilder.CreateViewQueryBuilder;
import querybuilder.DeleteQueryBuilder;
import querybuilder.ExecuteQueryBuilder;
import querybuilder.InsertQueryBuilder;
import querybuilder.QueryBuilder;
import querybuilder.SelectQueryBuilder;
import querybuilder.UpdateQueryBuilder;

public class PostgreSqlQueryBuilder extends QueryBuilder {

	public PostgreSqlQueryBuilder(Connection connection) {
		super(connection);
	}

	@Override
	public DeleteQueryBuilder delete(String table) {
		return new PostgreSqlDeleteBuilder(connection, table);
	}

	@Override
	public InsertQueryBuilder insert(String table) {
		return new PostgreSqlInsertBuilder(connection, table);
	}

	@Override
	public UpdateQueryBuilder update(String table) {
		return new PostgreSqlUpdateBuilder(connection, table);
	}

	@Override
	public SelectQueryBuilder select(String select) {
		return new PostgreSqlSelectBuilder(connection, select);
	}

	@Override
	public ExecuteQueryBuilder deleteTable(String table) {
		return new PostgreSqlExecuteBuilder(connection, String.format("DROP TABLE %s", table));
	}

	@Override
	public CreateTableQueryBuilder createTable(String name) {
		return new PostgreSqlCreateTableBuilder(connection, name);
	}

	@Override
	public AlterTableQueryBuilder alterTable(String name) {
		return new PostgreSqlAlterTableBuilder(connection, name);
	}

	@Override
	public ExecuteQueryBuilder deleteView(String table) {
		return new PostgreSqlExecuteBuilder(connection, String.format("DROP VIEW %s", table));
	}

	@Override
	public CreateViewQueryBuilder createView(String name) {
		return new PostgreSqlCreateViewBuilder(connection, name, false);
	}

	@Override
	public CreateViewQueryBuilder alterView(String name) {
		return new PostgreSqlCreateViewBuilder(connection, name, true);
	}

	@Override
	public ExecuteQueryBuilder createIndex(String name, String table, String... colums) {
		return new PostgreSqlExecuteBuilder(
			connection,
			String.format("CREATE INDEX %s ON %s(%s)", name, table, Implode.implode(", ", colums))
		);
	}

	@Override
	public ExecuteQueryBuilder deleteIndex(String name, String table) {
		return new PostgreSqlExecuteBuilder(
				connection,
				String.format("DROP INDEX %s", name)
			);
	}

}
