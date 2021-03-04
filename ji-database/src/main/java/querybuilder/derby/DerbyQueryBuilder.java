package querybuilder.derby;

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

public class DerbyQueryBuilder extends QueryBuilder {

	public DerbyQueryBuilder(Connection connection) {
		super(connection);
	}
	
	@Override
	protected SelectQueryBuilder query(String query) {
		return new DerbySelectBuilder(query, connection);
	}

	@Override
	public DeleteQueryBuilder delete(String table) {
		return new DerbyDeleteBuilder(connection, table);
	}

	@Override
	public InsertQueryBuilder insert(String table) {
		return new DerbyInsertBuilder(connection, table);
	}

	@Override
	public UpdateQueryBuilder update(String table) {
		return new DerbyUpdateBuilder(connection, table);
	}

	@Override
	public SelectQueryBuilder select(String select) {
		return new DerbySelectBuilder(connection, select);
	}

	@Override
	public ExecuteQueryBuilder deleteTable(String table) {
		return new DerbyExecuteBuilder(connection, String.format("DROP TABLE %s", table));
	}

	@Override
	public CreateTableQueryBuilder createTable(String name) {
		return new DerbyCreateTableBuilder(connection, name);
	}

	@Override
	public AlterTableQueryBuilder alterTable(String name) {
		return new DerbyAlterTableBuilder(connection, name);
	}

	@Override
	public ExecuteQueryBuilder deleteView(String table) {
		return new DerbyExecuteBuilder(connection, String.format("DROP VIEW %s", table));
	}

	@Override
	public CreateViewQueryBuilder createView(String name) {
		return new DerbyCreateViewBuilder(connection, name, false);
	}

	@Override
	public CreateViewQueryBuilder alterView(String name) {
		return new DerbyCreateViewBuilder(connection, name, true);
	}

	@Override
	public ExecuteQueryBuilder createIndex(String name, String table, String... colums) {
		return new DerbyExecuteBuilder(
			connection,
			String.format("CREATE INDEX %s ON %s(%s)", name, table, Implode.implode(", ", colums))
		);
	}

	@Override
	public ExecuteQueryBuilder deleteIndex(String name, String table) {
		return new DerbyExecuteBuilder(
				connection,
				String.format("DROP INDEX %s", name)
			);
	}

	@Override
	public Functions getSqlFunctions() {
		return new DerbyFunctions();
	}

}
