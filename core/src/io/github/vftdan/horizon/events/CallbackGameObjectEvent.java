package io.github.vftdan.horizon.events;

import io.github.vftdan.horizon.gameMap.GameMap.*;

public class CallbackGameObjectEvent extends GameObjectEvent implements Runnable {
	public Runnable callback;

	@Override
	public void run() {
		if(callback != null) callback.run();
	}
	public CallbackGameObjectEvent() {
		
	}
	public CallbackGameObjectEvent(Runnable cb) {
		callback = cb;
	}
}
