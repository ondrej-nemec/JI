package ji.common.structures;

public interface ThrowingBiConsumer<T, S, E extends Throwable> {

	void accept(T first, S second) throws E;
	
}
