package testing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import common.Logger;
import database.Database;
import database.support.DoubleConsumer;
import querybuilder.InsertQueryBuilder;
import querybuilder.QueryBuilder;
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
	protected DoubleConsumer getDoubleConsumer() {
		return (consumer)->{consumer.accept(connection);};
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
					InsertQueryBuilder builder = getQueryBuilder().insert(table.getName());
					
					int length = row.getColumns().keySet().size();
					String[] columns = new String[length];
					String[] values = new String[length];
					
					int i = 0; //TODO jinak zadavat parametry, datove typy resit
					for (String key : row.getColumns().keySet()) {
						columns[i] = key;
						values[i] = "?";
						builder.addParameter(row.getColumns().get(key));
						i++;
					}
					builder.addColumns(columns).values(values).execute();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}						
			}
		}
	}

	@Override
	protected void createDb() throws SQLException {
		// not implemented
	}
	
	@Override
	public boolean createDbAndMigrate() {
		return nestedDatabase.createDbAndMigrate();
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		return nestedDatabase.getQueryBuilder();
	}

}
