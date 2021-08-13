package query.wrappers;

import query.executors.SingleExecute;
import querybuilder.Join;

public interface CreateViewBuilder extends SingleExecute<CreateViewBuilder> {
	
	CreateViewBuilder select(String... params);

	CreateViewBuilder from(String table);
	
	CreateViewBuilder join(String table, Join join, String on);

	CreateViewBuilder where(String where);
	
	CreateViewBuilder andWhere(String where);
	
	CreateViewBuilder orWhere(String where);
	
	CreateViewBuilder orderBy(String orderBy);
	
	CreateViewBuilder groupBy(String groupBy);
	
	CreateViewBuilder having(String having);
	
	CreateViewBuilder limit(int limit, int offset);

}
