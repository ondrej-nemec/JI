package query.wrappers;

import query.executors.SingleExecute;

public interface CreateViewBuilder extends SingleExecute<CreateViewBuilder>, Select<CreateViewBuilder> {
	
	CreateViewBuilder select(String... params);

}
