package testing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import common.DatabaseConfig;
import database.Database;
import testing.entities.Row;
import testing.entities.Table;

public class DatabaseMock extends Database {

	private final Database nestedDatabase;
	
	private final List<Table> tables;
	
	public DatabaseMock(final DatabaseConfig config, final List<Table> tables) {
		super(config);
		this.nestedDatabase = Database.getDatabase(config);
		this.tables = tables;
	}
	
	public Database getNestedDatabase() {
		return nestedDatabase;
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
	public void applyQuery(final Consumer<Connection> chidlConsumer) throws SQLException {
		createDbAndMigrate();
		Consumer<Connection> parentConsumer = (con)->{
			try {
				con.setAutoCommit(false);
				applyDataSet(con);
				chidlConsumer.accept(con);
				con.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		};
		
		super.applyQuery(parentConsumer);
	}	

	private void applyDataSet(final Connection conn) {
		for(Table table : tables) {
			for(Row row : table.getRows()) {
				Consumer<Connection> insert = (connection)->{
					try {
						Statement st = connection.createStatement();
						st.executeUpdate(
								"INSERT INTO " + table.getName() + " " + getInsertString(row.getColumns())
						);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				};	
				insert.accept(conn);						
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
			values += "'" + columns.get(key) + "'";
		}
		names += ")";
		values += ")";
		
		return names + " VALUES " + values;
	}
	
}
