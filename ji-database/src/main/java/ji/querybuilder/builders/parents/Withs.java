package ji.querybuilder.builders.parents;

import ji.querybuilder.builders.MultipleSelectBuilder;
import ji.querybuilder.builders.SelectBuilder;
import ji.querybuilder.structures.SubSelect;

public interface Withs<P> {

	default P with(SelectBuilder builder, String alias) {
		return _with(builder, alias);
	}
	
	default P with(MultipleSelectBuilder builder, String alias) {
		return _with(builder, alias);
	}
	
	P _with(SubSelect select, String alias);
	
}
