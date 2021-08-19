package query.buildersparent;

import java.util.HashMap;
import java.util.Map;

public class SimpleBuilder implements Builder {

	private StringBuilder query = new StringBuilder();
	
	public SimpleBuilder(String query) {
		this.query.append(query);
	}
	
	public SimpleBuilder append(String query) {
		this.query.append(query);
		return this;
	}
	
	public SimpleBuilder() {}
	
	@Override
	public String getSql() {
		return query.toString();
	}

	@Override
	public Map<String, String> getParameters() {
		return new HashMap<>();
	}

}
