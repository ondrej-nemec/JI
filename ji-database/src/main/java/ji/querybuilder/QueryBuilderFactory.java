package ji.querybuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

import ji.querybuilder.builders.AlterTableBuilder;
import ji.querybuilder.builders.AlterViewBuilder;
import ji.querybuilder.builders.BatchBuilder;
import ji.querybuilder.builders.CreateIndexBuilder;
import ji.querybuilder.builders.CreateTableBuilder;
import ji.querybuilder.builders.CreateViewBuilder;
import ji.querybuilder.builders.DeleteBuilder;
import ji.querybuilder.builders.DeleteIndexBuilder;
import ji.querybuilder.builders.DeleteTableBuilder;
import ji.querybuilder.builders.DeleteViewBuilder;
import ji.querybuilder.builders.InsertBuilder;
import ji.querybuilder.builders.MultipleSelectBuilder;
import ji.querybuilder.builders.SelectBuilder;
import ji.querybuilder.builders.UpdateBuilder;

public interface QueryBuilderFactory {

	Connection getConnection();

	/**
	 * Begin transaction
	 * @throws SQLException
	 */
	void begin() throws SQLException;

	void commit() throws SQLException;

	void rollback() throws SQLException;

	Functions getSqlFunctions();

	BatchBuilder batch();

	MultipleSelectBuilder multiSelect(SelectBuilder builder);

	DeleteBuilder delete(String table);

	InsertBuilder insert(String table);

	UpdateBuilder update(String table);

	UpdateBuilder update(String table, String alias);

	SelectBuilder select(Function<Functions, String> select);

	SelectBuilder select(String... select);

	DeleteTableBuilder deleteTable(String table);

	CreateTableBuilder createTable(String name);

	AlterTableBuilder alterTable(String name);

	DeleteViewBuilder deleteView(String table);

	CreateViewBuilder createView(String name);

	AlterViewBuilder alterView(String name);

	CreateIndexBuilder createIndex(String name, String table, String... columns);

	DeleteIndexBuilder deleteIndex(String name, String table);

}