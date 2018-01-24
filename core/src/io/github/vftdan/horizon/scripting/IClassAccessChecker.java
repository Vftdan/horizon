package io.github.vftdan.horizon.scripting;

public interface IClassAccessChecker {
	public boolean isAllowed(String s);
	public boolean isAllowedPrefix(String s);
}
