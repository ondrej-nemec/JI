package querybuilder.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import querybuilder.InsertQueryBuilder;

public class PostgreSqlInsertBuilder implements InsertQueryBuilder {

	private final String query;
	
	private final Map<String, String> params;
	
	private final Connection connection;
	
	public PostgreSqlInsertBuilder(final Connection connection, final String table) {
		this.connection = connection;
		this.query = "INSERT INTO " + table;
		this.params = new HashMap<>();
	}

	@Override
	public InsertQueryBuilder addNotEscapedValue(String columnName, String value) {
		params.put(columnName, value);
		return this;
	}

	@Override
	public Object execute() throws SQLException {
		try (PreparedStatement stat = connection.prepareStatement(getSql(), Statement.RETURN_GENERATED_KEYS)) {
			stat.executeUpdate();
			try(ResultSet rs = stat.getGeneratedKeys();){
				if (rs.next()) {
					return rs.getObject(1);
				}
				return -1; // if no key generated - can be valid state
			}
		}
	}

	@Override
	public String getSql() {
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

}
