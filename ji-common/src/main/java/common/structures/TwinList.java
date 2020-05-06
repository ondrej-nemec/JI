package common.structures;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TwinList<K, V> {
	
	@SafeVarargs
	public static <K, V> TwinList<K, V> fromArray(Tuple2<K, V> ...tuples) {
		TwinList<K, V> list = new TwinList<>();
		list.put(tuples);
		return list;
	}
	
	private final Map<K, V> firsts;
	
	private final Map<V, K> seconds;
	
	public TwinList() {
		this.firsts = new LinkedHashMap<>();// new HashMap<>();
		this.seconds = new LinkedHashMap<>();// new HashMap<>();
	}
	
	public void put(K first, V second) {
		firsts.put(first, second);
		seconds.put(second, first);
	}
	
	public void put(Tuple2<K, V> tuple) {
		put(tuple._1(), tuple._2());
	}
	
	@SuppressWarnings("unchecked")
	public void put(Tuple2<K, V> ...tuples) {
		for (Tuple2<K, V> tuple : tuples) {
			put(tuple);
		}
	}
	
	public K getBySecond(V second) {
		return seconds.get(second);
	}
	
	public V getByFirst(K first) {
		return firsts.get(first);
	}

	public Set<K> getFirsts() {
		return firsts.keySet();
	}
	
	public Set<V> getSeconds() {
		return seconds.keySet();
	}
}
