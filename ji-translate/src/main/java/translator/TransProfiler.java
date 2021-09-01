package translator;

import java.util.Map;

public interface TransProfiler {

	void missingParameter(String module, String key, Map<String, Object> variables, String locale);
	
	void missingLocale(String locale);
	
}
