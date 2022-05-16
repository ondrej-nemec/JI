package ji.common.structures;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MapInit<K, V> {
	
	public static MapInit<String, Object> create() {
		return new MapInit<String, Object>();
	}
	
	private final Map<K, V> map;
	
	public MapInit() {
		this(new HashMap<>());
	}
	
	public MapInit(K k, V v) {
		this.map = new HashMap<>();
		map.put(k, v);
	}
	
	public MapInit(Map<K, V> map) {
		this.map = map;
	}
	
	public MapInit<K, V> append(K key, V value) {
		map.put(key, value);
		return this;
	}
	
	public Map<K, V> toMap() {
		return map;
	}
	
	public Properties toProperties() {
		Properties prop = new Properties();
		prop.putAll(map);
		return prop;
	}
	
	public MapDictionary<K, V> toDictionaryMap() {
		return new MapDictionary<>(map);
	}
}
