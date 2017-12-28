package io.github.vftdan.horizon.gameMap.objects;

import io.github.vftdan.horizon.events.EventEmitter;
/*import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.Actor;
*/
import io.github.vftdan.horizon.gameMap.GameMap.*;
//import io.github.vftdan.horizon.gameMap.*;

public interface IGameObject extends EventEmitter<GameObjectEventListener, GameObjectEvent> {
	/*public GraphicRepresentationVariant grv = GraphicRepresentationVariant.NONE;
	public TextureRegion textureRegion = null;
	public Actor actor = null;
	public GameMapChunk chunk = null;
	public int cellX = 0, cellY = 0;
	public float offX = 0, offY = 0;
	public Map<Integer, Cell> cells = null;
	public boolean opaque = false;
	public void setCellPos(int x, int y);*/
	public void setCellPos(int x, int y);
	public boolean isOpaque();
	public void setOpaque(boolean b);
}
