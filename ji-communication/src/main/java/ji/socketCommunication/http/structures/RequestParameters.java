package ji.socketCommunication.http.structures;

import java.util.HashMap;

import ji.common.structures.MapDictionary;

public class RequestParameters extends MapDictionary<String, Object> {
	
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

	@Override
	public String toString() {
		return String.format("RequestParameters [%s]", super.toString());
	}
	
}
