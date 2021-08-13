package query.executors;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import query.buildersparent.ParametrizedBuilder;
import query.buildersparent.Builder;

public interface BatchExecute<B> extends Execute, ParametrizedBuilder<B> {

	List<Builder> _getBuilders();
	
	default void execute() throws SQLException {
		try (Statement stat = getConnection().createStatement();) {
			for (Builder b : _getBuilders()) {
				String query = b.createSql(getParameters());
				// TODO log.log(b.getSql(), b.getParameters());
				stat.addBatch(query);
			}
		    stat.executeBatch();
		}
	}
}
