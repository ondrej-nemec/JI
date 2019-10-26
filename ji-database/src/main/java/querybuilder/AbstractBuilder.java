package querybuilder;

import java.sql.Connection;
import java.sql.SQLException;

import database.support.ConnectionConsumer;

public abstract class AbstractBuilder {

	protected Connection connection;
	
	public AbstractBuilder(final Connection connection) {
		this.connection = connection;
	}
	
	public abstract String getSql();

	@Deprecated
	protected void applyQuery(ConnectionConsumer conConsumer) throws SQLException {
	//	consumer.accept(conConsumer);
	}

}
