package socketCommunication.http.server;

import java.util.HashMap;

import common.structures.MapDictionary;
import common.structures.Tuple2;

public class RequestParameters extends MapDictionary<String, Object> {
	
	private String plainBody;
	
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

	public String getPlainBody() {
		return plainBody;
	}
	
	public void setPlainBody(String plainText) {
		this.plainBody = plainText;
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public String toString() {
		return String.format("RequestParameters [%s, %s]", plainBody, super.toString());
	}
	
}
