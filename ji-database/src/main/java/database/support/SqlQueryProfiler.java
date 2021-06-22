package database.support;

public interface SqlQueryProfiler {
	
	public static SqlQueryProfiler PROFILER = null;

	void execute(String identifier, String sql);
	
	void execute(String identifier);
	
	void prepare(String identifier, String sql);
	
	void addParam(String identifier, Object param);
	
}
