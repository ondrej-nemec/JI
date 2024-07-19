package ji.querybuilder.builders;

import java.sql.SQLException;
import java.util.function.Function;

import ji.querybuilder.Builder;
import ji.querybuilder.Functions;
import ji.querybuilder.enums.Where;

public interface DeleteBuilder extends Builder {

	default DeleteBuilder where(String where) {
		return where(n->where);
	}

	default DeleteBuilder where(String where, Where join) {
		return where(f->where, join);
	}
	
	default DeleteBuilder where(Function<Functions, String> where) {
		return where(where, Where.AND);
	}
	
	DeleteBuilder where(Function<Functions, String> where, Where join);
	
	DeleteBuilder addParameter(String name, Object value);
	
	int execute() throws SQLException;
	
}
