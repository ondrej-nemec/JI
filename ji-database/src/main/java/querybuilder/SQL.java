package querybuilder;

public class SQL {

	public static String escape(String sql) {
		// TODO  * @ - _ + . /
		return sql.replaceAll("\\'", "''");
	}
	
}
