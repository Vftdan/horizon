package io.github.vftdan.horizon;

public class StringProcessor {
	private StringProcessor(){}
	
	public static String escapeStr(String s) {
		return s.replaceAll("\\\\", "\\\\").replaceAll("\\\'", "\\\'").replaceAll("\\\"", "\\\"");
	}
}
