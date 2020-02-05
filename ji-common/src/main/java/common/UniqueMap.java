package common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class UniqueMap <T, S> {
	
	private final Map<T, S> first;
	
	private final Map<S, T> second;
	
	public UniqueMap() {
		this.first = new HashMap<>();
		this.second = new HashMap<>();
	}
	
	public void put(T a, S b) {
		first.put(a, b);
		second.put(b, a);
	}
	
	public S getA(T key) {
		return first.get(key);
	}
	
	public T getB(S key) {
		return second.get(key);
	}
	
	public void foreachA(BiConsumer<T, S> consumer) {
		first.forEach(consumer);
	}
	
	public void foreachB(BiConsumer<S, T> consumer) {
		second.forEach(consumer);
	}
	
	public Set<T> keySetA() {
		return first.keySet();
	}
	
	public Set<S> keySetB() {
		return second.keySet();
	}
	
}
