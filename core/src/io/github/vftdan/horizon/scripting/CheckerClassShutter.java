package io.github.vftdan.horizon.scripting;

import org.mozilla.javascript.ClassShutter;

public class CheckerClassShutter implements ClassShutter {

	private IClassAccessChecker checker;
	public CheckerClassShutter(IClassAccessChecker c) {
		checker = c;
	}

	@Override
	public boolean visibleToScripts(String name) {
		if(checker != null) return checker.isAllowedPrefix(name);
		return true;
	}

}
