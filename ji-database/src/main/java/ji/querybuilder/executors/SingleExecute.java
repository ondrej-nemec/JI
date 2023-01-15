package ji.querybuilder.executors;

import java.sql.SQLException;
import java.sql.Statement;

import ji.database.wrappers.StatementWrapper;
import ji.querybuilder.buildersparent.ParametrizedBuilder;

public interface SingleExecute<B> extends Execute, ParametrizedBuilder<B> {

	default int execute() throws SQLException {
		String query = createSql();
		try (Statement stat = getConnection().createStatement();) {
			if (stat instanceof StatementWrapper) {
				StatementWrapper w = StatementWrapper.class.cast(stat);
				w.getProfiler().builderQuery(w.ID, getSql(), query, getParameters());
			}
			return stat.executeUpdate(query);
		}
	}
}
