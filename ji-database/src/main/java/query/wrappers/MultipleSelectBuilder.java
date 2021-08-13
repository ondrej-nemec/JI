package query.wrappers;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import common.structures.Tuple2;
import query.buildersparent.QueryBuilderParent;
import query.executors.SelectExecute;

public class MultipleSelectBuilder extends QueryBuilderParent implements SelectExecute<MultipleSelectBuilder> {

	private final SelectBuilder initial;
	private List<Tuple2<SelectBuilder, String>> selects;
	
	public MultipleSelectBuilder(Connection connection, SelectBuilder initial) {
		super(connection);
		this.initial = initial;
		this.selects = new LinkedList<>();
	}
	
	/**
	 * A ∪ B   Set union: Combine two sets into one
	 * @param select
	 * @return this
	 */
	public MultipleSelectBuilder union(SelectBuilder select) {
		selects.add(new Tuple2<>(select, "UNION"));
		return this;
	}

	/**
	 * A ∩ B   Set intersection: The members that A and B have in common
	 * @param select
	 * @return this
	 */
	public MultipleSelectBuilder intersect(SelectBuilder select) {
		selects.add(new Tuple2<>(select, "INSERSECT"));
		return this;
	}

	/**
	 * A − B   Set difference: The members of A that are not in B
	 * @param select
	 * @return this
	 */
	public MultipleSelectBuilder except(SelectBuilder select) {
		selects.add(new Tuple2<>(select, "EXCEPT"));
		return this;
	}
	

	@Override
	public MultipleSelectBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}
	
	@Override
	public String getSql() {
		query.append(initial.getSql());
		selects.forEach((select)->{
			query.append(" " + select._2() + " " + select._1().getSql());
		});
		return super.getSql();
	}
	
	@Override
	public String createSql() {
		StringBuilder query = new StringBuilder();
		query.append(initial.createSql(getParameters()));
		selects.forEach((select)->{
			query.append(" " + select._2() + " " + select._1().createSql(getParameters()));
		});
		return query.toString();
	}

}
