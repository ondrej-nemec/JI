package querybuilder.sqlserver;

import java.sql.Connection;

import common.functions.Implode;
import query.QueryBuilderFactory;
import query.wrappers.AlterTableBuilder;
import query.wrappers.CreateTableBuilder;
import query.wrappers.CreateViewBuilder;
import query.wrappers.DeleteBuilder;
import query.wrappers.ExecuteBuilder;
import query.wrappers.InsertBuilder;
import query.wrappers.SelectBuilder;
import query.wrappers.UpdateBuilder;
import querybuilder.Functions;

public class SqlServerQueryBuilder implements QueryBuilderFactory {

	private final Connection connection;
	
	public SqlServerQueryBuilder(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Functions getSqlFunctions() {
		return new SqlServerFunctions();
	}

	@Override
	public DeleteBuilder delete(String table) {
		return new SqlServerDeleteBuilder(connection, table);
	}

	@Override
	public InsertBuilder insert(String table) {
		return new SqlServerInsertBuilder(connection, table);
	}

	@Override
	public UpdateBuilder update(String table) {
		return new SqlServerUpdateBuilder(connection, table);
	}

	@Override
	public SelectBuilder select(String select) {
		return new SqlServerSelectBuilder(connection, select);
	}

	@Override
	public ExecuteBuilder deleteTable(String table) {
		return new ExecuteBuilder(connection, String.format("DROP TABLE %s", table));
	}

	@Override
	public CreateTableBuilder createTable(String name) {
		return new SqlServerCreateTableBuilder(connection, name);
	}

	@Override
	public AlterTableBuilder alterTable(String name) {
		return new SqlServerAlterTableBuilder(connection, name);
	}

	@Override
	public ExecuteBuilder deleteView(String table) {
		return new ExecuteBuilder(connection, String.format("DROP VIEW %s", table));
	}

	@Override
	public CreateViewBuilder createView(String name) {
		return new SqlServerCreateViewBuilder(connection, name, false);
	}

	@Override
	public CreateViewBuilder alterView(String name) {
		return new SqlServerCreateViewBuilder(connection, name, true);
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
		return new ExecuteBuilder(connection, String.format("ALTER TABLE %s DROP INDEX %s", table, name));
	}

}
