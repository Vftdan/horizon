package io.github.vftdan.horizon;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import io.github.vftdan.horizon.GAME;
import io.github.vftdan.horizon.scripting.ScriptExecutorManager;
import io.github.vftdan.horizon.scripting.ScriptExecutorManager.ExecutorClasses;

public class DesktopLauncher {
	public static void main (String[] arg) {
		// TODO remove
		//ScriptExecutorManager.setExecutorClass(ExecutorClasses.RHINO);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GAME(), config);
		
	}
}
