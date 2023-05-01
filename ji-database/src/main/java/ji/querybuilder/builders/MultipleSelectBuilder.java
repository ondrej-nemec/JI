package ji.querybuilder.builders;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import ji.common.exceptions.LogicException;
import ji.common.structures.Tuple2;
import ji.querybuilder.buildersparent.Builder;
import ji.querybuilder.buildersparent.QueryBuilderParent;
import ji.querybuilder.executors.SelectExecute;

public class MultipleSelectBuilder extends QueryBuilderParent implements SelectExecute<MultipleSelectBuilder> {

	private final SelectBuilder initial;
	private List<Tuple2<SelectBuilder, String>> selects;
	private StringBuilder orderByBuilder = new StringBuilder();
	
	public MultipleSelectBuilder(Connection connection, SelectBuilder initial) {
		super(connection);
		this.initial = initial;
		this.selects = new LinkedList<>();
	}
	
	/**
	 * A union B   Set union: Combine two sets into one
	 * @param select
	 * @return this
	 */
	public MultipleSelectBuilder union(SelectBuilder select) {
		selects.add(new Tuple2<>(select, "UNION"));
		return this;
	}

	/**
	 * A intersection B   Set intersection: The members that A and B have in common
	 * @param select
	 * @return this
	 */
	public MultipleSelectBuilder intersect(SelectBuilder select) {
		selects.add(new Tuple2<>(select, "INSERSECT"));
		return this;
	}

	/**
	 * A difference B   Set difference: The members of A that are not in B
	 * @param select
	 * @return this
	 */
	public MultipleSelectBuilder except(SelectBuilder select) {
		selects.add(new Tuple2<>(select, "EXCEPT"));
		return this;
	}
	
	public MultipleSelectBuilder orderBy(String orderBy) {
		if (orderBy == null || orderBy.trim().length() == 0) {
			throw new LogicException("ORDER BY statement cannot be empty or null");
		}
		if (orderByBuilder.toString().isEmpty()) {
			orderByBuilder.append(" ORDER BY ");
		} else {
			orderByBuilder.append(", ");
		}
		orderByBuilder.append(orderBy);
		return this;
	}

	@Override
	public MultipleSelectBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}
	
	@Override
	public String getSql() {
		return prepare(b->b.getSql());
	}
	
	@Override
	public String createSql() {
		return prepare(b->b.createSql(getParameters()));
	}
	
	private String prepare(Function<Builder, String> toString) {
		StringBuilder query = new StringBuilder();
		query.append(toString.apply(initial));
		selects.forEach((select)->{
			query.append(" " + select._2() + " " + toString.apply(select._1()));
		});
		query.append(orderByBuilder.toString());
		return query.toString();
	}

}
