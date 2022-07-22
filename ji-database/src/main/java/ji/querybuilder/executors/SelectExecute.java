package ji.querybuilder.executors;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ji.common.structures.DictionaryValue;
import ji.common.structures.ThrowingFunction;
import ji.database.Database;
import ji.database.support.DatabaseRow;
import ji.database.wrappers.StatementWrapper;
import ji.querybuilder.buildersparent.ParametrizedBuilder;

public interface SelectExecute<B> extends Execute, ParametrizedBuilder<B> {
		
	/** INTERNAL */
	default <T> T _execute(ThrowingFunction<ResultSet, T, SQLException> callback) throws SQLException {
		String query = createSql();
		try (Statement stat = getConnection().createStatement(); ResultSet res = stat.executeQuery(query);) {
			if (Database.PROFILER != null) {
				StatementWrapper w = StatementWrapper.class.cast(stat);
				Database.PROFILER.builderQuery(w.ID, getSql(), query, getParameters());
			}
			return callback.apply(res);
		}
	}

	/** INTERNAL */
	default DatabaseRow _parseRow(ResultSet res) throws SQLException {
		DatabaseRow row = new DatabaseRow();
		for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
			row.addValue(res.getMetaData().getColumnLabel(i), _parseValue(res, i));
		}
		return row;
	}
	
	/** INTERNAL 
	 * @throws SQLException */
	default Object _parseValue(ResultSet rs, int index) throws SQLException {
		Object value = rs.getObject(index);
		if (value instanceof Date) {
			return LocalDate.parse(value.toString());
		}
		if (value instanceof Time) {
			return LocalTime.parse(value.toString());
		}
		if (value instanceof Timestamp) {
			String text = rs.getString(index);
			text = text.replaceFirst(" ", "T").replaceFirst(" ", "");
			if (text.contains("+") || StringUtils.countMatches(text, "-") > 3) {
				if (StringUtils.countMatches(text, ":") == 2) {
					text += ":00";
				}
				return ZonedDateTime.parse(text, DateTimeFormatter.ISO_ZONED_DATE_TIME);
			} else {
				return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
			}
		}
		return value;
	}
	
	/************/
	
	default DictionaryValue fetchSingle() throws SQLException {
		return _execute((rs)->{
			if (rs.next()) {
				return new DictionaryValue(_parseValue(rs, 1));
			}
			return null;
		});
	}

	default DatabaseRow fetchRow() throws SQLException {
		return _execute((res)->{
			if (res.next()) {
				return _parseRow(res);
			}
			return null;
		});
	}
	
	default <T> T fetchRow(Class<T> clazz) throws Exception {
		DatabaseRow row = fetchRow();
		if (row == null) {
			return null;
		}
		return row.parse(clazz);
	}

	default List<DatabaseRow> fetchAll() throws SQLException {
		return fetchAll((r)->r);
	}

	default <T> List<T> fetchAll(ThrowingFunction<DatabaseRow, T, SQLException> function) throws SQLException {
		return _execute((res)->{
			List<T> rows = new LinkedList<>();
			while (res.next()) {
				rows.add(function.apply(_parseRow(res)));
			}
			return rows;
		});
	}

}
