package io.github.vftdan.horizon.scripting;

import org.mozilla.javascript.ContextFactory;

public class ScriptExecutorManager {
	private static boolean rhinoInited = false;
	private static void initRhino() {
		if(rhinoInited) return;
		ContextFactory.initGlobal(new FilteredContextFactory());
		rhinoInited = true;
	}
	public static enum ExecutorClasses {
		NASHORN, RHINO
	}
	private static ExecutorClasses curExecutor = ExecutorClasses.NASHORN;
	public static void setExecutorClass(ExecutorClasses c) {
		curExecutor = c;
	}
	public static IScriptExecutor instantiateExecutor(ExecutorClasses c, IClassAccessChecker ch) {
		switch(c) {
			case NASHORN: NashornScriptExecutor.classFilter = new CheckerClassFilter(ch); return new NashornScriptExecutor();
			case RHINO: initRhino(); RhinoScriptExecutor.shutter = new CheckerClassShutter(ch); return new RhinoScriptExecutor();
			default: return null;
		}
	}
	public static IScriptExecutor instantiateExecutor(ExecutorClasses c) {
		return instantiateExecutor(c, null);
	}
	public static IScriptExecutor instantiateExecutor() {
		return instantiateExecutor(curExecutor);
	}
	public static IScriptExecutor instantiateExecutor(IClassAccessChecker ch) {
		return instantiateExecutor(curExecutor, ch);
	}
}
