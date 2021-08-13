package query.wrappers;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import query.buildersparent.Builder;
import query.buildersparent.QueryBuilderParent;
import query.executors.BatchExecute;

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

}
