package io.github.vftdan.horizon.gameMap.objects;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import io.github.vftdan.horizon.gameMap.GameMap.GameObject;
import io.github.vftdan.horizon.gameMap.GameMap.GraphicRepresentationVariant;

public abstract class AbstractTiledGameObject extends GameObject {
	public GraphicRepresentationVariant grv = GraphicRepresentationVariant.TILEDMAP;
	public TiledMapTile tile;
	public Cell mainCell;
	private static TextureRegion[] defaultTextureRegions = new TextureRegion[1];
	private static int defaultLayer;
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
	public AbstractTiledGameObject() {
		if(getDefaultTextureRegion() != null) {
			textureRegion = getDefaultTextureRegion();
			tile = new StaticTiledMapTile(textureRegion);
			mainCell = new Cell();
			updateTexture();
			if(cells == null) cells = new HashMap<Integer, Cell>();
			cells.put(defaultLayer, mainCell);
		}
	}
	public void updateTexture() {
		if(tile != null) {
			tile.setTextureRegion(textureRegion);
			if(mainCell != null) {
				mainCell.setTile(tile);
			}
		}
	}
}
