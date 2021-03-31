package socketCommunication.http.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.structures.Dictionary;
import common.structures.Tuple2;

public class RequestParameters extends HashMap<String, Object> implements Dictionary {

	private static final long serialVersionUID = 1L;
	
	@SafeVarargs
	public RequestParameters(Tuple2<String, ?> ...tuples) {
		for (Tuple2<String, ?> tuple : tuples) {
			put(tuple._1(), tuple._2());
		}
	}
	
	public UploadedFile getUploadedFile(String key) {
		return UploadedFile.class.cast(get(key));
	}
	
	public List<?> getList(String key) {
		return List.class.cast(get(key));
	}
	
	public Map<?, ?> getMap(String key) {
		return Map.class.cast(get(key));
	}
	
	@Override
	public Object getValue(String name) {
		return get(name);
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

}
