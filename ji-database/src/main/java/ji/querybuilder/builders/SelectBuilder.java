package ji.querybuilder.builders;


import ji.querybuilder.Builder;
import ji.querybuilder.builders.share.Fetch;
import ji.querybuilder.builders.share.PlainSelect;
import ji.querybuilder.structures.SubSelect;

public interface SelectBuilder extends Builder, SubSelect, PlainSelect<SelectBuilder>, Fetch {

	SelectBuilder with(String name, SubSelect select);
	
}
