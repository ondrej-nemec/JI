package ji.querybuilder.builders;

import ji.querybuilder.buildersparent.Builder;
import ji.querybuilder.enums.Join;

public interface Select<C> extends Builder {

	C from(String table);
	
	default C from(Select<SelectBuilder> builder, String name) {
		return from(String.format("(%s) %s", builder.createSql(), name));
	}
	
	C join(String table, Join join, String on);

	default C join(Select<SelectBuilder> builder, String name, Join join, String on) {
		return join(String.format("(%s) %s", builder.createSql(), name), join, on);
	}
	
	C where(String where);
	
	C andWhere(String where);
	
	C orWhere(String where);
	
	C orderBy(String orderBy);
	
	C groupBy(String groupBy);
	
	C having(String having);
	
	C limit(int limit, int offset);
}
