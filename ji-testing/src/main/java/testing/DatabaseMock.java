package testing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import common.Logger;
import database.Database;
import database.support.ConnectionConsumer;
import querybuilder.DeleteQueryBuilder;
import querybuilder.InsertQueryBuilder;
import querybuilder.SelectQueryBuilder;
import querybuilder.UpdateQueryBuilder;
import testing.entities.Row;
import testing.entities.Table;
import utils.env.DatabaseConfig;

public class DatabaseMock extends Database {

	private final Database nestedDatabase;
	
	private final List<Table> tables;
	
	private Connection connection;
	
	public DatabaseMock(final DatabaseConfig config, final List<Table> tables, final Logger logger) {
		super(config, logger);
		this.nestedDatabase = Database.getDatabase(config, logger);
		this.tables = tables;
	}

	@Override
	public void startServer() {
		nestedDatabase.startServer();
	}

	@Override
	public void stopServer() {
		nestedDatabase.stopServer();
	}
	
	@Override
	public void applyQuery(ConnectionConsumer consumer) throws SQLException {
		consumer.accept(connection);
	}
	
	public Database getNestedDatabase() {
		return nestedDatabase;
	}
	
	public void prepare() throws SQLException {
		connection = DriverManager.getConnection(getConnectionString(), createProperties());
		connection.setAutoCommit(false);
		applyDataSet(connection);
	}
	
	public void clean() throws SQLException {
		connection.rollback();
		connection.close();
	}
	
	private void applyDataSet(final Connection conn) {
		for(Table table : tables) {
			for(Row row : table.getRows()) {
				try {
					Statement st = connection.createStatement();
					st.executeUpdate(
							"INSERT INTO " + table.getName() + " " + getInsertString(row.getColumns())
					);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}						
			}
		}
	}
	
	private String getInsertString(Map<String, String> columns) {
		String names = "(";
		String values = "(";
		boolean first = true;
		for(String key : columns.keySet()) {
			if (first) {
				first = false;
			} else {
				names += ", ";
				values += ", ";
			}
			names += key;
			values += columns.get(key);
		}
		names += ")";
		values += ")";
		
		return names + " VALUES " + values;
	}

	@Override
	protected void createDb() throws SQLException {
		nestedDatabase.createDbAndMigrate();
	}

	@Override
	public SelectQueryBuilder getSelectBuilder() {
		return nestedDatabase.getSelectBuilder();
	}

	@Override
	public UpdateQueryBuilder getUpdateBuilder() {
		return nestedDatabase.getUpdateBuilder();
	}

	@Override
	public DeleteQueryBuilder getDeletetBuilder() {
		return nestedDatabase.getDeletetBuilder();
	}

	@Override
	public InsertQueryBuilder getInsertBuilder() {
		return nestedDatabase.getInsertBuilder();
	}

}
