package ji.querybuilder.executors;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import ji.database.wrappers.StatementWrapper;
import ji.querybuilder.buildersparent.Builder;
import ji.querybuilder.buildersparent.ParametrizedBuilder;

public interface BatchExecute<B> extends Execute, ParametrizedBuilder<B> {

	List<Builder> _getBuilders();
	
	default void execute() throws SQLException {
		try (Statement stat = getConnection().createStatement();) {
			for (Builder b : _getBuilders()) {
				String query = b.createSql(getParameters());
				if (stat instanceof StatementWrapper) {
					StatementWrapper w = StatementWrapper.class.cast(stat);
					w.getProfiler().builderQuery(w.ID, b.getSql(), query, getParameters());
				}
				stat.addBatch(query);
			}
		    stat.executeBatch();
		}
	}
}
