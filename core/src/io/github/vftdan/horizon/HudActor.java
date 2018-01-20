package io.github.vftdan.horizon;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HudActor extends SimpleActor {
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
