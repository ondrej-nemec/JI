package querybuilder;

public interface Parameters<B> {
	
	B addParameter(String name, boolean value);

	B addParameter(String name, int value);

	B addParameter(String name, double value);

	B addParameter(String name, String value);

	default B addParameter(String name, Object value) {
		return addParameter(name, value.toString());
	}
	
	/**
	 * @return SQL string with auxiliary names for variables
	 */
	String getSql();
	
	/**
	 * @return SQL string with auxiliary names replaced with values
	 */
	String createSql();

}
