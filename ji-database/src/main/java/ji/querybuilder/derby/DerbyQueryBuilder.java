package ji.querybuilder.derby;

import java.sql.Connection;

import ji.common.functions.Implode;
import ji.querybuilder.QueryBuilderFactory;
import ji.querybuilder.builders.AlterTableBuilder;
import ji.querybuilder.builders.CreateTableBuilder;
import ji.querybuilder.builders.CreateViewBuilder;
import ji.querybuilder.builders.DeleteBuilder;
import ji.querybuilder.builders.ExecuteBuilder;
import ji.querybuilder.builders.Functions;
import ji.querybuilder.builders.InsertBuilder;
import ji.querybuilder.builders.SelectBuilder;
import ji.querybuilder.builders.UpdateBuilder;

public class DerbyQueryBuilder implements QueryBuilderFactory {

	private final Connection connection;
	
	public DerbyQueryBuilder(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Functions getSqlFunctions() {
		return new DerbyFunctions();
	}
	@Override
	public DeleteBuilder delete(String table) {
		return new DerbyDeleteBuilder(connection, table);
	}
	@Override
	public InsertBuilder insert(String table) {
		return new DerbyInsertBuilder(connection, table);
	}
	@Override
	public UpdateBuilder update(String table) {
		return new DerbyUpdateBuilder(connection, table);
	}
	@Override
	public SelectBuilder select(String select) {
		return new DerbySelectBuilder(connection, select);
	}
	@Override
	public ExecuteBuilder deleteTable(String table) {
		return new ExecuteBuilder(connection, String.format("DROP TABLE %s", table));
	}
	@Override
	public CreateTableBuilder createTable(String name) {
		return new DerbyCreateTableBuilder(connection, name);
	}
	@Override
	public AlterTableBuilder alterTable(String name) {
		return new DerbyAlterTableBuilder(connection, name);
	}
	@Override
	public ExecuteBuilder deleteView(String table) {
		return new ExecuteBuilder(connection, String.format("DROP VIEW %s", table));
	}
	@Override
	public CreateViewBuilder createView(String name) {
		return new DerbyCreateViewBuilder(connection, name, false);
	}
	@Override
	public CreateViewBuilder alterView(String name) {
		return new DerbyCreateViewBuilder(connection, name, true);
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

	@Override
	public Connection getConnection() {
		return connection;
	}

}
