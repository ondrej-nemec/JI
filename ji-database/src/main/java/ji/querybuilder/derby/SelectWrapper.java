package ji.querybuilder.derby;

import java.sql.Connection;

import ji.common.functions.Implode;
import ji.querybuilder.builders.Select;
import ji.querybuilder.buildersparent.QueryBuilderParent;
import ji.querybuilder.enums.Join;

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
		query.append(" " + EnumToDerbyString.joinToString(join) +" " + table + " ON " + on);
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
		query.append(String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", offset, limit));
		return get();
	}
}
