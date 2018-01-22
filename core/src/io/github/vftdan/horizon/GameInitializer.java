package io.github.vftdan.horizon;

import java.io.FileNotFoundException;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import io.github.vftdan.horizon.gameMap.GameMap;
import io.github.vftdan.horizon.gameMap.GameMap.GameObject;
import io.github.vftdan.horizon.gameMap.GameMapGenerator;
import io.github.vftdan.horizon.gameMap.objects.AbstractTiledGameObject;
import io.github.vftdan.horizon.gameMap.objects.PlayerGameObject;
import io.github.vftdan.horizon.screens.AppScreen;
import io.github.vftdan.horizon.screens.GameScreen;
import io.github.vftdan.horizon.screens.MenuScreen;

public class GameInitializer {
	public GameScreen screen;
	public GameInitializer() {
		
	}
	public boolean initNew(long seed) {
		if(GAME.instance.session == null) GAME.instance.session = new GameSession();
		GAME.instance.session.seed = seed;
		GAME.instance.session.playerData = null;
		main();
		return true;
	}
	public boolean initLoaded(String saveFile) {
		try {
			if(!(GameSaver.loadGameState(saveFile))) return false;
			main();
			screen.player.useData(GAME.instance.session.playerData);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean initNext() {
		GAME.instance.session.playerData.level++;
		main();
		return true;
	}
	private boolean main() {
		if(screen != null) {
			screen.stage.dispose();
			screen.guiStage.dispose();
			screen.tiledMap.dispose();
			screen.inpProcArray.clear();
			screen.stage = new Stage();
			screen.guiStage = new Stage();
			screen.tiledMap = new TiledMap();
		} else {
			screen = new GameScreen();
		}
		screen.player = new PlayerGameObject();
		screen.gmap = new GameMap(64, 64);
		if(GAME.instance.session.playerData != null) {
			screen.gmap.level = GAME.instance.session.playerData.level;
		} else {
			screen.gmap.level = 0;
		}
		screen.gmap.tiledMap = screen.tiledMap;
		if(screen.gmap.generator == null) screen.gmap.generator = new GameMapGenerator();
		screen.gmap.generator.initRand(GAME.instance.session, screen.gmap.level);
		
		screen.stage.addActor(screen.player.actor);
		screen.stage.setViewport(screen.viewport);
		screen.guiStage = new Stage();
		//GAME.instance.addDisposable(screen.guiStage, screen.stage, screen.tiledMap, screen.player.actor.textureRegion.getTexture());
		screen.bg_layers = new int[]{0};
		screen.tiledMapRenderer = new OrthogonalTiledMapRenderer(screen.tiledMap);
        screen.cam = new OrthographicCamera();
        screen.cam.setToOrtho(false, 64 * GameObject.cellWidth, 64 * GameObject.cellHeight);
        screen.player.boundCam = screen.cam;
        screen.gmap.generate(screen.player);
        screen.player.physics = true;
        screen.player.animationDuration = .1f;
		screen.player.healthBar = new HealthBarActor();
        screen.viewport = new FitViewport(GAME.instance.screenDims.x, GAME.instance.screenDims.y, screen.cam);
		TiledMapTileLayer layer = new TiledMapTileLayer(64 * GameObject.cellWidth, 64 * GameObject.cellHeight, GameObject.cellWidth, GameObject.cellHeight);
		for(GameObject obj: screen.gmap.mainChunk) {
			if(obj instanceof AbstractTiledGameObject) {
				layer.setCell(obj.cellX, obj.cellY, ((AbstractTiledGameObject)obj).mainCell);
			}
		}
		screen.gmap.tiledLayers.add(layer);
		TextButton pauseb = new TextButton("||", GAME.instance.skin);
		pauseb.addListener(new ClickListener(){
			public void clicked(InputEvent e, float x, float y) {
				AppScreen.openScreen(GAME.instance.screens.get("pause"));
			}
		});
		screen.guiStage.addActor(pauseb);
		screen.guiStage.addActor(screen.player.healthBar);
		screen.guiStage.setViewport(new FitViewport(GAME.instance.unscaleUi(GAME.instance.screenDims.x), GAME.instance.unscaleUi(GAME.instance.screenDims.y), new OrthographicCamera(){{this.setToOrtho(false, GAME.instance.unscaleUi(GAME.instance.screenDims.x), GAME.instance.unscaleUi(GAME.instance.screenDims.y));}}));
		screen.inpProcArray.add(screen.player);
		screen.inpProcArray.add(screen.guiStage);
		screen.inpProcArray.add(GAME.instance);
		screen.inpProcArray.add(new GestureDetector(screen.player));
		screen.inpProcArray.add(new GestureDetector(GAME.instance));
		return true;
	}
}
