package ji.querybuilder.builders;

import ji.querybuilder.buildersparent.Builder;
import ji.querybuilder.enums.Join;

public interface Select<C> extends Builder {

	C from(String table);

	default C from(String table, String alias) {
		return from(String.format("%s %s", table, alias));
	}
	
	default C from(Select<SelectBuilder> builder, String alias) {
		return from(String.format("(%s) %s", builder.createSql(), alias));
	}
	
	C join(String table, Join join, String on);
	
	default C join(String table, String alias, Join join, String on) {
		return join(String.format("%s %s", table, alias), join, on);
	}

	default C join(Select<SelectBuilder> builder, String alias, Join join, String on) {
		return join(String.format("(%s) %s", builder.createSql(), alias), join, on);
	}
	
	C where(String where);
	
	C andWhere(String where);
	
	C orWhere(String where);
	
	C orderBy(String orderBy);
	
	C groupBy(String groupBy);
	
	C having(String having);
	
	C limit(int limit, int offset);
}
