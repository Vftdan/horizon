package io.github.vftdan.horizon.scripting;

import org.mozilla.javascript.ContextFactory;

public class ScriptExecutorManager {
	private static boolean rhinoInited = false;
	private static void initRhino() {
		if(rhinoInited) return;
		ContextFactory.initGlobal(new FilteredContextFactory());
	}
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
			case RHINO: initRhino(); return new RhinoScriptExecutor();
			default: return null;
		}
	}
	public static IScriptExecutor instantiateExecutor() {
		return instantiateExecutor(curExecutor);
	}
}
