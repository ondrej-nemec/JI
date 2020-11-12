package testing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import common.Logger;
import database.Database;
import database.DatabaseConfig;
import database.support.ConnectionFunction;
import database.support.DoubleConsumer;
import querybuilder.InsertQueryBuilder;
import testing.entities.Row;
import testing.entities.Table;

public class DatabaseMock extends Database {
	
	private final List<Table> tables;
	
	private final Connection connection;
	
	public DatabaseMock(DatabaseConfig config, final List<Table> tables, Logger logger) {
		super(config, true, logger);
		this.tables = tables;
		try {
			this.connection = pool.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Connection to database could not be created", e);
		}
		
	}

	public void applyDataSet() throws SQLException {
		applyBuilder((querybuilder)->{
			for(Table table : tables) {
				for(Row row : table.getRows()) {
					InsertQueryBuilder builder = querybuilder.insert(table.getName());
    				for (String key : row.getColumns().keySet()) {
    					builder = builder.addValue(key, row.getColumns().get(key));
    				}
    				builder.execute();
				}
			}
			return null;
		});
	}
	
	@Override
	protected <T> DoubleConsumer<T> getDoubleFunction(ConnectionFunction<T> consumer) {
		return ()->{
			return consumer.apply(connection);
		};
	}
	
	protected void rollback() throws SQLException {
		pool.returnAllConnections();
	}
}
