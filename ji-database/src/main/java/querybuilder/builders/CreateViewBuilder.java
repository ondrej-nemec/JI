package querybuilder.builders;

import querybuilder.executors.MultipleExecute;

public interface CreateViewBuilder extends MultipleExecute<CreateViewBuilder>, Select<CreateViewBuilder> {
	
	CreateViewBuilder select(String... params);

}
