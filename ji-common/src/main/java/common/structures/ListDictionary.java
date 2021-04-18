package common.structures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class ListDictionary<S> implements Dictionary<Integer> {
	
	private final List<S> list;
	
	public static <S> ListDictionary<S> linkedList() {
		return new ListDictionary<>(new LinkedList<>());
	}
	
	public static <S> ListDictionary<S> arrayList() {
		return new ListDictionary<>(new ArrayList<>());
	}

	public ListDictionary(List<S> list) {
		this.list = list;
	}
	
	@Override
	public Object getValue(Integer key) {
		return list.get(key);
	}
	
	public ListDictionary<S> add(Integer index, S value) {
		list.add(index, value);
		return this;
	}
	
	public ListDictionary<S> addAll(List<S> values) {
		list.addAll(values);
		return this;
	}
	
	public ListDictionary<S> add(S value) {
		list.add(value);
		return this;
	}
	
	public boolean remove(Integer index) {
		return list.remove(index);
	}
	
	public List<S> toList() {
		return list;
	}
	
	public void forEach(Consumer<S> action) {
		list.forEach(action);
	}
	
	public <E extends Throwable> void forEachThrowable(ThrowingConsumer<S, E> action) throws E {
		for (S s : list) {
			action.accept(s);
		}
	}
	
	@Override
	public String toString() {
		return list.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof ListDictionary) )
			return false;
		ListDictionary<?> dictionary = (ListDictionary<?>)obj;
		return list.equals(dictionary.list);
	}

}
