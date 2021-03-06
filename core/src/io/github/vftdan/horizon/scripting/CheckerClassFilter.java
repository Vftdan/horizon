package io.github.vftdan.horizon.scripting;

import jdk.nashorn.api.scripting.ClassFilter;

public class CheckerClassFilter implements ClassFilter {

	private IClassAccessChecker checker;
	public CheckerClassFilter(IClassAccessChecker c) {
		checker = c;
	}
	
	@Override
	public boolean exposeToScripts(String name) {
		if(checker != null) return checker.isAllowed(name);
		return true;
	}

}
