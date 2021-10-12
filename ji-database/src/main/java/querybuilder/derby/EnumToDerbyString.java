package querybuilder.derby;

import querybuilder.enums.ColumnSetting;
import querybuilder.enums.ColumnType;
import querybuilder.enums.Join;
import querybuilder.enums.OnAction;

public class EnumToDerbyString {
	
	public static String onActionToString(OnAction action) {
		switch (action) {
    		case RESTRICT: return "RESTRICT";
    		case CASCADE: return "CASCADE"; // not supported on update
    		case SET_NULL: return "SET NULL";
    		case NO_ACTION: return "NO ACTION";
    		case SET_DEFAULT: return "SET DEFAULT";
			default: throw new RuntimeException("Not implemented action: " + action);
		}
	}
	
	public static String joinToString(final Join join) {
		switch(join) {
			case FULL_OUTER_JOIN: throw new RuntimeException("Full Outer Join is not supported");
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
    		case AUTO_INCREMENT: return " GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)";
    		case UNIQUE: return " UNIQUE";
    		case NOT_NULL: return " NOT NULL";
    		case NULL: return "";
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
