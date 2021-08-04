package common.structures;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ListInit<S> {

	private final Collection<S> list;
	
	public ListInit(S item) {
		this(create(item));
	}
	
	private static <S> List<S> create(S item) {
		List<S> list = new LinkedList<>();
		list.add(item);
		return list;
	}
	
	public ListInit() {
		this(new LinkedList<>());
	}
	
	public ListInit(Collection<S> list) {
		this.list = list;
	}
	
	public ListInit<S> add(S item) {
		list.add(item);
		return this;
	}
	
	public List<S> toList() {
		return new LinkedList<>(list);
	}
	
	public Set<S> toSet() {
		return new HashSet<>(list);
	}
	
	public ListDictionary<S> toDictionaryList() {
		return new ListDictionary<>(toList());
	}
	
}
