package querybuilder.executors;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import common.structures.DictionaryValue;
import querybuilder.buildersparent.Builder;

public interface InsertExecute<B> extends Execute, Builder {

	default DictionaryValue execute() throws SQLException {
		String sql = getSql();
		try (PreparedStatement stat = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stat.executeUpdate();
			try(ResultSet rs = stat.getGeneratedKeys();){
				if (rs.next()) {
					return new DictionaryValue(rs.getObject(1));
				}
				return new DictionaryValue(-1); // if no key generated - can be valid state
			}
		}
	}
	
}
