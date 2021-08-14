package query.wrappers;

import query.buildersparent.Builder;
import querybuilder.Join;

public interface Select<C> extends Builder {

	C from(String table);
	
	default C from(Select<C> builder) {
		return from(String.format("(%s)", builder.createSql()));
	}
	
	C join(String table, Join join, String on);

	default C join(Select<C> builder, Join join, String on) {
		return join(String.format("(%s)", builder.createSql()), join, on);
	}
	
	C where(String where);
	
	C andWhere(String where);
	
	C orWhere(String where);
	
	C orderBy(String orderBy);
	
	C groupBy(String groupBy);
	
	C having(String having);
	
	C limit(int limit, int offset);
}
