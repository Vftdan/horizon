package io.github.vftdan.horizon.gameMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import io.github.vftdan.horizon.SerializablePair;
import io.github.vftdan.horizon.gameMap.objects.*;

public class GameMap {
	int width, height;
	public GameMap(int w, int h) {
		width = w;
		height = h;
	}
	public static class GameObjectEvent {
		public GameObject source;
		public GameObject target;
		public boolean rejected = false;
		public void reject() {
			rejected = true;
		}
		public GameObjectEvent(GameObject source, GameObject target) {
			this.source = source;
			this.target = target;
		}
		public GameObjectEvent() {
			this(null, null);
		}
	}
	public static class GameObjectEventListener {
		//InputListener inputListener = null;
		public boolean tryEnter(GameObjectEvent e) {
			return false;
		}
		public boolean onEnter(GameObjectEvent e) {
			return false;
		}
	}
	public enum GraphicRepresentationVariant {
		NONE, STAGE, TILEDMAP
	}
	public static class GameObject implements IGameObject {
		//public static GameObject prototype = new GameObject();
		public GraphicRepresentationVariant grv = GraphicRepresentationVariant.NONE;
		public TextureRegion textureRegion;
		public Actor actor;
		public GameMapChunk chunk;
		public int cellX, cellY;
		public float offX, offY;
		public Map<Integer, Cell> cells;
		public static int cellHeight = 1;
		public static int cellWidth = 1;
		public void setCellPos(int x, int y) {
			cellX = x;
			cellY = y;
		}
		protected boolean opaque = false;
		public boolean isOpaque() {
			return opaque;
		}
		public void setOpaque(boolean b) {
			this.opaque = b;
		}
		public GameObject clone() {
			GameObject go = new GameObject();
			cloneTo(go);
			return go;
		}
		public void cloneTo(GameObject target) {
			//TODO
			target.grv = this.grv;
			target.actor = this.actor;
			target.textureRegion = this.textureRegion;
			if(this.cells != null) {
				if(target.cells == null) target.cells = new HashMap<Integer, Cell>();
				for(Integer i: this.cells.keySet()) {
					target.cells.put(i, this.cells.get(i));
				}
			}
		}
		@Override
		public boolean addEventListener(GameObjectEventListener el) {
			return false;
		}
		@Override
		public boolean removeEventListener(GameObjectEventListener el) {
			return false;
		}
		@Override
		public boolean dispatchEvent(String ename, GameObjectEvent e) {
			return false;
		}
		@Override
		public Collection<GameObjectEventListener> getEventListeners() {
			return new ArrayList<GameObjectEventListener>();
		}
	}
		
		
	public TiledMap tiledMap;
	public Stage stage;
	public GameMapGenerator generator;
	public GameMapChunkUnion mainChunk;
	public MapLayers tiledLayers;
	public Set<SerializablePair<Integer, Integer>> opened = new HashSet<SerializablePair<Integer, Integer>>();
	public int level = 0;
	public void generate(PlayerGameObject p) {
		BasicTileType[][] bm = generator.genBasics(width, height);
		Vector2[] sf = generator.appendPassages(bm, width, height);
		mainChunk = new GameMapChunkUnion();
		mainChunk.map = this;
		//System.out.println(mainChunk);
		GameObject obj;
		tiledLayers = tiledMap.getLayers();
		int i, j;
		for(i = 0; i < height; i++) {
			for(j = 0; j < width; j++) {
				//System.out.println("Tile at: " + j + ", " + i + " is " + (bm[i][j].isOpaque()?"Opaque":"Permeable"));
				if(bm[i][j].isOpaque() && !bm[i][j].isSemi()) {
					obj = new WallGameObject();
					obj.setCellPos(j, i);
					obj.chunk = mainChunk;
					mainChunk.add(obj);
					if(i != 0 && (!bm[i - 1][j].isOpaque() || bm[i - 1][j].isSemi())) {
						obj.textureRegion = WallGameObject.getDefaultTextureRegion(1);
						((WallGameObject)obj).updateTexture();
					}
					//System.out.println(mainChunk.size());
				}
				if(!bm[i][j].isOpaque() && bm[i][j].isSemi()) {
					obj = new PuzzleGateGameObject();
					obj.setCellPos(j, i);
					obj.chunk = mainChunk;
					mainChunk.add(obj);
					//System.out.println(mainChunk.size());
				}
				if(!bm[i][j].isOpaque() && !bm[i][j].isSemi()) {
					obj = new FloorGameObject();
					obj.setCellPos(j, i);
					obj.chunk = mainChunk;
					mainChunk.add(obj);
				}
			}
		}
		obj = new ExitGameObject();
		obj.setCellPos((int)sf[1].x, (int)sf[1].y);
		obj.chunk = mainChunk;
		mainChunk.add(obj);
		p.chunk = mainChunk;
		p.moveToCell((int)sf[0].x, (int)sf[0].y);
		mainChunk.add(p);
	}
	public static class GameObjectPool<T extends GameObject> {
		public T prototype;
		public Array<T> pool;
		public void push(T go) {
			pool.add(go);
		}
		@SuppressWarnings("unchecked")
		public T pop() {
			if(pool.size != 0) {
				return pool.pop();
			}
			return (T)prototype.clone();
		}
		public GameObjectPool(T proto) {
			prototype = proto;
		}
	}
}
