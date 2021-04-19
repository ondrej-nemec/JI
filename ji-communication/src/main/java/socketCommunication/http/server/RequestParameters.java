package socketCommunication.http.server;

import java.util.HashMap;
import java.util.List;

import common.structures.MapDictionary;
import common.structures.Tuple2;

public class RequestParameters extends MapDictionary<String, Object> {
	
	@SafeVarargs
	@Deprecated
	public RequestParameters(Tuple2<String, ?> ...tuples) {
		super(new HashMap<>());
		for (Tuple2<String, ?> tuple : tuples) {
			put(tuple._1(), tuple._2());
		}
	}
	
	public RequestParameters() {
		super(new HashMap<>());
	}
	
	public UploadedFile getUploadedFile(String key) {
		return UploadedFile.class.cast(getValue(key));
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

}
