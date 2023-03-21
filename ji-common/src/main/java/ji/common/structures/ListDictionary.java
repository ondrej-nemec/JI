package ji.common.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ListDictionary<S> implements Dictionary<Integer> {
	
	private final Collection<S> collection;
	
	public static <S> ListDictionary<S> linkedList() {
		return new ListDictionary<>(new LinkedList<>());
	}
	
	public static <S> ListDictionary<S> arrayList() {
		return new ListDictionary<>(new ArrayList<>());
	}
	
	public static <S> ListDictionary<S> hashSet() {
		return new ListDictionary<>(new HashSet<>());
	}

	public ListDictionary(Collection<S> list) {
		this.collection = list;
	}
	
	@Override
	public Object getValue(Integer key) {
		if (collection instanceof List<?>) {
			return List.class.cast(collection).get(key);
		}
		throw new RuntimeException("Operation is not supported");
	}
	
	@SuppressWarnings("unchecked")
	public ListDictionary<S> add(Integer index, S value) {
		if (collection instanceof List<?>) {
			List.class.cast(collection).add(index, value);
			return this;
		}
		throw new RuntimeException("Operation is not supported");
	}
	
	public ListDictionary<S> addAll(List<S> values) {
		collection.addAll(values);
		return this;
	}
	
	public ListDictionary<S> add(S value) {
		collection.add(value);
		return this;
	}
	
	public boolean remove(Integer index) {
		return collection.remove(index);
	}
	
	public int size() {
		return collection.size();
	}
	
	@SuppressWarnings("unchecked")
	public List<S> toList() {
		if (collection instanceof List<?>) {
			return List.class.cast(collection);
		}
		return new LinkedList<>(collection);
	}
	
	@SuppressWarnings("unchecked")
	public Set<S> toSet() {
		if (collection instanceof Set<?>) {
			return Set.class.cast(collection);
		}
		return new HashSet<>(collection);
	}
	
	public void forEach2(Consumer<S> action) {
		collection.forEach(action);
	}
	
	public <E extends Throwable> void forEach(ThrowingConsumer<DictionaryValue, E> action) throws E {
		for (S s : collection) {
			action.accept(new DictionaryValue(s));
		}
	}
	
	public <E extends Throwable> void forEach(ThrowingBiConsumer<Integer, DictionaryValue, E> action) throws E {
		int index = 0;
		for (S s : collection) {
			action.accept(index++, new DictionaryValue(s));
		}
	}
	
	@Override
	public String toString() {
		return collection.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof ListDictionary) )
			return false;
		ListDictionary<?> dictionary = (ListDictionary<?>)obj;
		return collection.equals(dictionary.collection);
	}

	@Override
	public void clear() {
		collection.clear();
	}

}
