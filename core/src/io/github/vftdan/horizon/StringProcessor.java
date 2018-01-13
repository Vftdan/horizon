package io.github.vftdan.horizon;

public class StringProcessor {
	private StringProcessor(){}
	
	public static String escapeStr(String s) {
		return s.replaceAll("\\\\", "\\\\").replaceAll("\\\'", "\\\'").replaceAll("\\\"", "\\\"");
	}
	
	public static String join(CharSequence delim, CharSequence... S) {
		try {
			return String.join(delim, S);
		} catch(NoSuchMethodError e) {
			String d = delim.toString();
			String res = "";
			for(int i = 0; i < S.length; i++) {
				if(i != 0) res += d;
				res += S[i];
			}
			return res;
		}
	}
}
