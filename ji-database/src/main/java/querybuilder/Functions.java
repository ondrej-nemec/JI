package querybuilder;

public interface Functions {

	/**
	 * Param(s) must be column name or escaped value - you can use parameters
	 * @return
	 */
	String concat(String param, String ...params);
	
	/**
	 * Param(s) must be column name or escaped value - you can use parameters
	 * @return
	 */
	String cast(String param, ColumnType type);
	
}
