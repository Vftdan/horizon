package io.github.vftdan.horizon;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelLanguageChangeable implements Language.ILanguageChangeable {

	private Label elem;
	public String[] keys;
	
	public LabelLanguageChangeable(Label b, String... k) {
		elem = b;
		keys = k;
	}
	
	@Override
	public void chLanguage(Language lang) {
		elem.setText(getText(lang, keys));
	}

	public String getText(Language lang, String... keys) {
		switch(keys.length) {
		case 0: return "";
		case 1: return lang.get(keys[0]);
		default: break;
		}
		String k = keys[0];
		String[] A = new String[keys.length - 1];
		for(int i = 0; i < A.length; i++) {
			A[i] = lang.get(keys[i + 1]);
		}
		return lang.get(k, (Object[])A);
	}
}
