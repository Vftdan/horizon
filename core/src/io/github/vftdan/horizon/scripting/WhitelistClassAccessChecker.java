package io.github.vftdan.horizon.scripting;

import static io.github.vftdan.horizon.Utils.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WhitelistClassAccessChecker implements IClassAccessChecker, Iterable<String> {

	private Set<String> allowed = new HashSet<String>();
	

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
	
	@Override
	public Iterator<String> iterator() {
		return allowed.iterator();
	}
	
	public boolean isAllowed(String s) {
		return logged(allowed.contains(logged(s)));
	}
	
	@Override
	public boolean isAllowedPrefix(String s) {
		for(String ac: this) {
			if(ac.equals(s)) return true;
			if(ac.length() < s.length() && s.startsWith(ac) && isOneOf(s.charAt(ac.length()), '.', '$')) return true;
		}
		return false;
	}
}
