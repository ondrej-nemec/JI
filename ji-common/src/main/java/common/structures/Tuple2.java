package common.structures;

public class Tuple2<F, S> {

	private final F _1;
	
	private final S _2;
	
	public Tuple2(F _1, S _2) {
		this._1 = _1;
		this._2 = _2;
	}

	public F _1() {
		return _1;
	}

	public S _2() {
		return _2;
	}
	
	@Override
	public String toString() {
		return String.format("Tuple(%s,%s)", _1, _2);
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof Tuple2<?, ?>)) {
			return false;
		}
		Tuple2<?, ?> t = Tuple2.class.cast(obj);
		if (!_1.equals(t._1)) {
			return false;
		}
		if (!_2.equals(t._2)) {
			return false;
		}
		return true;
	}
	
}
