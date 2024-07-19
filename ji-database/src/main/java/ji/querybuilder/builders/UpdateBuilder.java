package ji.querybuilder.builders;

import java.sql.SQLException;
import java.util.function.Function;

import ji.querybuilder.Builder;
import ji.querybuilder.Functions;
import ji.querybuilder.builders.share.Parametrized;
import ji.querybuilder.builders.share.Wheres;

public interface UpdateBuilder extends Builder, Wheres<UpdateBuilder>, Parametrized<UpdateBuilder> {
	
	default UpdateBuilder set(String update) {
		return set(f->update);
	}
	
	UpdateBuilder set(Function<Functions, String> update);
	
	int execute() throws SQLException;
	
}
