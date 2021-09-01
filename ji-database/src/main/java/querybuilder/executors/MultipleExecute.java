package querybuilder.executors;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import database.Database;
import database.wrappers.StatementWrapper;
import querybuilder.buildersparent.Builder;
import querybuilder.buildersparent.ParametrizedBuilder;

public interface MultipleExecute<B> extends Execute, ParametrizedBuilder<B> {

	List<Builder> _getBuilders();
	
	default void execute() throws SQLException {
		try (Statement stat = getConnection().createStatement();) {
			for (Builder b : _getBuilders()) {
				String query = b.createSql(getParameters());
				if (Database.PROFILER != null) {
					Database.PROFILER.builderQuery(StatementWrapper.class.cast(stat).ID, getSql(), query, getParameters());
				}
				if (!query.isEmpty()) {
					stat.execute(query);
				}
			}
		}
	}
	
}
