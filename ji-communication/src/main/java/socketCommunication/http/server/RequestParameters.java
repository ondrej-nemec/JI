package socketCommunication.http.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.structures.Tuple2;

public class RequestParameters extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	
	@SafeVarargs
	public RequestParameters(Tuple2<String, ?> ...tuples) {
		for (Tuple2<String, ?> tuple : tuples) {
			put(tuple._1(), tuple._2());
		}
	}
	
	public String getString(String key) {
		Object val = get(key);
		return val == null ? null : val.toString();
	}
	
	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}

	public Integer getInt(String key) {
		return Integer.parseInt(getString(key));
	}
	
	public Long getLong(String key) {
		return Long.parseLong(getString(key));
	}
	
	public Double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}
	
	public List<?> getList(String key) {
		return List.class.cast(get(key));
	}
	
	public Map<?, ?> getMap(String key) {
		return Map.class.cast(get(key));
	}
	
	/*
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("RequestParameters:");
		forEach((key, item)->{
			b.append(String.format("{%s=%s[%s]},", key, item.getClass(), item));
		});
		return b.toString();
	}
	*/
}
