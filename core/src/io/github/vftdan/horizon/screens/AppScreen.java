package io.github.vftdan.horizon.screens;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import static io.github.vftdan.horizon.GAME.instance;


public class AppScreen {
	public Music bgm;
	public boolean autoplay = false;
	public TiledMap tiledMap;
	public TiledMapRenderer tiledMapRenderer;
	public int[] bg_layers;
	public int[] fg_layers;
	public Stage stage;
	public Stage guiStage;
	public OrthographicCamera cam;
	public Viewport viewport;
	//public Viewport guiViewport;
	public Array<InputProcessor> inpProcArray = new Array<InputProcessor>();
	public String screenName;
	public String toString() {
		return screenName;
	}
	public void start(Timer.Task callback) {
		Music oldBgm = instance.bgm;
		if(oldBgm != bgm && oldBgm != null) oldBgm.stop();
		instance.bgm = bgm;
		if(bgm != null && autoplay) instance.startBgm();
		instance.tiledMap = tiledMap;
		instance.tiledMapRenderer = tiledMapRenderer;
		instance.bg_layers = bg_layers;
		instance.fg_layers = fg_layers;
		instance.stage = stage;
		instance.guiStage = guiStage;
		instance.cam = cam;
		instance.viewport = viewport;
		//instance.guiViewport = guiViewport;
		//if(viewport != null) viewport.update((int)instance.screenDims.x, (int)instance.screenDims.y);
		instance.currentScreen = this;
		instance.resize((int)instance.screenDims.x, (int)instance.screenDims.y);
		activateInputListeners();
		startAnim(callback);
	}
	public void start() {
		start(null);
	}
	public void activateInputListeners() {
		//System.out.println(this + " " + inpProcArray);
		//instance.inpProcessors.clear();
		instance.inpProcessors.setProcessors(inpProcArray);
	}
	public static void openScreen(final AppScreen screen, final Timer.Task callback) {
		//System.out.println("Changing screen from " + instance.currentScreen + " to " + screen);
		Timer.Task t = new Timer.Task(){
			@Override
			public void run() {
				screen.start(callback);
			}
		};
		if(instance.currentScreen != null) instance.currentScreen.stopAnim(t);
		else t.run();
	}
	public static void openScreen(AppScreen screen) {
		openScreen(screen, null);
	}
	public void startAnim(Timer.Task callback) {
		if(callback != null) Timer.schedule(callback, 0);
	}
	public void stopAnim(Timer.Task callback) {
		if(callback != null) Timer.schedule(callback, 0);
	}
	
	public void resize() {
		
	}
}
