package common.structures;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

public class MapDictionary<K, V> implements Dictionary<K> {
	
	private final Map<K, V> map;
	
	public static <K, V> MapDictionary<K, V> hashMap() {
		return new MapDictionary<>(new HashMap<>());
	}
	
	public static MapDictionary<Object, Object> properties() {
		return new MapDictionary<>(new Properties());
	}

	public MapDictionary(Map<K, V> map) {
		this.map = map;
	}
	
	public MapDictionary<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}
	
	@Override
	public Object getValue(K name) {
		return map.get(name);
	}
	
	public Map<K, V> toMap() {
		return map;
	}
	
	public void forEach(BiConsumer<K, V> action) {
		map.forEach(action);
	}
	
	public <E extends Throwable> void forEachThrowable(ThrowingBiConsumer<K, V, E> action) throws E {
		for (K k : map.keySet()) {
			action.accept(k, map.get(k));
		}
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof MapDictionary) )
			return false;
		MapDictionary<?, ?> dictionary = (MapDictionary<?, ?>)obj;
		return map.equals(dictionary.map);
	}

}
