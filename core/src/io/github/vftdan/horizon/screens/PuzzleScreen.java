package io.github.vftdan.horizon.screens;

import javax.script.ScriptException;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.vftdan.horizon.GAME;
import io.github.vftdan.horizon.GameSaver;
import io.github.vftdan.horizon.scripting.PuzzleScreenAPI;
import io.github.vftdan.horizon.scripting.ScriptExecutorManager;
import io.github.vftdan.horizon.scripting.IScriptExecutor;
import io.github.vftdan.horizon.scripting.NashornScriptExecutor;
//import io.github.vftdan.horizon.scripting.WhitelistClassFilter;

public class PuzzleScreen extends AppScreen {
	//TODO
	public IScriptExecutor executor;
	public int childSpacing = 15;
	public int elemOffsetY = 450;
	public int elemOffsetX = 30;
	public PuzzleScreen() {
		this.screenName = "Puzzle screen";
		this.guiStage = new Stage();
		this.guiStage.setViewport(new FitViewport(GAME.instance.unscaleUi(GAME.instance.screenDims.x), GAME.instance.unscaleUi(GAME.instance.screenDims.y), new OrthographicCamera(){{this.setToOrtho(false, GAME.instance.unscaleUi(GAME.instance.screenDims.x), GAME.instance.unscaleUi(GAME.instance.screenDims.y));}}));
		this.inpProcArray.add(guiStage);
		//NashornScriptExecutor.classFilter = new WhitelistClassFilter(){{add("io.github.vftdan.horizon.scripting.PuzzleScreenAPI");}};
		executor = ScriptExecutorManager.instantiateExecutor();
	}
	public void startAnim(Timer.Task callback) {
		try {
			PuzzleScreenAPI.unfix();
			PuzzleScreenAPI.setScreen(this);
			PuzzleScreenAPI.fix();
			executor.eval("var API = " + executor.getJavaPackageImportCode("io.github.vftdan.horizon.scripting.PuzzleScreenAPI") + ";");
			//executor.eval("API.putButtonGroup({'Right': function(){API.onSuccess()}, 'Wrong': function(){API.onMistake()}});");
			try {
				executor.eval(GameSaver.child("testtask.js").readString());
			} catch(Exception e) {
				e.printStackTrace();
				terminate(2, e);
			}
			//PuzzleScreenAPI.putButton("qwerty", null);
			super.startAnim(callback);
			/*Timer.schedule(new Timer.Task() { public void run() {
				GameScreen g = (GameScreen) GAME.instance.screens.get("gameMain");
				AppScreen.openScreen(g);
				if(g.screenCaller != null) {
					if(Math.random() < .5) {
						g.screenCaller.screenCallback(1, "Solved!");
					} else {
						g.screenCaller.screenCallback(0, "Incorrect!");
					}
				}
			}}, 2.5f);*/
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void terminate(int status, Object data) {
		GameScreen g = (GameScreen) GAME.instance.screens.get("gameMain");
		AppScreen.openScreen(g);
		if(g.screenCaller != null) {
			g.screenCaller.screenCallback(status, data);
		}
	}
	public void stopAnim(Timer.Task callback) {
		//guiStage.dispose();
		super.stopAnim(callback);
	}
}
