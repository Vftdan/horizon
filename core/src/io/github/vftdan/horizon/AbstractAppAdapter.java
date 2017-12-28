package io.github.vftdan.horizon;

import java.util.ArrayList;


import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class AbstractAppAdapter extends ApplicationAdapter implements InputProcessor, GestureListener {
	ArrayList<Disposable> toDispose = new ArrayList<Disposable>();
	public Color bgc = new Color(1, 1, 1, 1);
	public Vector2 screenDims = new Vector2();
	public Music bgm;
	public Stage stage;
	public Stage guiStage;
	public OrthographicCamera cam;
	public Viewport viewport;
	//public Viewport guiViewport;
	public ShaderProgram shd;
	public Skin skin;
	public InputMultiplexer inpProcessors = new InputMultiplexer();
	@Override
	public void dispose() {
		for(Disposable d: toDispose) {
			d.dispose();
		}
	}
	public void cls() {
		Gdx.gl.glClearColor(bgc.r, bgc.g, bgc.b, bgc.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	public void addDisposable(Disposable... D) {
		if(toDispose == null) toDispose = new ArrayList<Disposable>();
		for(Disposable d: D) {
			toDispose.add(d);
		}
	}
	@Override
	public void create() {
		screenDims.x = Gdx.graphics.getWidth();
		screenDims.y = Gdx.graphics.getHeight();
	}
	@Override
	public void resize(int width, int height){
		if(viewport != null) viewport.update(width, height);
		//cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		if(screenDims != null) {
			screenDims.x = width;
			screenDims.y = height;
		}
	}
	public void startBgm() {
		bgm.play();
	}
}
