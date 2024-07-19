package ji.querybuilder.structures;

import ji.querybuilder.Builder;

public class StringWrapper implements SubSelect {

	private final String string;
	
	public StringWrapper(String string) {
		this.string = string;
	}

	/*@Override
	public String getSql() {
		return string;
	}

	@Override
	public String createSql() {
		return string;
	}*/
}
