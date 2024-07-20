package ji.querybuilder.builder_impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ji.common.structures.DictionaryValue;
import ji.common.structures.Tuple2;
import ji.querybuilder.DbInstance;
import ji.querybuilder.builder_impl.share.Escape;
import ji.querybuilder.builders.InsertBuilder;
import ji.querybuilder.builders.share.PlainSelect;
import ji.querybuilder.structures.SubSelect;

public class InsertBuilderImpl implements InsertBuilder {

	// TODO with
	private final Connection connection;
	private final DbInstance instance;
	private final String table;
	private final Map<String, String> values;
	private PlainSelect<?> select;
	
	private final List<Tuple2<String, SubSelect>> withs;
	
	public InsertBuilderImpl(Connection connection, DbInstance instance, String table) {
		this.connection = connection;
		this.instance = instance;
		this.table = table;
		this.values = new HashMap<>();
		this.withs = new LinkedList<>();
	}
	
	public List<Tuple2<String, SubSelect>> getWiths() {
		return withs;
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
	public InsertBuilder with(String name, SubSelect select) {
		this.withs.add(new Tuple2<>(name, select));
		return this;
	}

	@Override
	public String getSql() {
		return instance.createSql(this);
	}

	@Override
	public InsertBuilder addValue(String columnName, Object value) {
		if (this.select != null) {
			throw new RuntimeException("Cannot use addValue if fromSelect is used");
		}
		this.values.put(columnName, Escape.escape(value));
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
