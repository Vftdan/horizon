package io.github.vftdan.horizon;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.vftdan.horizon.screens.GameScreen;

import java.util.HashMap;
import java.util.Map;

import static io.github.vftdan.horizon.Utils.*;
import static io.github.vftdan.horizon.Utils.Math.clamp;

public class HealthBarActor extends HudActor {
	//public static TextureRegion trLf, trRf, trLe, trRe;
	public float health = 1.0f;
	public HealthBarActor() {
		this.setBounds(0, GAME.instance.screenDims.y - 10, 50, 10);
		//this.setScale((float).5);
		elements = new ActorElement[] {
				new ActorElement(){{setNpName("el");}},
				new ActorElement(){{setNpName("fl");}},
				new ActorElement(){{setNpName("er");}},
				new ActorElement(){{setNpName("fr");}},
		};
		//logged(patches.size());
	}
	public void draw(Batch b, float alpha) {
		health = clamp(health, 0, 1).floatValue();
		elements[0].relW = elements[1].relW = elements[2].relX = elements[3].relX = clamp(health, .1 ,.9).floatValue();
		elements[2].relW = elements[3].relW = 1 - clamp(health, .1 ,.9).floatValue();
		if(health == 0) {
			elements[1].visible = elements[3].visible = false;
			elements[0].visible = elements[2].visible = true;
		} else if(health == 1) {
			elements[1].visible = elements[3].visible = true;
			elements[0].visible = elements[2].visible = false;
		} else {
			elements[1].visible = elements[2].visible = true;
			elements[0].visible = elements[3].visible = false;
		}
		super.draw(b, alpha);
	}

	/*@Override
	public void draw(Batch batch, float alpha) {

	}*/
	//public static TextureRegion[] defaultTextureRegions;
	/*TextureRegion textureRegion;
	boolean flippedX = false;
	boolean flippedY = false;
	public void setTexture(TextureRegion t) {
		textureRegion = t;
	}
	public void setTexture(Texture t) {
		this.setTexture(new TextureRegion(t));
	}
	public boolean flipX() {
		flippedX = !flippedX;
		return flippedX;
	}
	public void setFlipedX(boolean b) {
		flippedX = b;
	}
	public boolean flipY() {
		flippedY = !flippedY;
		return flippedY;
	}
	public void setFlipedY(boolean b) {
		flippedY = b;
	}
	@Override
	public void draw(Batch batch, float alpha) {
		if(textureRegion != null) {
			float x = getX(), y = getY(), ox = getOriginX(), oy = getOriginY(), w = getWidth(), h = getHeight(), sx = getScaleX(), sy = getScaleY(), r = getRotation();
			if(flippedX) {
				x += w * getScaleX();
				w *= -1;
			}
			if(flippedY) {
				y += h * getScaleY();
				h *= -1;
			}
			batch.draw(textureRegion, x, y, ox, oy, w, h, sx, sy, r);
		}
	}
	public void updateBounds() {
		setBounds(getX(), getY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
	}*/
}
