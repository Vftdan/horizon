package io.github.vftdan.horizon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;

import com.badlogic.gdx.files.FileHandle;

public class Language {
	public static boolean useWeakRef = true;
	public static Language current;
	private static Set<Reference<ILanguageChangeable>> changeables = new HashSet<Reference<ILanguageChangeable>>();
	public HashMap<String, String> fileData;
	public static final Pattern fileLnRe = Pattern.compile("^([\\w\\.]+)\\=(.*)$");
	protected static <T> Reference<T> ref(T o) {
		if(useWeakRef) return new WeakReference<T>(o);
		return new StrongReference<T>(o);
	}
	public static HashMap<String, Language> languages = new HashMap<String, Language>();
	@SuppressWarnings("unchecked")
	public Language(Language defa) {
		if(defa != null) fileData = (HashMap<String, String>)defa.fileData.clone();
		else fileData = new HashMap<String, String>();
	}
	public Language() {
		this(null);
	}
	public void loadFile(FileHandle file) {
		InputStreamReader isr = new InputStreamReader(file.read(), Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		String ln;
		try {
			for(;;) {
				ln = br.readLine();
				if(ln == null) break;
				parseFileLine(ln);
			}
			br.close();
			isr.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		if(fileData.containsKey("language.code")) languages.put(fileData.get("language.code"), this);
	}
	public void parseFileLine(String ln, HashMap<String, String> hm) {
		Matcher m = fileLnRe.matcher(ln);
		if(!m.find()) return;
		hm.put(m.group(1), m.group(2));
	}
	public void parseFileLine(String ln) {
		parseFileLine(ln, fileData);
	}
	public String get(String k, Object... A) {
		if(k == null || k.length() == 0) k = "null";
		if(k.charAt(0) == '#') return k.substring(1);
		if(fileData.containsKey(k))
			return String.format(fileData.get(k), A);
		else {
			String[] s = new String[A.length];
			for(int i = 0; i < A.length; i++) {
				s[i] = A[i].toString();
			}
			return k + StringProcessor.join(", ", s);
		}
	}
	
	public static void addChangeables(ILanguageChangeable... A) {
		for(ILanguageChangeable c: A) {
			changeables.add(ref(c));
		}
	}
	
	public static void languageChange(Language lang) {
		for(Reference<ILanguageChangeable> cref: changeables) {
			ILanguageChangeable c = cref.get();
			if(c == null) {
				//System.out.println("Object was collected by gc");
				continue;
			}
			try {
				c.chLanguage(lang);
			} catch(RuntimeException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void languageChange() {
		languageChange(current);
	}
	
	public static interface ILanguageChangeable {
		public void chLanguage(Language lang);
	}
}
