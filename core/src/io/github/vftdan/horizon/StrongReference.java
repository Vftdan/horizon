package io.github.vftdan.horizon;

import java.lang.ref.*;

public class StrongReference<T> extends WeakReference<T> {

	protected T data;
	
	public StrongReference(T o) {
		super(o);
		data = o;
	}
	
	public T get() {
		return data;
	}
	
	public void clear() {
		super.clear();
		data = null;
	}

}
