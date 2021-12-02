package ji.common.structures;

import java.util.function.Function;

public class IntegerBuilder {

	private int value;
	
	public IntegerBuilder() {
		this(0);
	}
	
	public IntegerBuilder(int value) {
		this.value = value;
	}
	
	public IntegerBuilder add(int plus) {
		value += plus;
		return this;
	}
	
	public IntegerBuilder remove(int minus) {
		value -= minus;
		return this;
	}
	
	public IntegerBuilder change(Function<Integer, Integer> change) {
		value = change.apply(value);
		return this;
	}
	
	@Override
	public String toString() {
		return value + "";
	}
	
	public int getInteger() {
		return value;
	}
	
}
