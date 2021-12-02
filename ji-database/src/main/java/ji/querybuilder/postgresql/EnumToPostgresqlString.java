package ji.querybuilder.postgresql;

import ji.querybuilder.enums.ColumnSetting;
import ji.querybuilder.enums.ColumnType;
import ji.querybuilder.enums.Join;
import ji.querybuilder.enums.OnAction;

public class EnumToPostgresqlString {
	
	public static String onActionToString(OnAction action) {
		switch (action) {
    		case RESTRICT: return "RESTRICT";
    		case CASCADE: return "CASCADE";
    		case SET_NULL: return "SET NULL";
    		case NO_ACTION: return "NO ACTION";
    		case SET_DEFAULT: return "SET DEFAULT";
			default: throw new RuntimeException("Not implemented action: " + action);
		}
	}
	
	public static String joinToString(final Join join) {
		switch(join) {
			case FULL_OUTER_JOIN: throw new RuntimeException("Full Outer Join is not supported by mysql");
			case INNER_JOIN: return "JOIN";
			case LEFT_OUTER_JOIN: return "LEFT JOIN";
			case RIGHT_OUTER_JOIN: return "RIGHT JOIN";
			default: throw new RuntimeException("Not implemented join: " + join);
		}
	}
	
	public static String defaultValueToString(Object defaultValue) {
		if (defaultValue == null) {
			return "";
		}
		String value = (defaultValue instanceof String) ? String.format("'%s'", defaultValue) : defaultValue.toString();
		return " DEFAULT " + value;
	}

	public static String settingToString(ColumnSetting setting, String column, StringBuilder append) {
		switch (setting) {
    		case AUTO_INCREMENT: return " SERIAL";
    		case UNIQUE: return " UNIQUE";
    		case NOT_NULL: return " NOT NULL";
    		case NULL: return " NULL";
    		case PRIMARY_KEY: append.append(String.format(", PRIMARY KEY (%s)", column)); return "";
    		default: return "";
		}
	}

	public static String typeToString(ColumnType type) {
		switch (type.getType()) {
    		case STRING:
    			return String.format("VARCHAR(%s)", type.getSize());
    		case CHAR:
    			return String.format("CHAR(%s)", type.getSize());
    		case DATETIME: return "TIMESTAMP";
    		default: return type.getType().toString();
		}
	}


}
