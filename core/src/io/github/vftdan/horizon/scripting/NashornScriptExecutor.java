package io.github.vftdan.horizon.scripting;

import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.script.*;

import com.badlogic.gdx.utils.Array;

import jdk.nashorn.api.scripting.*;

public class NashornScriptExecutor implements IScriptExecutor {
	ScriptEngine engine;
	Bindings scope;
	public static ClassFilter classFilter;
	public NashornScriptExecutor(Bindings s) {
		//engine = (new ScriptEngineManager()).getEngineByName("js");
		if(classFilter == null) {
			engine = (new NashornScriptEngineFactory()).getScriptEngine();
		} else {
			engine = (new NashornScriptEngineFactory()).getScriptEngine(classFilter);
		}
		scope = s;
	}
	public NashornScriptExecutor() {
		//engine = (new ScriptEngineManager()).getEngineByName("js");
		engine = (new NashornScriptEngineFactory()).getScriptEngine(classFilter);
		scope = engine.getBindings(ScriptContext.ENGINE_SCOPE);
	}
	public void put(String v, Object o) {
		if(o instanceof JsObject) {
			put(v, ((JsObject)o).obj);
			return;
		}
		scope.put(v, o);
	}
	public JsObject get(String v) {
		return new JsObject(this, scope.get(v));
	}
	public JsObject eval(String c) throws ScriptException {
		return new JsObject(this, engine.eval(c, scope));
	}
	public JsObject toJsArray(Iterable<?> a) throws ScriptException {
		Bindings b = engine.createBindings();
		int i = 0;
		for(Object o: a) {
			if(o instanceof JsObject) b.put(i + "", ((JsObject)o).obj);
			else b.put(i + "", o);
			i++;
		}
		b.put("length", i);
		return new JsObject(this, engine.eval("(function(){var i, a = []; for(i = 0; i < length; i++) a[i] = this[i]; print(a); return a})()", b));
	}
	public JsObject toJsArray(Object[] a) throws ScriptException {
		return toJsArray(Arrays.asList(a));
	}
	public JsObject instantiateJsObject(Object b) {
		return new JsObject(this, b);
	}
	public JsObject instantiateJsObject() {
		return new JsObject(this);
	}
	
	public boolean isJsException(Exception e) {
		return e instanceof ScriptException;
	}
	
