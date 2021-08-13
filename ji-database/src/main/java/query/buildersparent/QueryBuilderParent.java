package query.buildersparent;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class QueryBuilderParent {
	
	protected final Connection connection;
	protected final StringBuilder query;
	protected final Map<String, String> params;
	
	public QueryBuilderParent(Connection connection) {
		this.connection = connection;
		this.query = new StringBuilder();
		this.params = new HashMap<>();
	}
	
	public Connection getConnection() {
		return connection;
	}

	public String getSql() {
		return query.toString();
	}

	public Map<String, String> getParameters() {
		return params;
	}
	
	protected void _addNotEscaped(String name, String value) {
		params.put(name, value);
	}

}
