package ji.querybuilder.builders.share;

public interface Parametrized<P> {

	P addParameter(String name, Object value);
	
}
