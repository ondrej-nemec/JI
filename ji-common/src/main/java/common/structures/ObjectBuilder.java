package common.structures;

public class ObjectBuilder<T> {
	private T t;

    public ObjectBuilder() {}

    public ObjectBuilder(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
    }
    
    public void clear() {
        this.t = null;
    }

    public boolean isPresent() {
        return t != null;
    }
}
