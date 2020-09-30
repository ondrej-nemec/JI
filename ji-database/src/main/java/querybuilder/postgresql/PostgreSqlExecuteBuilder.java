package querybuilder.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import querybuilder.ExecuteQueryBuilder;

public class PostgreSqlExecuteBuilder implements ExecuteQueryBuilder {

	private final String sql;
	
	private final Connection connection;
	
	public PostgreSqlExecuteBuilder(Connection connection ,String sql) {
		this.sql = sql;
		this.connection = connection;
	}
	
	@Override
	public void execute() throws SQLException {
		try (Statement stat = connection.createStatement();) {
			stat.executeUpdate(getSql());
		}
	}

	@Override
	public String getSql() {
		return sql;
	}

}
