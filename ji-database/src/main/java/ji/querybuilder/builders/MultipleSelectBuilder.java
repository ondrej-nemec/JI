package ji.querybuilder.builders;

import ji.querybuilder.Builder;
import ji.querybuilder.builders.share.Fetch;
import ji.querybuilder.builders.share.Ordered;
import ji.querybuilder.builders.share.Parametrized;
import ji.querybuilder.structures.SubSelect;

public interface MultipleSelectBuilder extends Builder, SubSelect, Fetch, Parametrized<MultipleSelectBuilder>, Ordered<MultipleSelectBuilder> {
	
	MultipleSelectBuilder union(SelectBuilder select);
	
	MultipleSelectBuilder intersect(SelectBuilder select);
	
	MultipleSelectBuilder unionAll(SelectBuilder select);
	
	MultipleSelectBuilder except(SelectBuilder select);

}
