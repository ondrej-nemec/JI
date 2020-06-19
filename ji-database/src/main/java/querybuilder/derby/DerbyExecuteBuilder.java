package querybuilder.derby;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import querybuilder.ExecuteQueryBuilder;

public class DerbyExecuteBuilder implements ExecuteQueryBuilder {

	private final String sql;
	
	private final Connection connection;
	
	public DerbyExecuteBuilder(Connection connection ,String sql) {
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
