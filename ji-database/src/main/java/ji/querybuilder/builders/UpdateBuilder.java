package ji.querybuilder.builders;

import java.sql.SQLException;
import java.util.function.Function;

import ji.querybuilder.Builder;
import ji.querybuilder.Functions;
import ji.querybuilder.builders.parents.Joins;
import ji.querybuilder.builders.parents.Parametrized;
import ji.querybuilder.builders.parents.Wheres;
import ji.querybuilder.structures.SubSelect;

public interface UpdateBuilder extends Builder, Joins<UpdateBuilder>, Wheres<UpdateBuilder>, Parametrized<UpdateBuilder> {
	
	UpdateBuilder with(String name, SubSelect select);
	
	default UpdateBuilder set(String update) {
		return set(f->update);
	}
	
	UpdateBuilder set(Function<Functions, String> update);
	
	int execute() throws SQLException;
	
}
