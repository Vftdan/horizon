package io.github.vftdan.horizon;

//import nashorn.*;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import io.github.vftdan.horizon.scripting.ScriptExecutorManager;
import io.github.vftdan.horizon.scripting.ScriptExecutorManager.ExecutorClasses;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	static PrintStream olderr;
	static String errPath = "./";
	public static void handleCrash(Exception e) {
		handleCrash(e.getMessage());
	}

	public static void handleCrash(String e) {
		try {
			File f = new File(errPath);
			f.mkdirs();
			f = new File(errPath + "/horizon_crash.log");
			FileWriter fw = new FileWriter(f, true);
			fw.write(new Date().toString() + "\n");
			fw.write(e + "\n");
			fw.flush();
			fw.close();
			System.out.println(f.getAbsolutePath());
		} catch(Exception ex) {
			ex.printStackTrace(olderr);
		}
	}
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		try {
			ScriptExecutorManager.setExecutorClass(ExecutorClasses.RHINO);
			if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
				GameSaver.allowExternal = false;
				this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
			}
			super.onCreate(savedInstanceState);
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			//GameSaver.externalPath = Environment.getExternalStorageDirectory().getAbsoluteFile().getAbsolutePath() + "/";
			Language.useWeakRef = false;
			olderr = System.err;
			System.setErr(new PrintStream(olderr){
				public void print(Object o) {
					if(o instanceof Exception) handleCrash((Exception)o);
					else handleCrash(o + "");
					super.print(o);
				}
				public void print(String s) {
					handleCrash(s);
					super.print(s);
				}
			});
			initialize(new GAME(), config);
		} catch(RuntimeException e) {
			handleCrash(e);
		}
	}


	@Override
	public void onRequestPermissionsResult(int prc, String[] perms, int[] gr) {
		if(prc == 200) GameSaver.allowExternal = true;
		GameSaver.regen();
	}
}
