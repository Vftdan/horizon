package io.github.vftdan.horizon.gameMap.objects;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

import io.github.vftdan.horizon.GAME;
import io.github.vftdan.horizon.GameSession;
import io.github.vftdan.horizon.PlayerData;
import io.github.vftdan.horizon.SerializablePair;
import io.github.vftdan.horizon.gameMap.GameMap.GameObject;

import java.util.ArrayList;
import java.util.Date;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerGameObject extends CreatureGameObject implements InputProcessor, GestureListener {
	protected PlayerData data;
	public long score = 0;
	private long lastTimeStamp = 0;
	private static TextureRegion[] defaultTextureRegions = new TextureRegion[1];
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
	@SuppressWarnings("unchecked")
	public void prepareData() {
		if(data == null) {
			if(GAME.instance.session == null) GAME.instance.session = new GameSession();
			if(GAME.instance.session.playerData == null) GAME.instance.session.playerData = new PlayerData();
			data = GAME.instance.session.playerData;
		}
		data.health = health;
		data.score = score;
		data.position = new SerializablePair<Integer, Integer>(cellX, cellY);
		data.opened = chunk.map.opened.toArray((SerializablePair<Integer, Integer>[])new SerializablePair[0]);
	}
	public PlayerGameObject() {
		super();
		if(getDefaultTextureRegion() != null) {
			textureRegion = getDefaultTextureRegion();
			actor.setTexture(textureRegion);
			actor.setWidth(textureRegion.getRegionWidth());
			actor.setHeight(textureRegion.getRegionHeight());
		}
	}
	public void useData(PlayerData data) {
		this.data = data;
		health = data.health;
		score = data.score;
		boolean ph = physics;
		float ad = animationDuration;
		physics = false;
		animationDuration = 0;
		moveToCell(data.position.getFirst(), data.position.getSecond());
		physics = ph;
		animationDuration = ad;
		chunk.map.opened.clear();
		ArrayList<AbstractGateGameObject> doors = new ArrayList<AbstractGateGameObject>();
		for(GameObject o: chunk.map.mainChunk) {
			if(o != null && o instanceof AbstractGateGameObject) doors.add((AbstractGateGameObject) o);
		}
		for(SerializablePair<Integer, Integer> p: data.opened) {
			//System.out.println(p);
			for(AbstractGateGameObject d: doors) {
				if(d.cellX == p.getFirst() && d.cellY == p.getSecond()) d.open();
			}
		}
	}
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		//return false;
		long ts = (new Date()).getTime();
		if(ts < lastTimeStamp) lastTimeStamp = ts;
		if(ts - lastTimeStamp < 100) return false;
		lastTimeStamp = ts;
		switch(keycode) {
		case Keys.LEFT: moveToCell(cellX - 1, cellY); actor.setFlipedX(false); break;
		case Keys.RIGHT: moveToCell(cellX + 1, cellY); actor.setFlipedX(true); break;
		case Keys.UP: moveToCell(cellX, cellY + 1); break;
		case Keys.DOWN: moveToCell(cellX, cellY - 1); break;
		case Keys.SPACE:
			for(GameObject o: sameCellObjects) {
				if(o instanceof InteractiveGameObject) {
					if(((InteractiveGameObject)o).interact()) return true;
				}
			}
		default: return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean tap(float x, float y, int count, int button) {
		return keyDown(Keys.SPACE);
	}
	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		float angle = (new Vector2(deltaX, deltaY)).angle();
		if(angle < 45 || angle > 325) return keyDown(Keys.RIGHT);
		if(angle > 45 && angle < 135) return keyDown(Keys.DOWN);
		if(angle > 135 && angle < 225) return keyDown(Keys.LEFT);
		if(angle > 225 && angle < 325) return keyDown(Keys.UP);
		return false;
	}
	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void pinchStop() {
		// TODO Auto-generated method stub
		
	}

}
