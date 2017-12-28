package io.github.vftdan.horizon.scripting;

import javax.script.Bindings;




public interface IScriptExecutor {

	public void put(String v, Object o);
	
	public IJsObject get(String v);
	
	public IJsObject eval(String c) throws Exception;
	
	public IJsObject toJsArray(Iterable<?> a) throws Exception;
	
	public IJsObject toJsArray(Object[] a) throws Exception;
	
	public String getJavaPackageImportCode(String pkg);
	
	public interface IJsObject {
		
		public Object getObject();
		
		public boolean isFunction() throws Exception;
		
		public IJsObject execute(Bindings args) throws Exception;
		
		public IJsObject execute(IJsObject args) throws Exception;
		
		public IJsObject execute() throws Exception;
		
		public void execute(Object... objects) throws Exception;
		
		public boolean isBindings();
		
		public boolean isConstructor() throws Exception;
		
		public boolean isArray() throws Exception;
		
		public String getType() throws Exception;
		
		public IJsObject execMethod(String m) throws Exception;
		
		public String toString();
		
	}
	
}
