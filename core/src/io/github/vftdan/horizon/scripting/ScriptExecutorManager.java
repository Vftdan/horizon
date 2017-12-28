package io.github.vftdan.horizon.scripting;

public class ScriptExecutorManager {
	public static enum ExecutorClasses {
		NASHORN, RHINO
	}
	private static ExecutorClasses curExecutor = ExecutorClasses.NASHORN;
	public static void setExecutorClass(ExecutorClasses c) {
		curExecutor = c;
	}
	public static IScriptExecutor instaniateExecutor(ExecutorClasses c) {
		switch(c) {
			case NASHORN: return new NashornScriptExecutor();
			case RHINO: return null;
			default: return null;
		}
	}
	public static IScriptExecutor instaniateExecutor() {
		return instaniateExecutor(curExecutor);
	}
}
