package query.executors;

import java.sql.SQLException;
import java.sql.Statement;

import query.buildersparent.ParametrizedBuilder;

public interface SingleExecute<B> extends Execute, ParametrizedBuilder<B> {

	default int execute() throws SQLException {
		String query = createSql();
		// TODO log.log(builder.getSql(), builder.getParameters());
		try (Statement stat = getConnection().createStatement();) {
			return stat.executeUpdate(query);
		}
	}
}
