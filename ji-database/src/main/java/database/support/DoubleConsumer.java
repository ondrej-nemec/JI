package database.support;

import java.sql.SQLException;

import common.structures.ThrowingConsumer;

public interface DoubleConsumer extends ThrowingConsumer<ConnectionConsumer, SQLException> {

	void accept(ConnectionConsumer connection) throws SQLException;
	
}
