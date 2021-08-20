package querybuilder.executors;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import database.Database;
import querybuilder.buildersparent.Builder;
import querybuilder.buildersparent.ParametrizedBuilder;

public interface BatchExecute<B> extends Execute, ParametrizedBuilder<B> {

	List<Builder> _getBuilders();
	
	default void execute() throws SQLException {
		try (Statement stat = getConnection().createStatement();) {
			for (Builder b : _getBuilders()) {
				String query = b.createSql(getParameters());
				if (Database.PROFILER != null) {
					Database.PROFILER.builderQuery(b.getSql(), query, getParameters());
				}
				stat.addBatch(query);
			}
		    stat.executeBatch();
		}
	}
}
