package ji.querybuilder.builder_impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import ji.common.structures.DictionaryValue;
import ji.querybuilder.DbInstance;
import ji.querybuilder.Functions;
import ji.querybuilder.builder_impl.share.Escape;
import ji.querybuilder.builders.InsertBuilder;
import ji.querybuilder.builders.share.PlainSelect;

public class InsertBuilderImpl implements InsertBuilder {

	// TODO with
	private final Connection connection;
	private final DbInstance instance;
	private final String table;
	private final Map<String, String> values;
	private PlainSelect<?> select;
	
	public InsertBuilderImpl(Connection connection, DbInstance instance, String table) {
		this.connection = connection;
		this.instance = instance;
		this.table = table;
		this.values = new HashMap<>();
	}
	
	public String getTable() {
		return table;
	}
	
	public Map<String, String> getValues() {
		return values;
	}
	
	public PlainSelect<?> getSelect() {
		return select;
	}

	@Override
	public String getSql() {
		return instance.createSql(this);
	}

	@Override
	public InsertBuilder addValue(String columnName, Function<Functions, Object> value) {
		if (this.select != null) {
			throw new RuntimeException("Cannot use addValue if fromSelect is used");
		}
		this.values.put(columnName, Escape.escape(value.apply(instance)));
		return this;
	}

	@Override
	public InsertBuilder fromSelect(PlainSelect<?> select) {
		if (!this.values.isEmpty()) {
			throw new RuntimeException("Cannot use fromSelect if value is set");
		}
		this.select = select;
		return this;
	}

	@Override
	public DictionaryValue execute() throws SQLException {
		String sql = createSql();
		try (PreparedStatement stat = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
