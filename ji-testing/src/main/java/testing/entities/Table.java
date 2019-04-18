package testing.entities;

import java.util.List;

public class Table {

	private final String name;
	
	private final List<Row> rows;
	
	public Table(final String name, final List<Row> rows) {
		this.name = name;
		this.rows = rows;
	}

	public String getName() {
		return name;
	}

	public List<Row> getRows() {
		return rows;
	}	
	
}
