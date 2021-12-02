package ji.common.structures;

public interface ThrowingConsumer<T, E extends Throwable> {

	void accept(T param) throws E;
	
}
