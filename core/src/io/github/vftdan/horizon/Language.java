package io.github.vftdan.horizon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.*;

import com.badlogic.gdx.files.FileHandle;

public class Language {
	public static Language current;
	public HashMap<String, String> fileData;
	public static final Pattern fileLnRe = Pattern.compile("^([\\w\\.]+)\\=(.*)$");
	public static HashMap<String, Language> languages = new HashMap<String, Language>();
	public Language(Language defa) {
		if(defa != null) fileData = (HashMap<String, String>)defa.fileData.clone();
		else fileData = new HashMap<String, String>();
	}
	public Language() {
		this(null);
	}
	public void loadFile(FileHandle file) {
		InputStreamReader isr = new InputStreamReader(file.read());
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
		if(fileData.containsKey(k))
			return String.format(fileData.get(k), A);
		else {
			String[] s = new String[A.length];
			for(int i = 0; i < A.length; i++) {
				s[i] = A[i].toString();
			}
			return k + String.join(", ", s);
		}
	}
}
