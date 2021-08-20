package querybuilder.builders;

import querybuilder.executors.SelectExecute;

public interface SelectBuilder extends SelectExecute<SelectBuilder>, Select<SelectBuilder> {
/*
	SelectBuilder from(String table);
	
	default SelectBuilder from(SelectBuilder builder) {
		return from(String.format("(%s)", builder.createSql()));
	}
	
	SelectBuilder join(String table, Join join, String on);

	default SelectBuilder join(SelectBuilder builder, Join join, String on) {
		return join(String.format("(%s)", builder.createSql()), join, on);
	}
	
	SelectBuilder where(String where);
	
	SelectBuilder andWhere(String where);
	
	SelectBuilder orWhere(String where);
	
	SelectBuilder orderBy(String orderBy);
	
	SelectBuilder groupBy(String groupBy);
	
	SelectBuilder having(String having);
	
	SelectBuilder limit(int limit, int offset);
	*/
}
