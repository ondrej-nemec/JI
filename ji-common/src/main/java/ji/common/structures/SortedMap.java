package ji.common.structures;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SortedMap<K, V> implements Dictionary<K> {

	private final List<K> order;
	private final Map<K, V> data;
	
	public SortedMap() {
		this.order = new LinkedList<>();
		this.data = new HashMap<>();
	}
	
	public SortedMap<K, V> putAll(Map<K, V> source) {
		source.forEach((k, v)->{
			put(k, v);
		});
		return this;
	}
	
	public V put(K key, V value) {
		if (!data.containsKey(key)) {
			order.add(key);
		}
		return data.put(key, value);
	}
	
	public boolean isEmpty() {
		return order.isEmpty();
	}
	
	public SortedMap<K, V> append(K key, V value) {
		put(key, value);
		return this;
	}
	
	@Override
	public V getValue(K key) {
		return data.get(key);
	}

	@Override
	public void clear() {
		data.clear();
		order.clear();
	}
	
	public boolean containsKey(K key) {
		return data.containsKey(key);
	}
	
	public V remove(K key) {
		V remove = data.remove(key);
		if (order.contains(key)) {
			order.remove(key);
		}
		return remove;
	}
	
	public void forEach(BiConsumer<K, V> callback) {
		order.forEach((key)->{
			callback.accept(key, data.get(key));
		});
	}
	
	public <E extends Throwable> void forEach(ThrowingBiConsumer<K, V, E> callback, Class<E> clazz) throws E {
        for (K key : order) {
             callback.accept(key, data.get(key));
        }
    }
	
	public Map<K, V> toMap() {
		return data;
	}
	
	public List<V> toList() {
		List<V> list = new LinkedList<>();
		forEach((k, v)->{
			list.add(v);
		});
		return list;
	}

	@Override
	public String toString() {
		if (order.isEmpty()) {
			return "[]";
		}
		StringBuilder result = new StringBuilder();
		forEach((k, v)->{
			result.append(String.format(",{\"%s\": \"%s\"}", k, v));
		});
		return "[" + result.toString().substring(1) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SortedMap<?, ?> other = (SortedMap<?, ?>) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		if (order == null) {
			if (other.order != null) {
				return false;
			}
		} else if (!order.equals(other.order)) {
			return false;
		}
		return true;
	}
	
}
