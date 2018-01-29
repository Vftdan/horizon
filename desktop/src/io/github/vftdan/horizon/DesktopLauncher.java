package io.github.vftdan.horizon;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import io.github.vftdan.horizon.GAME;
import io.github.vftdan.horizon.scripting.ScriptExecutorManager;
import io.github.vftdan.horizon.scripting.ScriptExecutorManager.ExecutorClasses;

public class DesktopLauncher {
	public static void handleCrash(RuntimeException e) {
		try {
			File f = new File("horizon_crash.log");
			FileWriter fw = new FileWriter(f, true);
			fw.write(new Date().toString() + "\n");
			fw.write(e.getMessage() + "\n");
			fw.flush();
			fw.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public static void main (String[] arg) {
		try {
		// TODO remove
			ScriptExecutorManager.setExecutorClass(ExecutorClasses.RHINO);
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			new LwjglApplication(new GAME(), config);
		} catch(RuntimeException e) {
			handleCrash(e);
		}
	}
}
