package io.github.vftdan.horizon.scripting;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jdk.nashorn.api.scripting.ClassFilter;

class WhitelistClassFilter implements ClassFilter {
	
	private Set<String> allowed = new HashSet<String>();
	
	@Override
	public boolean exposeToScripts(String arg0) {
		return allowed.contains(arg0);
	}
	
	public void addPath(String pkg) {
		String[] path = pkg.split("\\.");
		while(true) {
			allowed.add(String.join(".", path));
			if(path.length < 2) break;
			path = Arrays.copyOfRange(path, 0, path.length - 2);
		}
	}
	public void add(String pkg) {
		allowed.add(pkg);
	}
}
