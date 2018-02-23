package io.github.vftdan.horizon.gameMap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import io.github.vftdan.horizon.GAME;
import io.github.vftdan.horizon.SerializablePair;
import io.github.vftdan.horizon.UIStick;
import io.github.vftdan.horizon.events.CallbackGameObjectEvent;
import io.github.vftdan.horizon.gameMap.GameMap.*;
import io.github.vftdan.horizon.screens.AppScreen;
import io.github.vftdan.horizon.screens.GameScreen;
import io.github.vftdan.horizon.screens.PuzzleScreen;

import static io.github.vftdan.horizon.Utils.logged;

public class PuzzleGateGameObject extends AbstractGateGameObject implements ScreenCallerGameObject {
	
	public TextureRegion openedTextureRegion;
	//private static TextureRegion defaultOpenedTextureRegion;
	//private static TextureRegion defaultTextureRegion;
	private static int defaultLayer;
	private CallbackGameObjectEvent cb = null;
	public int screenCallbackStatus;
	private static TextureRegion[] defaultTextureRegions = new TextureRegion[2];
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
	public boolean dispatchEvent(String ename, GameObjectEvent e) {
		if(ename == "tryEnter") {
			if(!opaque) return false;
			if(e instanceof Runnable) cb = (CallbackGameObjectEvent)e;
			((GameScreen) GAME.instance.screens.get("gameMain")).screenCaller = this;
			UIStick uistick = ((PlayerGameObject)e.source).uistick;
			uistick.stopInteraction();
			AppScreen.openScreen(new PuzzleScreen());
			return true;
		}
		return false;
	}
	public static void setDefaultLayer(int l) {
		defaultLayer = l;
	};
	public static int getDefaultLayer() {
		return defaultLayer;
	};
	
	public PuzzleGateGameObject() {
		if(getDefaultTextureRegion() != null) {
			textureRegion = getDefaultTextureRegion();
			tile = new StaticTiledMapTile(textureRegion);
			mainCell = new Cell();
			updateTexture();
			if(cells == null) cells = new HashMap<Integer, Cell>();
			cells.put(defaultLayer, mainCell);
		};
		if(getDefaultTextureRegion(1) != null) openedTextureRegion = getDefaultTextureRegion(1);
	}
	
	@Override
	public boolean open() {
		chunk.map.opened.add(new SerializablePair<Integer, Integer>((Integer)cellX, (Integer)cellY));
		this.opaque = false;
		textureRegion = openedTextureRegion;
		updateTexture();
		return true;
	}

	@Override
	public void screenCallback(int status, Object data) {
		// TODO Auto-generated method stub
		screenCallbackStatus = status;
		CreatureGameObject source = ((CreatureGameObject)cb.source);
		if(status == 1) {
			((PlayerGameObject)source).addScore(100);
			open();
		} else if(status == 0) {
			//System.out.println(cb.source);
			if(cb != null && cb.source instanceof CreatureGameObject) {
				//logged(source.hashCode());
				source.setHealth(source.getHealth() - 6);
				source.dispatchEvent("healthChanged", new GameObjectEvent(this, source));
			}
		}
		if(cb != null) cb.run();
	}

	/*@Override
	public boolean interact() {
		// TODO Auto-generated method stub
		((GameScreen) GAME.instance.screens.get("gameMain")).screenCaller = this;
		AppScreen.openScreen(new PuzzleScreen());
		return true;
	}*/

}
