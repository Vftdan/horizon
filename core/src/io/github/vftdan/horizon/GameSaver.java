package io.github.vftdan.horizon;

import java.io.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import io.github.vftdan.horizon.gameMap.objects.PlayerGameObject;

public final class GameSaver {
	//public static PlayerGameObject player;
	public static boolean allowExternal = true;
	private static FileHandle savepath = genSavePath();
	public static void regen() {
		FileHandle fh = genSavePath();
		if(fh != null) savepath = fh;
	}
	private static FileHandle genSavePath() {
		if(Gdx.files == null) return null;
		FileHandle spf = Gdx.files.local("savepath");
		if(spf.exists()) {
			try {
				savepath = Gdx.files.absolute(spf.readString());
				savepath.mkdirs();
			} catch (Exception e) {
				spf.delete();
				genSavePath();
			}
		} else {
			if(allowExternal && Gdx.files.isExternalStorageAvailable()) {
				savepath = Gdx.files.external("games/vftdan/horizon/saves/");
			} else {
				savepath = Gdx.files.local("saves/");
			}
			savepath.mkdirs();
			spf.writeString(savepath.file().getAbsolutePath(), false);
		}
		return savepath;
	}
	public static FileHandle getSavePath() {
		if(savepath == null) regen();
		return savepath;
	}
	public static FileHandle child(String name) {
		return getSavePath().child(name);
	}
	public static boolean saveObject(File file, Serializable obj) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.close();
			fos.close();
		} catch(IOException e) {
			return false;
		}
		return true;
	}
	public static boolean saveObject(FileHandle fh, Serializable obj) {
		return saveObject(fh.file(), obj);
	}
	public static boolean saveObject(String childFile, Serializable obj) {
		return saveObject(child(childFile), obj);
	}
	public static Object loadObject(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			ois.close();
			fis.close();
			return obj;
		} catch(Exception e) {
			if(e instanceof IOException || e instanceof ClassNotFoundException) {
				e.printStackTrace();
				return null;
			};
			throw (RuntimeException)e;
		}
	}
	public static Object loadObject(FileHandle fh) {
		return loadObject(fh.file());
	}
	public static Object loadObject(String childFile) {
		return loadObject(child(childFile));
	}
	public static boolean saveGameState(File file) {
		//if(player != null) player.prepareData();
		return saveObject(file, GAME.instance.session);
	}
	public static boolean saveGameState(FileHandle fh) {
		return saveObject(fh, GAME.instance.session);
	}
	public static boolean saveGameState(String childFile) {
		return saveObject(childFile, GAME.instance.session);
	}
	public static boolean loadGameState(File file) {
		Object obj = loadObject(file);
		if(obj == null || !(obj instanceof GameSession)) return false;
		GAME.instance.session = (GameSession)obj;
		//System.out.println(GAME.instance.session.playerData);
		return true;
	}
	public static boolean loadGameState(FileHandle fh) {
		return loadGameState(fh.file());
	}
	public static boolean loadGameState(String childFile) {
		return loadGameState(child(childFile));
	}
}
