package io.github.vftdan.horizon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.vftdan.horizon.GAME;

public class GameOverScreen extends MenuScreen {
	Label titleData = new Label("", GAME.instance.skin);
	Label textInfo = new Label("", GAME.instance.skin);
	TextButton quitButton = new TextButton(GAME.instance.curLang.get("gameover.tomain"), GAME.instance.skin);
	
	public GameOverScreen() {
		titleData.setFontScale(titleData.getFontScaleX() * 2);
		putActors(titleData, textInfo, quitButton);
		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				openScreen(GAME.instance.screens.get("mainMenu"));
			}
		});
	}
	
	public GameOverScreen(boolean won) {
		this();
		if(won) titleData.setText(GAME.instance.curLang.get("gameover.won"));
		else titleData.setText(GAME.instance.curLang.get("gameover.loose"));
	}
}
