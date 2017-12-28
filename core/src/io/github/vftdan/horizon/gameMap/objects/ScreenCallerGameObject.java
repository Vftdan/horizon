package io.github.vftdan.horizon.gameMap.objects;

public interface ScreenCallerGameObject extends IGameObject {
	public void screenCallback(int status, Object data);
}
