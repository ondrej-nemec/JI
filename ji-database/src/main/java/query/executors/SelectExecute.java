package query.executors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import common.structures.DictionaryValue;
import common.structures.ThrowingFunction;
import database.support.DatabaseRow;
import query.buildersparent.ParametrizedBuilder;

public interface SelectExecute<B> extends Execute, ParametrizedBuilder<B> {
		
	/** INTERNAL */
	default <T> T _execute(ThrowingFunction<ResultSet, T, SQLException> callback) throws SQLException {
		String query = createSql();
		// TODO log.log(builder.getSql(), builder.getParameters());
		try (Statement stat = getConnection().createStatement(); ResultSet res = stat.executeQuery(query);) {
			return callback.apply(res);
		}
	}

	/** INTERNAL */
	default DatabaseRow _parseRow(ResultSet res) throws SQLException {
		DatabaseRow row = new DatabaseRow();
		for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
			row.addValue(res.getMetaData().getColumnLabel(i), res.getObject(i));
		}
		return row;
	}
	
	/************/
	
	default DictionaryValue fetchSingle() throws SQLException {
		return _execute((rs)->{
			if (rs.next()) {
				return new DictionaryValue(rs.getObject(1));
			}
			return null;
		});
	}

	default DatabaseRow fetchRow() throws SQLException {
		return _execute((res)->{
			if (res.next()) {
				return _parseRow(res);
			}
			return new DatabaseRow(); // TODO back compatibility, null or optional can be better
		});
	}
	
	default <T> T fetchRow(Class<T> clazz) throws Exception {
		DatabaseRow row = fetchRow();
		if (row.values().isEmpty()) { // TODO == null after fetchRow will be returning null
			return null;
		}
		return row.parse(clazz);
	}

	default List<DatabaseRow> fetchAll() throws SQLException {
		return fetchAll((r)->r);
	}

	default <T> List<T> fetchAll(Function<DatabaseRow, T> function) throws SQLException {
		return _execute((res)->{
			List<T> rows = new LinkedList<>();
			while (res.next()) {
				rows.add(function.apply(_parseRow(res)));
			}
			return rows;
		});
	}

}
