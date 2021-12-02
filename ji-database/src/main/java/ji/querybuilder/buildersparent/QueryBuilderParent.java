package ji.querybuilder.buildersparent;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class QueryBuilderParent {
	
	public enum Type {
		NORMAL, INSERT, APPEND
	}
	
	protected final Connection connection;
	protected final StringBuilder append;
	protected final StringBuilder query;
	protected final Map<String, String> params;
	private final Type type;

	public QueryBuilderParent(Connection connection) {
		this(connection, Type.NORMAL);
	}
	
	public QueryBuilderParent(Connection connection, Type type) {
		this.connection = connection;
		this.query = new StringBuilder();
		this.append = new StringBuilder();
		this.params = new HashMap<>();
		this.type = type;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public String getSql() {
		switch (type) {
			case APPEND: return new StringBuilder().append(query).append(append).append(")").toString();
			case INSERT: return getInsertSql();
			case NORMAL:
			default: return query.toString();
		}
	}
	
	private String getInsertSql() {
		String columns = "(";
		String values = "VALUES (";
		
		boolean first = true;
		for (String name : params.keySet()) {
			if (first) {
				first = false;
			} else {
				columns += ", ";
				values += ", ";
			}
			columns += name;
			values += params.get(name);
		}
		
		columns += ")";
		values += ")";
		return query + " " + columns + " " + values;
	}

	public Map<String, String> getParameters() {
		return params;
	}
	
	protected void _addNotEscaped(String name, String value) {
		params.put(name, value);
	}

}
