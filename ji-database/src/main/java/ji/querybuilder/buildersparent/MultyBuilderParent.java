package ji.querybuilder.buildersparent;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultyBuilderParent {
	
	protected final Connection connection;
	protected final SimpleBuilder mainBuilder;
	protected final List<SimpleBuilder> builders;
	protected final Map<String, String> params;
	
	public MultyBuilderParent(Connection connection) {
		this.connection = connection;
		this.params = new HashMap<>();
		this.builders = new LinkedList<>();
		this.mainBuilder = new SimpleBuilder();
	}

	public void addBuilder(String query) {
		builders.add(new SimpleBuilder(query));
	}

	public Connection getConnection() {
		return connection;
	}
	
	public List<Builder> _getBuilders() {
		List<Builder> b = new LinkedList<>();
		b.add(mainBuilder);
		b.addAll(builders);
		return b;
	}

	public Map<String, String> getParameters() {
		return params;
	}
	
	protected void _addNotEscaped(String name, String value) {
		params.put(name, value);
	}

	public String getSql() {
		StringBuilder query = new StringBuilder();
		_getBuilders().forEach((b)->{
			if (!b.getSql().isEmpty()) {
				query.append(b.getSql()).append(";");
			}
		});
		return query.toString();
	}

}
