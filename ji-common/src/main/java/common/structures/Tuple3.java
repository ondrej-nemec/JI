package common.structures;

public class Tuple3<F, S, T> {

	private final F _1;
	
	private final S _2;
	
	private final T _3;
	
	public Tuple3(F _1, S _2, T _3) {
		this._1 = _1;
		this._2 = _2;
		this._3 = _3;
	}

	public F _1() {
		return _1;
	}

	public S _2() {
		return _2;
	}
	
	public T _3() {
		return _3;
	}

	@Override
	public String toString() {
		return String.format("Tuple(%s,%s. %s)", _1, _2, _3);
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof Tuple3<?, ?, ?>)) {
			return false;
		}
		Tuple3<?, ?, ?> t = Tuple3.class.cast(obj);
		if (!_1.equals(t._1)) {
			return false;
		}
		if (!_2.equals(t._2)) {
			return false;
		}
		if (!_3.equals(t._3)) {
			return false;
		}
		return true;
	}
	
}
