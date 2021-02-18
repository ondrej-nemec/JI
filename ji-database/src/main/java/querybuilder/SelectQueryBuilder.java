package querybuilder;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import database.support.DatabaseRow;

public interface SelectQueryBuilder extends Parameters<SelectQueryBuilder> {

	SelectQueryBuilder from(String table);
	
	default SelectQueryBuilder from(SelectQueryBuilder builder) {
		return from(String.format("(%s)", builder.createSql()));
	}
	
	SelectQueryBuilder join(String table, Join join, String on);

	default SelectQueryBuilder join(SelectQueryBuilder builder, Join join, String on) {
		return join(String.format("(%s)", builder.createSql()), join, on);
	}
	
	SelectQueryBuilder where(String where);
	
	SelectQueryBuilder andWhere(String where);
	
	SelectQueryBuilder orWhere(String where);
	
	SelectQueryBuilder orderBy(String orderBy);
	
	SelectQueryBuilder groupBy(String groupBy);
	
	SelectQueryBuilder having(String having);
	
	SelectQueryBuilder limit(int limit, int offset);
	
	Object fetchSingle() throws SQLException;
	
	DatabaseRow fetchRow() throws SQLException;
	
	List<DatabaseRow> fetchAll() throws SQLException;
	
	default <T> List<T> fetchAll(Function<DatabaseRow, T> function) throws SQLException {
		List<T> result = new LinkedList<>();
		for(DatabaseRow row : fetchAll()) {
			result.add(function.apply(row));
		}
		return result;
	}

}
