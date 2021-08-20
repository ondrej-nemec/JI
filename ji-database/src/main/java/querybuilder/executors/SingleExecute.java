package querybuilder.executors;

import java.sql.SQLException;
import java.sql.Statement;

import database.Database;
import querybuilder.buildersparent.ParametrizedBuilder;

public interface SingleExecute<B> extends Execute, ParametrizedBuilder<B> {

	default int execute() throws SQLException {
		String query = createSql();
		if (Database.PROFILER != null) {
			Database.PROFILER.builderQuery(getSql(), query, getParameters());
		}
		try (Statement stat = getConnection().createStatement();) {
			return stat.executeUpdate(query);
		}
	}
}
