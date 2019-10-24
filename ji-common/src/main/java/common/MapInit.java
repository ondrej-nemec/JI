package common;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import common.structures.Tuple2;

/**
 * For quick initialization use:
	import static common.MapInit.*;
	
	Tuple2 factory - static t() method
 * @author Ondøej Nìmec
 *
 * @param <K>
 * @param <V>
 */
public class MapInit<K, V> {
	
	public static <K, V> Tuple2<K, V> t(K key, V value) {
		return new Tuple2<K, V>(key, value);
	}
	
	public static Tuple2<String, String> t(String key, Object value) {
		return new Tuple2<>(key, value.toString());
	}
	
	@SuppressWarnings("unchecked")
	private Map<K, V> map(Map<K, V>map, Tuple2<K, V>... kvs) {
		for (Tuple2<K, V> tuple : kvs) {
			map.put(tuple._1(), tuple._2());
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
