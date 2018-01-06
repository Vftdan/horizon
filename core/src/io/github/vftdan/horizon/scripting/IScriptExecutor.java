package io.github.vftdan.horizon.scripting;

import java.util.Set;

public interface IScriptExecutor {

	public void put(String v, Object o);
	
	public IJsObject get(String v);
	
	public IJsObject eval(String c) throws Exception;
	
	public IJsObject toJsArray(Iterable<?> a) throws Exception;
	
	public IJsObject toJsArray(Object[] a) throws Exception;
	
	public String getJavaPackageImportCode(String pkg);
	
	public IJsObject instantiateJsObject(Object o);

	public IJsObject instantiateJsObject();
	
	public boolean isJsException(Exception e);
	
	public interface IJsObject {
		
		public Object getObject();
		
		public Object get(String key);
		
		public void put(String key, IJsObject o);
		
		public void put(String key, Object o);
		
		public Set<String> keySet();
		
		public boolean isFunction() throws Exception;
		
		public IJsObject execute(IJsObject args) throws Exception;
		
		public IJsObject execute() throws Exception;
		
		public IJsObject execute(Object... objects) throws Exception;
		
		public boolean isMap();
		
		public boolean isConstructor() throws Exception;
		
		public boolean isArray() throws Exception;
		
		public String getType() throws Exception;
		
		public IJsObject execMethod(String m) throws Exception;
		
		public String toString();
		
		public Object to(Class<?> c) throws InstantiationException, IllegalAccessException;
		
	}
	
}
