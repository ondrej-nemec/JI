package ji.querybuilder.builders;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ji.querybuilder.buildersparent.Builder;
import ji.querybuilder.buildersparent.QueryBuilderParent;
import ji.querybuilder.executors.BatchExecute;

public class BatchBuilder extends QueryBuilderParent implements BatchExecute<BatchBuilder> {

	private final List<Builder> batches;
	
	public BatchBuilder(Connection connection) {
		super(connection);
		this.batches = new LinkedList<>();
	}
	
	public BatchBuilder addBatch(Builder batch) {
		batches.add(batch);
		return this;
	}

	@Override
	public BatchBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}

	@Override
	public List<Builder> _getBuilders() {
		return batches;
	}
	
	@Override
	public String getSql() {
		return prepate(batch->batch.getSql());
	}

	@Override
	public String createSql(Map<String, String> parameters) {
		return prepate(batch->batch.createSql(parameters));
	}

	private String prepate(Function<Builder, String> createSql) {
		StringBuilder query = new StringBuilder();
		batches.forEach((batch)->{
			query.append(createSql.apply(batch));
			query.append("; ");
		});
		return query.toString();
	}
	
}
