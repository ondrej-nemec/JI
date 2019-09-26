package querybuilder;

import java.sql.SQLException;

import database.support.ConnectionConsumer;
import database.support.DoubleConsumer;

public abstract class AbstractBuilder {

	protected DoubleConsumer consumer;
	
	public AbstractBuilder(final DoubleConsumer consumer) {
		this.consumer = consumer;
	}
	
	public abstract String getSql();

	protected void applyQuery(ConnectionConsumer conConsumer) throws SQLException {
		consumer.accept(conConsumer);
	}

}
