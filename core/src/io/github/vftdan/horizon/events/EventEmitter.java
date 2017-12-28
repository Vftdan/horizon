package io.github.vftdan.horizon.events;

import java.util.Collection;

public interface EventEmitter<EventListener, Event> {
	public boolean addEventListener(EventListener el);
	public boolean removeEventListener(EventListener el);
	public boolean dispatchEvent(String ename, Event e);
	public Collection<EventListener> getEventListeners();
}
