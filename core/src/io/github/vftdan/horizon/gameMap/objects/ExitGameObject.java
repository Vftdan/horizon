package io.github.vftdan.horizon.gameMap.objects;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import io.github.vftdan.horizon.GAME;
import io.github.vftdan.horizon.GameSaver;
import io.github.vftdan.horizon.screens.AppScreen;
import io.github.vftdan.horizon.screens.GameScreen;

public class ExitGameObject extends AbstractTiledGameObject implements InteractiveGameObject {
	private static int defaultLayer;
	public boolean opaque = false;
	private static TextureRegion[] defaultTextureRegions = new TextureRegion[1];
	public static void setDefaultTextureRegion(TextureRegion tr, int state) {
		defaultTextureRegions[state] = tr;
	};
	public static void setDefaultTextureRegion(TextureRegion tr) {
		setDefaultTextureRegion(tr, 0);
	}
	public static TextureRegion getDefaultTextureRegion(int state) {
		return defaultTextureRegions[state];
	};
	public static TextureRegion getDefaultTextureRegion() {
		return defaultTextureRegions[0];
	};
	public static void setDefaultLayer(int l) {
		defaultLayer = l;
	};
	public static int getDefaultLayer() {
		return defaultLayer;
	};
	public ExitGameObject() {
		if(getDefaultTextureRegion() != null) {
			textureRegion = getDefaultTextureRegion();
			tile = new StaticTiledMapTile(textureRegion);
			mainCell = new Cell();
			updateTexture();
			if(cells == null) cells = new HashMap<Integer, Cell>();
			cells.put(defaultLayer, mainCell);
		}
	}
	@Override
	public boolean interact() {
		// TODO goto next level
		//AppScreen.openScreen(GAME.instance.screens.get("mainMenu"));
		((GameScreen)GAME.instance.screens.get("gameMain")).player.prepareData();
		//GameSaver.saveObject("save4.bin", GAME.instance.session);
		GAME.instance.initializer.initNext();
		//System.out.println(!GAME.instance.playing);
		if(!GAME.instance.playing) return true;
		GameScreen scr = (GameScreen)GAME.instance.screens.get("gameMain");
		AppScreen.openScreen(scr);
		scr.player.setHealth(GAME.instance.session.playerData.health + 5);
		scr.player.setScore(GAME.instance.session.playerData.score);
		scr.player.prepareData();
		GameSaver.saveGameState("currentsave.bin");
		return true;
	}
}
