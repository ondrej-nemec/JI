package querybuilder.mysql;

import java.sql.Connection;

import common.functions.Implode;
import querybuilder.builders.Select;
import querybuilder.buildersparent.QueryBuilderParent;
import querybuilder.enums.Join;

public abstract class SelectWrapper<C> extends QueryBuilderParent implements Select<C> {

	public SelectWrapper(Connection connection) {
		super(connection);
	}
	
	protected abstract C get();
		
	public C select(String... select) {
		query.append("SELECT " + Implode.implode(", ", select));
		return get();
	}

	@Override
	public C from(String table) {
		query.append(" FROM " + table);
		return get();
	}

	@Override
	public C join(String table, Join join, String on) {
		query.append(" " + EnumToMysqlString.joinToString(join) +" " + table + " ON " + on);
		return get();
	}

	@Override
	public C where(String where) {
		query.append(" WHERE (" + where + ")");
		return get();
	}

	@Override
	public C andWhere(String where) {
		query.append(" AND (" + where + ")");
		return get();
	}

	@Override
	public C orWhere(String where) {
		query.append(" OR (" + where + ")");
		return get();
	}

	@Override
	public C orderBy(String orderBy) {
		query.append(" ORDER BY " + orderBy);
		return get();
	}

	@Override
	public C groupBy(String groupBy) {
		query.append(" GROUP BY " + groupBy);
		return get();
	}

	@Override
	public C having(String having) {
		query.append(" HAVING " + having);
		return get();
	}

	@Override
	public C limit(int limit, int offset) {
		query.append(" LIMIT " + limit + " OFFSET " + offset);
		return get();
	}

}
