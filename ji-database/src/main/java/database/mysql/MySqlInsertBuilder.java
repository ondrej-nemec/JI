package database.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.InsertQueryBuilder;

public class MySqlInsertBuilder extends AbstractBuilder implements InsertQueryBuilder {

	private final String query;
	
	private final List<String> columns;
	
	private final List<String> values;
	
	private int returned;
	
	public MySqlInsertBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.query = "INSERT INTO " + table;
		this.columns = new LinkedList<>();
		this.values = new LinkedList<>();
	}

	private MySqlInsertBuilder(
			final DoubleConsumer consumer,
			final String query,
			final String partQuery, 
			final List<String> columns,
			final List<String> values) {
		super(consumer);
		this.query = query + " " + partQuery;
		this.columns = columns;
		this.values = values;
	}

	@Override
	public InsertQueryBuilder addValue(String columnName, String value) {
		columns.add(columnName);
		values.add(value);
		return this;
	}

	@Override
	public int execute() throws SQLException {
		this.consumer.accept((conn)->{
			PreparedStatement stat = conn.prepareStatement(getSql());
			for (int i = 0; i < values.size(); i++) {
				stat.setString(i+1, values.get(i));
			}			
			returned = stat.executeUpdate();
		});
		return returned;
	}

	@Override
	public String getSql() {
		return query + " " + addColumns() + " " + values();
	}


	private String addColumns() {
		return getBrackedString(columns.size(), (i)->{return columns.get(i);});
	}

	private String values() {		
		return "VALUES " + getBrackedString(values.size(), (i)->{return "?";});
	}
	
	private String getBrackedString(int limit, Function<Integer, String> valueProvider) {
		String names = "(";
		for (int i = 0; i < limit; i++) {
			if (i > 0) {
				names += ", ";
			}
			names += valueProvider.apply(i);
		}
		return names + ")";
	}
	
}
