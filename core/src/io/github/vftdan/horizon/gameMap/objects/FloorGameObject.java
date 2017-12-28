package io.github.vftdan.horizon.gameMap.objects;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class FloorGameObject extends AbstractTiledGameObject {
	private static int defaultLayer;
	protected boolean opaque = false;
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
	public void setOpaque(boolean b) {
		opaque = b;
	}
	public boolean isOpaque() {
		return opaque;
	}
	public static void setDefaultLayer(int l) {
		defaultLayer = l;
	};
	public static int getDefaultLayer() {
		return defaultLayer;
	};
	public FloorGameObject() {
		if(getDefaultTextureRegion() != null) {
			textureRegion = getDefaultTextureRegion();
			tile = new StaticTiledMapTile(textureRegion);
			mainCell = new Cell();
			updateTexture();
			if(cells == null) cells = new HashMap<Integer, Cell>();
			cells.put(defaultLayer, mainCell);
		}
	}
}