	public static class JsObject implements IJsObject /*implements Iterable<Object>*/ {
		public NashornScriptExecutor defaultExecutor;
		Object obj;
		public Bindings scope;
		public JsObject(NashornScriptExecutor e, Object b, Bindings s) {
			defaultExecutor = e;
			obj = b;
			scope = s;
		}
		public JsObject(NashornScriptExecutor e) {
			this(e, e.engine.createBindings(), e.engine.createBindings());
		}
		public JsObject(NashornScriptExecutor e, Object b) {
			this(e, b, e.engine.createBindings());
		}
		public Object getObject() {
			return obj;
		}
		public boolean isFunction() throws ScriptException {
			return getType().equals("function");
		}
		public JsObject execute(Bindings args) throws ScriptException {
			scope.put("__obj", obj);
			scope.put("__args", args);
			//System.out.println(obj);
			//System.out.println(args);
			return new JsObject(defaultExecutor, defaultExecutor.engine.eval("__obj.apply(this, __args);/*throw JSON.stringify([__obj, __obj.apply, Object.keys(this), __args]);*/", scope));
		}
		public JsObject execute(IJsObject args) throws ScriptException {
			return execute((Bindings)args.getObject());
		}
		public JsObject execute() throws ScriptException {
			scope.put("__obj", obj);
			return new JsObject(defaultExecutor, defaultExecutor.engine.eval("__obj()", scope));
		}
		public IJsObject execute(Object... objects) throws ScriptException {
			Bindings b = defaultExecutor.engine.createBindings();
			int i = 0;
			for(Object o: objects) {
				b.put(i + "", o instanceof IJsObject ? ((IJsObject)o).getObject() : o);
				i++;
			}
			//System.out.println(i);
			b.put("length", i);
			return execute(b);
		}
		public boolean isMap() {
			return obj instanceof Map<?, ?>;
		}
		public boolean isConstructor() throws ScriptException {
			if(!isMap() || !isFunction()) return false;
			Bindings b = (Bindings)obj;
			return b.containsKey("prototype");
		}
		public boolean isArray() throws ScriptException {
			scope.put("__obj", obj);
			return (Boolean)defaultExecutor.engine.eval("Array.isArray(__obj)", scope);
		}
		public Object to(Class<?> c) throws InstantiationException, IllegalAccessException {
			if(c.isAssignableFrom(obj.getClass())) return c.cast(obj);
			if(String.class.isAssignableFrom(c)) return c.cast(toString());
			/*if(Iterable.class.isAssignableFrom(c)) {
				Method m = null;
				Class<?> t = c.getComponentType();
				try {
					m = c.getMethod("set", t);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(m != null) {
					try {
					Bindings b = (Bindings)obj;
					@SuppressWarnings("unchecked")
					Iterable<Object> r = (Iterable<Object>)c.newInstance();
					int length = (int)(long)(Long)b.get("length");
					int i;
					for(i = 0; i < length; i++) {
						m.invoke(r, b.get(i + ""));
					}
					return c.cast(r);
					} catch(InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}*/
			if(Collection.class.isAssignableFrom(c)) {
				Bindings b = (Bindings)obj;
				@SuppressWarnings("unchecked")
				Collection<Object> r = (Collection<Object>)c.newInstance();
				int length = (int)(long)(Long)b.get("length");
				int i;
				for(i = 0; i < length; i++) {
					r.add(b.get(i + ""));
				}
				return c.cast(r);
			}
			if(Array.class.isAssignableFrom(c)) {
				Bindings b = (Bindings)obj;
				@SuppressWarnings("unchecked")
				Array<Object> r = (Array<Object>)c.newInstance();
				int length = (int)(long)(Long)b.get("length");
				int i;
				for(i = 0; i < length; i++) {
					r.add(b.get(i + ""));
				}
				return c.cast(r);
			}
			if(c.isArray()) {
				Bindings b = (Bindings)obj;
				int length = (int)(long)(Long)b.get("length");
				Object[] r = new Object[length];
				int i;
				for(i = 0; i < length; i++) {
					r[i] = b.get(i + "");
				}
				Class<?> t =  c.getComponentType();
				if(t == Object.class) return r;
				return c.cast(r);
			}
			return null;
		}
		
		public String getType() throws ScriptException {
			scope.put("obj", obj);
			return (String)defaultExecutor.engine.eval("typeof obj", scope);
		}
		public JsObject execMethod(String m) throws ScriptException {
			scope.put("obj", obj);
			scope.put("met", m);
			return new JsObject(defaultExecutor, defaultExecutor.engine.eval("obj[met]()", scope));
		}
		public String toString() {
			if(obj == null) return "null";
			try {
				return (String)execMethod("toString").obj;
			} catch(ScriptException e) {
				return obj.toString();
			}
		}
		/*public Iterator<Object> iterator() {
			
			return null;
		}*/
		@Override
		public Object get(String key) {
			if(obj instanceof Map<?, ?>) {
				return ((Map<?, ?>)obj).get(key);
			}
			// TODO use reflect
			return null;
		}
		@Override
		public void put(String key, IJsObject o) {
			put(key, o.getObject());
		}
		@Override
		public void put(String key, Object o) {
			//System.out.println(obj.getClass());
			if(obj instanceof Map<?, ?>) {
				try {
					@SuppressWarnings("unchecked")
					Map<Object, Object> hm = (Map<Object, Object>)obj;
					hm.put(key, o);
					return;
				} catch(ClassCastException e) {
					//Continue function
				}
			}
			// TODO throw something
			System.out.println("Unable to put");
			throw new RuntimeException("Unable to put");
		}
		@Override
		public Set<String> keySet() {
			return ((Bindings)obj).keySet();
		}
	}

	@Override
	public String getJavaPackageImportCode(String pkg) {
		return "Java.type('" + pkg + "')";
	}
}
