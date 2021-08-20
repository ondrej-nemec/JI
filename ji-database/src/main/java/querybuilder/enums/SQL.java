package querybuilder.enums;

public class SQL {

	public static String escape(String sql) {
		// TODO??  * @ - _ + . /
		return String.format("'%s'", sql.replaceAll("\\'", "''"));
	}
	
}
