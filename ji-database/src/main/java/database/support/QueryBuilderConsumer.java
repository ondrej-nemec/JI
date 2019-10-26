package database.support;

import java.sql.SQLException;

import common.structures.ThrowingConsumer;
import querybuilder.QueryBuilder;

public interface QueryBuilderConsumer extends ThrowingConsumer<QueryBuilder, SQLException> {

	void accept(QueryBuilder queryBuilder) throws SQLException;
}
