package ji.querybuilder;

import java.sql.Connection;
import java.sql.SQLException;

import ji.querybuilder.builders.AlterTableBuilder;
import ji.querybuilder.builders.BatchBuilder;
import ji.querybuilder.builders.CreateTableBuilder;
import ji.querybuilder.builders.CreateViewBuilder;
import ji.querybuilder.builders.DeleteBuilder;
import ji.querybuilder.builders.ExecuteBuilder;
import ji.querybuilder.builders.Functions;
import ji.querybuilder.builders.InsertBuilder;
import ji.querybuilder.builders.MultipleSelectBuilder;
import ji.querybuilder.builders.SelectBuilder;
import ji.querybuilder.builders.UpdateBuilder;

public class QueryBuilder implements QueryBuilderFactory, PreparedQueries {

	private final QueryBuilderFactory factory;
	
	public QueryBuilder(QueryBuilderFactory factory) {
		this.factory = factory;
	}

	public Connection getConnection() {
		return factory.getConnection();
	}
	
	/**
	 * Begin transaction
	 * @throws SQLException
	 */
	public void begin() throws SQLException {
		factory.getConnection().setAutoCommit(false);
	}

	/**
	 * Commit transaction
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		factory.getConnection().commit();
	}
	
	/**
	 * Rollback transaction
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		factory.getConnection().rollback();
	}
	
	public BatchBuilder batch() {
		return new BatchBuilder(factory.getConnection());
	}
	
	public MultipleSelectBuilder multiSelect(SelectBuilder builder) {
		return new MultipleSelectBuilder(factory.getConnection(), builder);
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
	public SelectBuilder select(String... select) {
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
