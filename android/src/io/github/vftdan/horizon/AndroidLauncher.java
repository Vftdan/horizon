package io.github.vftdan.horizon;

//import nashorn.*;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import io.github.vftdan.horizon.scripting.ScriptExecutorManager;
import io.github.vftdan.horizon.scripting.ScriptExecutorManager.ExecutorClasses;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		ScriptExecutorManager.setExecutorClass(ExecutorClasses.RHINO);
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
			GameSaver.allowExternal = false;
			this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
		}
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//GameSaver.externalPath = Environment.getExternalStorageDirectory().getAbsoluteFile().getAbsolutePath() + "/";
		initialize(new GAME(), config);
	}


	@Override
	public void onRequestPermissionsResult(int prc, String[] perms, int[] gr) {
		if(prc == 200) GameSaver.allowExternal = true;
		GameSaver.regen();
	}
}
