package io.github.vftdan.horizon.scripting;

import static io.github.vftdan.horizon.Utils.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

@SuppressWarnings("serial")
public class FilteredNativeJavaObject extends NativeJavaObject {
	public static Set<String> fields = new HashSet<String>(){{this.add("getClass");}};
	public Map<String, Object> elems = new HashMap<String, Object>();
	public FilteredNativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType) {
		super(scope, javaObject, staticType);
	}
 
	@Override
	public Object get(String name, Scriptable start) {
		if (fields.contains(name)) {
			return ifNull(elems.get(name), NOT_FOUND);
		}
 
		return super.get(name, start);
	}
	 
	@Override
	public void put(String name, Scriptable start, Object value) {
		if (fields.contains(name)) {
			elems.put(name, value);
			return;
		}
 
		super.put(name, start, value);
	}
}
