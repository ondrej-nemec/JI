package query.wrappers;

import query.executors.MultipleExecute;

public interface CreateViewBuilder extends MultipleExecute<CreateViewBuilder>, Select<CreateViewBuilder> {
	
	CreateViewBuilder select(String... params);

}
