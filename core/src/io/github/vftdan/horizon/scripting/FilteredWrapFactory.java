package io.github.vftdan.horizon.scripting;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

public class FilteredWrapFactory extends WrapFactory {
	@Override
	public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class<?> staticType) {
		return new FilteredNativeJavaObject(scope, javaObject, staticType);
	}
}
