package io.github.vftdan.horizon;

import static io.github.vftdan.horizon.Utils.Math.*;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;

import io.github.vftdan.horizon.ComplexActor.ActorElement;

public class UIStick extends HudActor {
	boolean isTouchDown = false;
	boolean isKeyDown = false;
	int selfTouchIndex = 0;
	int dirx = 0, diry = 0;
	int kx = 0, ky = 0;
	int w = 100, h = 100;
	int prior = 0;
	float cx = 150, cy = 150, dx = 0, dy = 0;

	public UIStick() {
		visible = false;
		elements = new ActorElement[] {
				new ActorElement(){{setNpName("stickbg");}},
				new ActorElement(){{setNpName("stickfg");}},
		};
	}
	
	Map<Integer, Pair<Float, Float>> touches = new HashMap<Integer, Pair<Float, Float>>();
	public Pair<Integer, Integer> getState() {
		if(isKeyDown) return new Pair<Integer, Integer>(kx, ky);
		if(isTouchDown) return new Pair<Integer, Integer>(dirx, diry);
		return new Pair<Integer, Integer>(kx, ky);
	}
	public int getPrior() {
		if(isTouchDown) return prior;
		return 0;
	}
	public void onTouchDown(int x, int y, int ti) {
		touches.put(ti, new Pair<Float, Float>((float)x, (float)y));
	}
	public boolean onTouchDragged(int x, int y, int ti) {
		if(!isTouchDown) {
			Pair<Float, Float> p = touches.get(ti);
			if(p == null) return false;
			isTouchDown = true;
			selfTouchIndex = ti;
			cx = p.getFirst();
			cy = p.getSecond();
		}
		if(ti != selfTouchIndex) return false;
		dx = (x - cx) / GAME.instance.scaleUi(w);
		dy = (y - cy) / GAME.instance.scaleUi(h);
		float dabs = (float)pythag(dx, dy);
		if(dabs > 1) {
			dx /= dabs;
			dy /= dabs;
		}
		dirx = signOf(dx, Math.min(dabs, 1) / 2f);
		diry = signOf(dy, Math.min(dabs, 1) / 2f);
		prior = signOf(Math.abs(dy) - Math.abs(dx));
		if(dabs < .12) return false;
		visible = true;
		return true;
	}
	public void onTouchUp(int x, int y, int ti) {
		if(isTouchDown && ti == selfTouchIndex) {
			dx = dy = 0;
			isTouchDown = false;
			visible = false;
		}
		touches.remove(ti);
	}
	public void onKeyDown(int keyx, int keyy) {
		if(keyx != 0) kx = signOf(keyx);
		if(keyy != 0) ky = signOf(keyy);
		if(kx != 0 || ky != 0) isKeyDown = true;
	}
	public void onKeyUp(int keyx, int keyy) {
		if(signOf(keyx) == kx) kx = 0;
		if(signOf(keyy) == ky) ky = 0;
		if(kx == 0 && ky == 0) isKeyDown = false;
	}
	
	public void draw(Batch batch, float alpha) {
		this.setBounds(GAME.instance.unscaleUi(cx) - w / 2, GAME.instance.unscaleUi(cy) - h / 2, w, h);
		elements[1].relX = dx * .7f;
		elements[1].relY = dy * .7f;
		super.draw(batch, alpha);
	}
	
	public void stopInteraction() {
		if(isTouchDown) onTouchUp(0, 0, selfTouchIndex);
	}
}
