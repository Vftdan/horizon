package io.github.vftdan.horizon.gameMap.objects;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import io.github.vftdan.horizon.gameMap.GameMap.GameObjectEventListener;

public abstract class AbstractGateGameObject extends WallGameObject {
	private static int defaultLayer;
	//private static TextureRegion[] defaultTextureRegions = new TextureRegion[2];
	protected boolean opaque = true;
	/*public static void setDefaultTextureRegion(TextureRegion tr, int state) {
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
	};*/
	public boolean isOpaque() {
		return opaque;
	}
	public void setOpaque(boolean b) {
		this.opaque = b;
	}
	public static void setDefaultLayer(int l) {
		defaultLayer = l;
	};
	public static int getDefaultLayer() {
		return defaultLayer;
	};
	public AbstractGateGameObject() {
		if(getDefaultTextureRegion() != null) {
			textureRegion = getDefaultTextureRegion();
			tile = new StaticTiledMapTile(textureRegion);
			mainCell = new Cell();
			updateTexture();
			if(cells == null) cells = new HashMap<Integer, Cell>();
			cells.put(defaultLayer, mainCell);
		}
	}
	public abstract boolean open();
}
