package querybuilder.derby;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import query.buildersparent.Builder;
import query.buildersparent.QueryBuilderParent;
import query.wrappers.CreateTableBuilder;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.OnAction;

public class DerbyCreateTableBuilder extends QueryBuilderParent implements CreateTableBuilder {

	private final StringBuilder append;
	private boolean first = true;
	
	public DerbyCreateTableBuilder(Connection connection, String table) {
		super(connection);
		query.append("CREATE TABLE " + table + " (");
		this.append = new StringBuilder();
	}
	
	@Override
	public CreateTableBuilder addColumn(String name, ColumnType type, ColumnSetting... settings) {
		addColumn(name, type, null, settings);
		return this;
	}

	@Override
	public CreateTableBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings) {
		if (!first) {
			query.append(",");
		}
		first = false;
		query.append(" ").append(name).append(" ");
		query.append(EnumToDerbyString.typeToString(type));
		query.append(EnumToDerbyString.defaultValueToString(defaultValue));
		for (ColumnSetting setting : settings) {
			query.append(EnumToDerbyString.settingToString(setting, name, append));
		}
		
		return this;
	}

	@Override
	public CreateTableBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		query.append(String.format(", FOREIGN KEY (%s) REFERENCES %s(%s)", column, referedTable, referedColumn));
		return this;
	}

	@Override
	public CreateTableBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate) {
		query.append(String.format(
				", CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE %s ON UPDATE %s",
				column, column,
				referedTable,
				referedColumn,
				EnumToDerbyString.onActionToString(onDelete),
				EnumToDerbyString.onActionToString(onUpdate)
		));
		return this;
	}
	
	@Override
	public String getSql() {
		return new StringBuilder().append(query.toString()).append(append.toString()).append(")").toString();
	}

	@Override
	public List<Builder> _getBuilders() {
		return Arrays.asList(this);
	}

	@Override
	public CreateTableBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}

}
