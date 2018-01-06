package io.github.vftdan.horizon.scripting;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
//import org.mozilla.javascript.NativeNumber;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

import com.badlogic.gdx.utils.Array;

//import io.github.vftdan.horizon.scripting.IScriptExecutor.IJsObject;

public class RhinoScriptExecutor implements IScriptExecutor {

	ContextFactory cf;
	Context ctx;
	Scriptable scope;
	
	public RhinoScriptExecutor() {
		cf = new ContextFactory();
		ctx = cf.enterContext();
		scope = ctx.initStandardObjects();
		ctx.setOptimizationLevel(-1);
		Context.exit();
	}
	
	@Override
	public void put(String v, Object o) {
		scope.put(v, scope, o);
	}

	@Override
	public IJsObject get(String v) {
		return new JsObject(cf, ctx, scope.get(v, scope));
	}

	@Override
	public IJsObject eval(String c) throws Exception {
		Context cx = cf.enterContext(ctx);
		Object r = cx.evaluateString(scope, c, "<cmd>", 1, null /* TODO SecurityDomain */);
		Context.exit();
		return new JsObject(cf, ctx, r);
	}

	@Override
	public IJsObject toJsArray(Iterable<?> a) throws Exception {
		return new JsObject(cf, ctx, Context.javaToJS(a, scope));
	}

	@Override
	public IJsObject toJsArray(Object[] a) throws Exception {
		return new JsObject(cf, ctx, Context.javaToJS(a, scope));
	}

	@Override
	public String getJavaPackageImportCode(String pkg) {
		return "Packages." + pkg;
	}

	@Override
	public IJsObject instantiateJsObject(Object o) {
		return new JsObject(cf, ctx, o);
	}

	@Override
	public IJsObject instantiateJsObject() {
		return new JsObject(cf, ctx);
	}

	@Override
	public boolean isJsException(Exception e) {
		// TODO Auto-generated method stub
		return true;
	}

	public static class JsObject implements IJsObject {
		private static Object[] mapUnpack(Object[] os) {
			Object[] r = new Object[os.length];
			for(int i = 0; i < r.length; i++) {
				if(os[i] instanceof IJsObject) r[i] = ((IJsObject)os[i]).getObject();
				else r[i] = os[i];
			}
			return r;
		}
		
		private static String[] mapToString(Object[] os) {
			String[] r = new String[os.length];
			for(int i = 0; i < r.length; i++) {
				r[i] = os[i] + "";
			}
			return r;
		}

		Scriptable obj;
		Context ctx;
		ContextFactory cf;
		
		public JsObject(ContextFactory cf, Context ctx, Object o) {
			if(o != null && o instanceof IJsObject) {
				o = ((IJsObject)o).getObject();
			}
			ctx = cf.enterContext(ctx);
			if(o == null || o instanceof Undefined) {
				obj = null;
			} else if(o instanceof Scriptable) {
				obj = (Scriptable)o;
			} else {
				obj = (Scriptable)Context.javaToJS(o, ctx.initStandardObjects());
			}
			this.ctx = ctx;
			this.cf = cf;
			Context.exit();
		}
		
		public JsObject(ContextFactory cf, Context ctx) {
			this(cf, ctx, new SimpleScriptableObject());
		}
		
		@Override
		public Object getObject() {
			return obj;
		}

		@Override 
		public Object get(String key) {
			return obj.get(key, obj);
		}

		@Override
		public void put(String key, IJsObject o) {
			put(key, o.getObject());
		}

		@Override
		public void put(String key, Object o) {
			if(o instanceof IJsObject) put(key, (IJsObject)o);
			obj.put(key, obj, o);
		}

		@Override
		public boolean isFunction() throws Exception {
			return obj instanceof Function;
		}

		@Override
		public IJsObject execute(IJsObject args) throws Exception {
			return execute((Object[])args.to(Object[].class));
		}

		@Override
		public IJsObject execute() throws Exception {
			return execute(new Object[]{});
		}

		@Override
		public IJsObject execute(Object... objects) throws Exception {
			Context cx = cf.enterContext(ctx);
			IJsObject r = new JsObject(cf, cx, ((Function)obj).call(cx, obj.getParentScope(), obj, mapUnpack(objects)));
			Context.exit();
			return r;
		}

		@Override
		public boolean isMap() {
			// TODO ?
			return true;
		}

		@Override
		public boolean isConstructor() throws Exception {
			return isFunction() && obj.has("prototype", obj);
		}

		@Override
		public boolean isArray() throws Exception {
			//throw new RuntimeException("RhinoScriptExecutor.JsObject.isArray() not implemented!");
			return obj instanceof NativeArray;
		}

		@Override
		public String getType() throws Exception {
			Context cx = cf.enterContext(ctx);
			Object r = cx.evaluateString(obj, "typeof this", "<cmd>", 1, null);
			Context.exit();
			return r.toString();
		}

		@Override
		public IJsObject execMethod(String m) throws Exception {
			Function f = (Function)obj.get(m, obj);
			return new JsObject(cf, ctx, f.call(ctx, obj, f, new Object[]{}));
		}
		
		public String toString() {
			if(obj == null || obj == Scriptable.NOT_FOUND || obj.equals(Scriptable.NOT_FOUND)) return "null";
			try {
				return (String)execMethod("toString").getObject();
			} catch(Exception e) {
				return obj.toString();
			}
		}

		@Override
		public Object to(Class<?> c) throws InstantiationException, IllegalAccessException {
			if(c.isAssignableFrom(obj.getClass())) return c.cast(obj);
			if(String.class.isAssignableFrom(c)) return c.cast(toString());
			if(obj instanceof NativeJavaObject) {
				Object oobj = ((NativeJavaObject)obj).unwrap();
				if(c.isAssignableFrom(oobj.getClass())) return c.cast(oobj);
			}
			if(Collection.class.isAssignableFrom(c)) {
				@SuppressWarnings("unchecked")
				Collection<Object> r = (Collection<Object>)c.newInstance();
				int length = (int)(long)(Long)obj.get("length", obj);
				int i;
				for(i = 0; i < length; i++) {
					r.add(obj.get(i, obj));
				}
				return c.cast(r);
			}
			if(Array.class.isAssignableFrom(c)) {
				@SuppressWarnings("unchecked")
				Array<Object> r = (Array<Object>)c.newInstance();
				int length = (int)(long)(Long)obj.get("length", obj);
				int i;
				for(i = 0; i < length; i++) {
					r.add(obj.get(i, obj));
				}
				return c.cast(r);
			}
			if(c.isArray()) {
				int length = (int)(long)(Long)obj.get("length", obj);
				Object[] r = new Object[length];
				int i;
				for(i = 0; i < length; i++) {
					r[i] = obj.get(i, obj);
				}
				Class<?> t =  c.getComponentType();
				if(t == Object.class) return r;
				return c.cast(r);
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Set<String> keySet() {
			return new HashSet<String>((Collection<String>)Arrays.asList(mapToString(obj.getIds())));
		}
		
	}
	
}
