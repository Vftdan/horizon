package io.github.vftdan.horizon.screens;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.vftdan.horizon.GAME;

public class MenuScreen extends GameScreen {
	public Table table = new Table();
	public ArrayList<Actor> UIactors = new ArrayList<Actor>();
	public int actorWidth = 200; //(int)((int)200 * GAME.instance.uiScale);
	public int childSpacing = 30; //(int)((int)30 * GAME.instance.uiScale);
	public MenuScreen(Stage guiStage) {
		this.screenName = "Menu screen";
		this.guiStage = guiStage;
		guiStage.setViewport(new FitViewport(GAME.instance.unscaleUi(GAME.instance.screenDims.x), GAME.instance.unscaleUi(GAME.instance.screenDims.y), new OrthographicCamera(){{this.setToOrtho(false, GAME.instance.unscaleUi(GAME.instance.screenDims.x), GAME.instance.unscaleUi(GAME.instance.screenDims.y));}}));
		this.guiStage.addActor(table);
		this.inpProcArray.add(guiStage);
		table.setWidth(guiStage.getWidth());
		table.align(Align.center | Align.top);
		table.setPosition(0, 480);
	}
	public MenuScreen() {
		this(new Stage());
	}
	public void putActor(Actor a) {
		table.add(a).width(actorWidth).padTop(childSpacing);
		table.row();
		UIactors.add(a);
	}
	public void putActors(Actor... A) {
		for(Actor a: A) {
			putActor(a);
		}
	}
	public void resize(int w, int h) {
		if(table != null) table.layout();
	}
}
