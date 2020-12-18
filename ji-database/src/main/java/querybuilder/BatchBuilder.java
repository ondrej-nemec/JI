package querybuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class BatchBuilder {

	private final List<Batch> batches;
	private final Connection connection;
	
	public BatchBuilder(Connection connection) {
		this.connection = connection;
		this.batches = new LinkedList<>();
	}
	
	public BatchBuilder addBatch(Batch batch) {
		batches.add(batch);
		return this;
	}
	
	public void execute() throws SQLException {
		try (Statement stat = connection.createStatement()) {
			for (Batch batch : batches) {
				stat.addBatch(batch.getQuerySql());
			}
			stat.executeBatch();
		}
	}
	
}
