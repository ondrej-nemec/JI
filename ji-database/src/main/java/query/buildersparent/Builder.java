package query.buildersparent;

import java.util.Map;

public interface Builder {

	String getSql();
	
	Map<String, String> getParameters();

	default String createSql(Map<String, String> params) {
		return getSql();
	}

	default String createSql() {
		return createSql(getParameters());
	}
	
}
