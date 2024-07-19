package ji.querybuilder.builder_impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ji.common.structures.Tuple2;
import ji.querybuilder.DbInstance;
import ji.querybuilder.Functions;
import ji.querybuilder.builder_impl.share.Escape;
import ji.querybuilder.builder_impl.share.ParametrizedSql;
import ji.querybuilder.builder_impl.share.SingleExecute;
import ji.querybuilder.builders.UpdateBuilder;
import ji.querybuilder.enums.Where;

public class UpdateBuilderImpl implements UpdateBuilder, SingleExecute, ParametrizedSql {

	// TODO with
	// TODO join
	private final Connection connection;
	private final DbInstance instance;
	private final String table;

	private final List<Tuple2<String, Where>> wheres;
	private final List<String> sets;
	private final Map<String, String> parameters;
	
	public UpdateBuilderImpl(Connection connection, DbInstance instance, String table) {
		this.connection = connection;
		this.instance = instance;
		this.table = table;
		this.parameters = new HashMap<>();
		this.sets = new LinkedList<>();
		this.wheres = new LinkedList<>();
	}

	public String getTable() {
		return table;
	}
	
	public List<Tuple2<String, Where>> getWheres() {
		return wheres;
	}
	
	public List<String> getSets() {
		return sets;
	}
	
	@Override
	public String getSql() {
		return instance.createSql(this);
	}

	@Override
	public String createSql() {
		return parse(getSql(), null);
	}

	@Override
	public UpdateBuilder where(Function<Functions, String> where, Where join) {
		this.wheres.add(new Tuple2<>(where.apply(instance), join));
		return this;
	}

	@Override
	public UpdateBuilder addParameter(String name, Object value) {
		parameters.put(name, Escape.escape(value));
		return this;
	}

	@Override
	public UpdateBuilder set(Function<Functions, String> update) {
		this.sets.add(update.apply(instance));
		return this;
	}

	@Override
	public int execute() throws SQLException {
		return execute(connection, createSql(), parameters);
	}

}
