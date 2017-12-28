package io.github.vftdan.horizon.scripting;

import javax.script.Bindings;
import javax.script.ScriptException;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.*;

import io.github.vftdan.horizon.GAME;
import io.github.vftdan.horizon.screens.PuzzleScreen;

public final class PuzzleScreenAPI {
	private static Stage stage;
	private static PuzzleScreen screen;
	private static boolean fixed = false;
	public static void setScreen(PuzzleScreen s) {
		if(fixed) return;
		screen = s;
		stage = s.guiStage;
	}
	public static void fix() {
		fixed = true;
	}
	public static void unfix() {
		if(GAME.instance.currentScreen != screen) fixed = false;
	}
	private static void putActor(Actor t) {
		screen.elemOffsetY -= t.getHeight();
		t.moveBy(screen.elemOffsetX, screen.elemOffsetY);
		screen.elemOffsetY -= screen.childSpacing;
		stage.addActor(t);
	}
	public static void putButton(String text, final Object o) {
		TextButton tb = new TextButton(text, GAME.instance.skin);
		putActor(tb);
		tb.addListener(new ClickListener(){
			public void clicked(InputEvent e, float x, float y) {
				try {
					Bindings b = screen.executor.engine.createBindings();
					b.put("x", x);
					b.put("y", y);
					(new NashornScriptExecutor.JsObject(screen.executor, o)).execute(new Object[]{b});
				} catch (ScriptException ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	public static void putButtonGroup(Bindings dict) {
		//(new ScriptExecutor.JsObject(screen.executor, dict));
		for(String k: dict.keySet()) {
			putButton(k, dict.get(k));
		}
	}
	public static void putLabel(String text) {
		putActor(new Label(text, GAME.instance.skin));
	}
	public static void putTextField(String text, final Object o) {
		TextField tf = new TextField(text, GAME.instance.skin);
		putActor(tf);
		tf.setTextFieldListener(new TextFieldListener (){
			public void keyTyped (TextField textField, char key) {
				try {
					Bindings b = screen.executor.engine.createBindings();
					b.put("keyCode", (int)key);
					b.put("value", textField.getText());
					(new NashornScriptExecutor.JsObject(screen.executor, o)).execute(new Object[]{b});
				} catch (ScriptException ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	public static void onSuccess() {
		screen.terminate(1, "Solved!");
	}
	public static void onMistake() {
		screen.terminate(0, "Incorrect!");
	}
	public static final String version = "0.0";
}
