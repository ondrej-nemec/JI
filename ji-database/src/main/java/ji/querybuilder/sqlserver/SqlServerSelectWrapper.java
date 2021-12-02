package ji.querybuilder.sqlserver;

import java.sql.Connection;

import ji.common.functions.Implode;
import ji.querybuilder.builders.Select;
import ji.querybuilder.buildersparent.MultyBuilderParent;
import ji.querybuilder.enums.Join;

public abstract class SqlServerSelectWrapper<C> extends MultyBuilderParent implements Select<C> {
	
	public SqlServerSelectWrapper(Connection connection) {
		super(connection);
	}
	
	protected abstract C get();
		
	public C select(String... select) {
		mainBuilder.append("SELECT " + Implode.implode(", ", select));
		return get();
	}

	@Override
	public C from(String table) {
		mainBuilder.append(" FROM " + table);
		return get();
	}

	@Override
	public C join(String table, Join join, String on) {
		mainBuilder.append(" " + EnumToSqlServerString.joinToString(join) +" " + table + " ON " + on);
		return get();
	}

	@Override
	public C where(String where) {
		mainBuilder.append(" WHERE (" + where + ")");
		return get();
	}

	@Override
	public C andWhere(String where) {
		mainBuilder.append(" AND (" + where + ")");
		return get();
	}

	@Override
	public C orWhere(String where) {
		mainBuilder.append(" OR (" + where + ")");
		return get();
	}

	@Override
	public C orderBy(String orderBy) {
		mainBuilder.append(" ORDER BY " + orderBy);
		return get();
	}

	@Override
	public C groupBy(String groupBy) {
		mainBuilder.append(" GROUP BY " + groupBy);
		return get();
	}

	@Override
	public C having(String having) {
		mainBuilder.append(" HAVING " + having);
		return get();
	}

	@Override
	public C limit(int limit, int offset) {
        if (mainBuilder.toString().contains("ORDER BY")) {
            throw new RuntimeException("SQL Server requires 'order by' clause before limit and offset");
        }
		mainBuilder.append(" OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY");
		return get();
	}
	
}
