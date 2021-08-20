package querybuilder.postgresql;

import java.sql.Connection;

import common.functions.Implode;
import querybuilder.QueryBuilderFactory;
import querybuilder.builders.AlterTableBuilder;
import querybuilder.builders.CreateTableBuilder;
import querybuilder.builders.CreateViewBuilder;
import querybuilder.builders.DeleteBuilder;
import querybuilder.builders.ExecuteBuilder;
import querybuilder.builders.Functions;
import querybuilder.builders.InsertBuilder;
import querybuilder.builders.SelectBuilder;
import querybuilder.builders.UpdateBuilder;

public class PostgreSqlQueryBuilder implements QueryBuilderFactory {

	private final Connection connection;
	
	public PostgreSqlQueryBuilder(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Functions getSqlFunctions() {
		return new PostgeSqlFunctions();
	}

	@Override
	public DeleteBuilder delete(String table) {
		return new PostgreSqlDeleteBuilder(connection, table);
	}

	@Override
	public InsertBuilder insert(String table) {
		return new PostgreSqlInsertBuilder(connection, table);
	}

	@Override
	public UpdateBuilder update(String table) {
		return new PostgreSqlUpdateBuilder(connection, table);
	}

	@Override
	public SelectBuilder select(String select) {
		return new PostgreSqlSelectBuilder(connection, select);
	}

	@Override
	public ExecuteBuilder deleteTable(String table) {
		return new ExecuteBuilder(connection, String.format("DROP TABLE %s", table));
	}

	@Override
	public CreateTableBuilder createTable(String name) {
		return new PostgreSqlCreateTableBuilder(connection, name);
	}

	@Override
	public AlterTableBuilder alterTable(String name) {
		return new PostgreSqlAlterTableBuilder(connection, name);
	}

	@Override
	public ExecuteBuilder deleteView(String table) {
		return new ExecuteBuilder(connection, String.format("DROP VIEW %s", table));
	}

	@Override
	public CreateViewBuilder createView(String name) {
		return new PostgreSqlCreateViewBuilder(connection, name, false);
	}

	@Override
	public CreateViewBuilder alterView(String name) {
		return new PostgreSqlCreateViewBuilder(connection, name, true);
	}

	@Override
	public ExecuteBuilder createIndex(String name, String table, String... colums) {
		return new ExecuteBuilder(
			connection,
			String.format("CREATE INDEX %s ON %s(%s)", name, table, Implode.implode(", ", colums))
		);
	}

	@Override
	public ExecuteBuilder deleteIndex(String name, String table) {
		return new ExecuteBuilder(
			connection,
			String.format("DROP INDEX %s", name)
		);
	}

}
