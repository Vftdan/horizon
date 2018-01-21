package io.github.vftdan.horizon.gameMap.objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;

import io.github.vftdan.horizon.GAME;
import io.github.vftdan.horizon.SimpleActor;
import io.github.vftdan.horizon.events.CallbackGameObjectEvent;
import io.github.vftdan.horizon.gameMap.GameMap.*;

public class CreatureGameObject extends GameObject {
	public GraphicRepresentationVariant grv = GraphicRepresentationVariant.STAGE;
	private static TextureRegion[] defaultTextureRegions = new TextureRegion[1];
	public SimpleActor actor;
	public float animationDuration;
	public Camera boundCam;
	public boolean physics = false;
	public ArrayList<GameObject> sameCellObjects;
	//TODO healthChanged event
	public static void setDefaultTextureRegion(TextureRegion tr, int state) {
		defaultTextureRegions[state] = tr;
	};
	public static TextureRegion getDefaultTextureRegion(int state) {
		return defaultTextureRegions[state];
	}
	public static void setDefaultTextureRegion(TextureRegion tr) {
		setDefaultTextureRegion(tr, 0);
	};
	public static TextureRegion getDefaultTextureRegion() {
		return getDefaultTextureRegion(0);
	}
	public CreatureGameObject() {
		actor = new SimpleActor();
		actor.setWidth(cellWidth);
		actor.setHeight(cellHeight);
		if(getDefaultTextureRegion() != null) {
			textureRegion = getDefaultTextureRegion();
			actor.setTexture(textureRegion);
			actor.setWidth(textureRegion.getRegionWidth());
			actor.setHeight(textureRegion.getRegionHeight());
		}
	}
	public void moveToCell() {
		float x = cellX * cellWidth + offX, y = cellY * cellHeight + offY;
		actor.addAction(moveTo(x, y, animationDuration));
		if(boundCam != null) {
			boundCam.position.set(x + cellWidth / 2, y + cellHeight / 2, 0);
		}
	}
	public void moveToCell(final int x, final int y) {
		ArrayList<GameObject> a = new ArrayList<GameObject>();
		if(physics && chunk != null) {
			//System.out.println(new Vector2(x, y));
			final CreatureGameObject source = this;
			final CallbackGameObjectEvent ev = new CallbackGameObjectEvent();
			ev.callback = new Timer.Task(){public void run(){
				if(!ev.target.isOpaque()) source.moveToCell(x, y);
			}};
			ev.source = this;
			ev.target = null;
			for(GameObject o: chunk) {
				if(o.cellX == x && o.cellY == y) {
					a.add(o);
					//if(o.isOpaque()) {
						//TODO change to tryEnter event
						/*if(o instanceof InteractiveGameObject) {
							InteractiveGameObject io = (InteractiveGameObject)o;
							if(!io.interact()) return;
						} else return;*/
						ev.target = o;
						if(o.dispatchEvent("tryEnter", ev)) return;
						else if(o.isOpaque()) return;
					//}
				}
			}
		} else {
			for(GameObject o: chunk) {
				if(o.cellX == x && o.cellY == y) {
					a.add(o);
				}
			}
		}
		sameCellObjects = a;
		setCellPos(x, y);
		moveToCell();
	}
	protected float health, maxHealth;
	public float getHealth() {
		return health;
	}
	public void setHealth(float h) { this.health = h; }
	public String toString() {
		return "Creature x = " + cellX + ", y = " + cellY;
	}
}
