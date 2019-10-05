package common.structures;

public interface ThrowingConsumer<T, E extends Throwable> {

	void accept(T connection) throws E;
	
}
