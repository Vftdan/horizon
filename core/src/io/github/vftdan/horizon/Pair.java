package io.github.vftdan.horizon;

public class Pair<T1, T2> {
	protected T1 val1;
	protected T2 val2;
	public Pair(T1 v1, T2 v2) {
		val1 = v1;
		val2 = v2;
	}
	public Pair() {
		
	}
	public T1 getFirst() {
		return val1;
	}
	public T2 getSecond() {
		return val2;
	}
	public String toString() {
		return String.format("Pair: [%s, %s]", val1, val2);
	}
}
