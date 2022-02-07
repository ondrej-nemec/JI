package ji.querybuilder.executors;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import ji.database.Database;
import ji.database.wrappers.StatementWrapper;
import ji.querybuilder.buildersparent.Builder;
import ji.querybuilder.buildersparent.ParametrizedBuilder;

public interface MultipleExecute<B> extends Execute, ParametrizedBuilder<B> {

	List<Builder> _getBuilders();
	
	default void execute() throws SQLException {
		try (Statement stat = getConnection().createStatement();) {
			for (Builder b : _getBuilders()) {
				String query = b.createSql(getParameters());
				if (Database.PROFILER != null) {
					StatementWrapper w = StatementWrapper.class.cast(stat);
					Database.PROFILER.builderQuery(w.ID, getSql(), query, getParameters());
				}
				if (!query.isEmpty()) {
					stat.execute(query);
				}
			}
		}
	}
	
}
