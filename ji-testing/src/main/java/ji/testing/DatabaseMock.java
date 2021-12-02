package ji.testing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import ji.common.Logger;
import ji.database.Database;
import ji.database.DatabaseConfig;
import ji.database.support.ConnectionFunction;
import ji.database.support.DoubleConsumer;
import ji.querybuilder.builders.BatchBuilder;
import ji.querybuilder.builders.InsertBuilder;
import ji.testing.entities.Row;
import ji.testing.entities.Table;

public class DatabaseMock extends Database {
	
	private final List<Table> tables;
	
	private final Connection connection;
	
	public DatabaseMock(DatabaseConfig config, final List<Table> tables, Logger logger) {
		super(config, true, logger);
		this.tables = tables;
		try {
			createDbIfNotExists();
			this.connection = pool.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Connection to database could not be created", e);
		}
		
	}

	public void applyDataSet() throws SQLException {
		applyBuilder((builder)->{
			BatchBuilder batch = builder.batch();
			for(Table table : tables) {
				for(Row row : table.getRows()) {
					InsertBuilder insert = builder.insert(table.getName());
    				for (String key : row.getColumns().keySet()) {
    					 insert.addValue(key, row.getColumns().get(key));
    				}
    				batch.addBatch(insert);
				}
			}
			batch.execute();
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
