package common.structures;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MapInit<K, V> {
	
	private final Map<K, V> map;
	
	public MapInit() {
		this.map = new HashMap<>();
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
	
	/**********************/
	
	public static <K, V> Tuple2<K, V> t(K key, V value) {
		return new Tuple2<K, V>(key, value);
	}

	@SuppressWarnings("unchecked")
	private Map<K, V> map(Map<K, V>map, Tuple2<K, V>... kvs) {
		for (Tuple2<K, V> tuple : kvs) {
			map.put(tuple._1(), tuple._2());
		}
		return map;
	}

	@SafeVarargs
	public static <K, V> UniqueMap<K, V> uniqueMap(Tuple2<K, V>... kvs) {
		UniqueMap<K, V> map = new UniqueMap<>();
		for (Tuple2<K, V> kv : kvs) {
			map.put(kv._1(), kv._2());
		}
		return map;
	}
	
	@SafeVarargs
	public static <K, V> Map<K, V> hashMap(Tuple2<K, V>... kvs) {
		MapInit<K, V> init = new MapInit<>();
		return init.map(new HashMap<>(), kvs);
	}

	@SafeVarargs
	public static Properties properties(Tuple2<?, ?>... kvs) {
		Properties prop = new Properties();
		for (Tuple2<?, ?> tuple : kvs) {
			prop.put(tuple._1(), tuple._2());
		}
		return prop;
	}
	
}
