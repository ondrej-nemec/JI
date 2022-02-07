package ji.database.support;

import java.util.Map;

public interface SqlQueryProfiler {

	void execute(String identifier, String sql);
	
	void execute(String identifier);
	
	void executed();
	
	void executed(Object res);
	
	void prepare(String identifier, String sql);
	
	void addParam(String identifier, Object param);
	
	void builderQuery(String identifier, String query, String sql, Map<String, String> params);
	
}
