package io.github.vftdan.horizon.screens;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;

import io.github.vftdan.horizon.gameMap.*;
import io.github.vftdan.horizon.gameMap.objects.PlayerGameObject;
import io.github.vftdan.horizon.gameMap.objects.ScreenCallerGameObject;

public class GameScreen extends AppScreen {
	public GameMap gmap;
	public static int defaultWidth = 64, defaultHeight = 64;
	public ScreenCallerGameObject screenCaller;
	public PlayerGameObject player;
	public GameScreen(TiledMap tmap, Stage stage, int w, int h) {
		this.screenName = "Game screen";
		if(gmap == null) gmap = new GameMap(w, h);
		gmap.tiledMap = this.tiledMap = tmap;
		gmap.stage = this.stage = stage;
	}
	public GameScreen(TiledMap tmap, Stage stage) {
		this(tmap, stage, defaultWidth, defaultHeight);
	}
	public GameScreen() {
		this(new TiledMap(), new Stage());
	}
	public GameScreen(int w, int h) {
		this(new TiledMap(), new Stage(), w, h);
	}
	public GameScreen(GameMap gmap) {
		if(gmap.tiledMap == null) gmap.tiledMap = new TiledMap();
		if(gmap.stage == null) gmap.stage = new Stage();
		this.stage = gmap.stage;
		this.tiledMap = gmap.tiledMap;
		this.gmap = gmap;
	}
}
