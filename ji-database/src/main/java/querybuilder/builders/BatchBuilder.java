package querybuilder.builders;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import querybuilder.buildersparent.Builder;
import querybuilder.buildersparent.QueryBuilderParent;
import querybuilder.executors.BatchExecute;

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
