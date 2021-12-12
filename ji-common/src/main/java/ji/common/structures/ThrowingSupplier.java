package ji.common.structures;

public interface ThrowingSupplier<T, E extends Throwable> {

	T get() throws E;
	
}