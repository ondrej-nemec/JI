package database.support;

import java.sql.SQLException;

import common.structures.ThrowingFunction;
import querybuilder.QueryBuilder;

public interface QueryBuilderFunction<T> extends ThrowingFunction<QueryBuilder, T, SQLException> {

	T apply(QueryBuilder queryBuilder) throws SQLException;
}
