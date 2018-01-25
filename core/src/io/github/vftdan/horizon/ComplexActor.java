package io.github.vftdan.horizon;
import static io.github.vftdan.horizon.Utils.*;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Vftdan on 21.01.2018.
 */
public class ComplexActor extends Actor {
	public boolean visible = true;
    public static Map<Class<? extends ComplexActor>, Map<String, NinePatch>> defaultTextures = new HashMap<Class<? extends ComplexActor>, Map<String, NinePatch>>();
    Map<String, NinePatch> patches = new HashMap(ifNull(defaultTextures.get(this.getClass()), new HashMap<String, NinePatch>()));
    ActorElement[] elements;
    public void putTexture(String s, NinePatch np) {
        patches.put(s, np);
    }
    public void putTexture(String s, TextureRegion t) {
        this.putTexture(s, new NinePatch(t));
    }
    public void putTexture(String s, Texture t) {
        this.putTexture(s, new TextureRegion(t));
    }
    public NinePatch getTexture(String s) {
        return patches.get(s);
    }
    public void drawElement(Batch b, ActorElement e) {
        if(!e.visible) return;
        NinePatch np = patches.get(e.npName);
        //System.out.println(e);
        if(np != null) {
            float x = getX(), y = getY(), ox = getOriginX(), oy = getOriginY(), w = getWidth(), h = getHeight(), sx = getScaleX(), sy = getScaleY(), r = getRotation();
            x += w * e.relX;
            y += h * e.relY;
            w *= e.relW;
            h *= e.relH;
            sx *= e.sx;
            sy *= e.sy;
            np.draw(b, x, y, ox, oy, w, h, sx, sy, r);
        } else {
            System.out.println(e.npName + " is null!");
        }
    }
    public void draw(Batch batch, float alpha) {
        //logged(trs.size());
    	if(!visible) return;
        for(ActorElement e: elements) {
            drawElement(batch, e);
        }
    }

    public class ActorElement {
        public void setNpName(String trName) {
            this.npName = trName;
        }
        String npName;
        float sx = 1, sy = 1, relX = 0, relY = 0, relW = 1, relH = 1;
        boolean visible = true;
        public String toString() {
            return String.format("%s %s x=%s y=%s w=%s h=%s sx=%s sy=%s", super.toString(), npName, relX, relY, relW, relH, sx, sy);
        }
    }

}
