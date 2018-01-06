package io.github.vftdan.horizon.scripting;

public class ScriptExecutorManager {
	public static enum ExecutorClasses {
		NASHORN, RHINO
	}
	private static ExecutorClasses curExecutor = ExecutorClasses.NASHORN;
	public static void setExecutorClass(ExecutorClasses c) {
		curExecutor = c;
	}
	public static IScriptExecutor instantiateExecutor(ExecutorClasses c) {
		switch(c) {
			case NASHORN: return new NashornScriptExecutor();
			case RHINO: return new RhinoScriptExecutor();
			default: return null;
		}
	}
	public static IScriptExecutor instantiateExecutor() {
		return instantiateExecutor(curExecutor);
	}
}
