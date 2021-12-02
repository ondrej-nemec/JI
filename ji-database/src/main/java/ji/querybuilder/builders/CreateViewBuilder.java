package ji.querybuilder.builders;

import ji.querybuilder.executors.MultipleExecute;

public interface CreateViewBuilder extends MultipleExecute<CreateViewBuilder>, Select<CreateViewBuilder> {
	
	CreateViewBuilder select(String... params);

}
