package ji.common.structures;

public interface ThrowingBiFunction<T, R, S, E extends Throwable> {

	S apply(T first, R second) throws E;
	
}
