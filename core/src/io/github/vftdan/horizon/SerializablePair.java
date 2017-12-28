package io.github.vftdan.horizon;

import java.io.Serializable;

public class SerializablePair<T1 extends Serializable, T2 extends Serializable> extends Pair<T1, T2> implements Serializable {

	private static final long serialVersionUID = 1L;
	public SerializablePair(T1 v1, T2 v2) {
		super(v1, v2);
		sval1 = v1;
		sval2 = v2;
	}
	public SerializablePair() {
		
	}
	protected T1 sval1;
	protected T2 sval2;
	public T1 getFirst() {
		return sval1;
	}
	public T2 getSecond() {
		return sval2;
	}
	public String toString() {
		val1 = sval1;
		val2 = sval2;
		return super.toString();
	}
}
