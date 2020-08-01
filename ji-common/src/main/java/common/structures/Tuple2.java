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
	
}
