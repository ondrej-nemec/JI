package ji.common.structures;

public interface ThrowingFunction<T, S, E extends Throwable> {

	S apply(T parameter) throws E;
	
}
