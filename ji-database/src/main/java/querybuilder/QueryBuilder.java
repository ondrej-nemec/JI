package querybuilder;

import java.sql.Connection;
import java.sql.SQLException;

import querybuilder.builders.AlterTableBuilder;
import querybuilder.builders.BatchBuilder;
import querybuilder.builders.CreateTableBuilder;
import querybuilder.builders.CreateViewBuilder;
import querybuilder.builders.DeleteBuilder;
import querybuilder.builders.ExecuteBuilder;
import querybuilder.builders.Functions;
import querybuilder.builders.InsertBuilder;
import querybuilder.builders.MultipleSelectBuilder;
import querybuilder.builders.SelectBuilder;
import querybuilder.builders.UpdateBuilder;

public class QueryBuilder implements QueryBuilderFactory {

	private final Connection connection;
	private final QueryBuilderFactory factory;
	
	public QueryBuilder(Connection connection, QueryBuilderFactory factory) {
		this.connection = connection;
		this.factory = factory;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public void transaction() throws SQLException {
		connection.setAutoCommit(false);
	}

	public void commit() throws SQLException {
		connection.commit();
	}
	
	public void rollback() throws SQLException {
		connection.rollback();
	}
	
	public BatchBuilder batch() {
		return new BatchBuilder(connection);
	}
	
	public MultipleSelectBuilder multiSelect(SelectBuilder builder) {
		return new MultipleSelectBuilder(connection, builder);
	}

	@Override
	public Functions getSqlFunctions() {
		return factory.getSqlFunctions();
	}

	@Override
	public DeleteBuilder delete(String table) {
		return factory.delete(table);
	}

	@Override
	public InsertBuilder insert(String table) {
		return factory.insert(table);
	}

	@Override
	public UpdateBuilder update(String table) {
		return factory.update(table);
	}

	@Override
	public SelectBuilder select(String select) {
		return factory.select(select);
	}

	@Override
	public ExecuteBuilder deleteTable(String table) {
		return factory.deleteTable(table);
	}

	@Override
	public CreateTableBuilder createTable(String name) {
		return factory.createTable(name);
	}

	@Override
	public AlterTableBuilder alterTable(String name) {
		return factory.alterTable(name);
	}

	@Override
	public ExecuteBuilder deleteView(String table) {
		return factory.deleteView(table);
	}

	@Override
	public CreateViewBuilder createView(String name) {
		return factory.createView(name);
	}

	@Override
	public CreateViewBuilder alterView(String name) {
		return factory.alterView(name);
	}

	@Override
	public ExecuteBuilder createIndex(String name, String table, String... colums) {
		return factory.createIndex(name, table, colums);
	}

	@Override
	public ExecuteBuilder deleteIndex(String name, String table) {
		return factory.deleteIndex(name, table);
	}
	
}
