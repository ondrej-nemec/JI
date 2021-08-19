package query.executors;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import query.buildersparent.ParametrizedBuilder;
import query.buildersparent.Builder;

public interface MultipleExecute<B> extends Execute, ParametrizedBuilder<B> {

	List<Builder> _getBuilders();
	
	default void execute() throws SQLException {
		try (Statement stat = getConnection().createStatement();) {
			for (Builder b : _getBuilders()) {
				String query = b.createSql(getParameters());
				// TODO log.log(parametrized.getSql(), getParameters());
				if (!query.isEmpty()) {
					stat.execute(query);
				}
			}
		}
	}
	
}
