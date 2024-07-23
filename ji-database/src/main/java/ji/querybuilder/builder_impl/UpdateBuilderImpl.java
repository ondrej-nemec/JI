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
import ji.querybuilder.Escape;
import ji.querybuilder.Functions;
import ji.querybuilder.builder_impl.share.ParametrizedSql;
import ji.querybuilder.builder_impl.share.SingleExecute;
import ji.querybuilder.builders.UpdateBuilder;
import ji.querybuilder.enums.Join;
import ji.querybuilder.enums.Where;
import ji.querybuilder.structures.Joining;
import ji.querybuilder.structures.SubSelect;

public class UpdateBuilderImpl implements UpdateBuilder, SingleExecute, ParametrizedSql {

	// TODO with
	private final Connection connection;
	private final DbInstance instance;
	private final String table;
	private final String alias;
	private final List<Joining> joins;
	private final List<Tuple2<String, Where>> wheres;
	private final List<String> sets;
	private final Map<String, String> parameters;
	
	private final List<Tuple2<String, SubSelect>> withs;
	
	public UpdateBuilderImpl(Connection connection, DbInstance instance, String table, String alias) {
		this(connection, instance, table, alias, new LinkedList<>());
	}
	
	public UpdateBuilderImpl(
		Connection connection, DbInstance instance, String table, String alias,
		List<Tuple2<String, SubSelect>> withs) {
		this.connection = connection;
		this.instance = instance;
		this.table = table;
		this.alias = alias;
		this.parameters = new HashMap<>();
		this.sets = new LinkedList<>();
		this.wheres = new LinkedList<>();
		this.joins = new LinkedList<>();
		this.withs = withs;
	}
	
	public List<Tuple2<String, SubSelect>> getWiths() {
		return withs;
	}

	public String getAlias() {
		return alias;
	}
	
	public String getTable() {
		return table;
	}
	
	public List<Tuple2<String, Where>> getWheres() {
		return wheres;
	}
	
	public List<Joining> getJoins() {
		return joins;
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
	public UpdateBuilder _join(SubSelect builder, String alias, Join join, Function<Functions, String> on) {
		this.joins.add(new Joining(builder, alias, join, on.apply(instance)));
		return this;
	}

	@Override
	public UpdateBuilder set(Function<Functions, String> update) {
		this.sets.add(update.apply(instance));
		return this;
	}

	@Override
	public UpdateBuilder addParameter(String name, Object value) {
		parameters.put(name, Escape.escape(value));
		return this;
	}
	@Override
	public int execute() throws SQLException {
		return execute(connection, createSql(), parameters);
	}

}
