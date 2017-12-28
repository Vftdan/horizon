package io.github.vftdan.horizon.gameMap;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import io.github.vftdan.horizon.gameMap.GameMap.*;

public class GameMapChunk implements Set<GameObject> {
	private Set<GameObject> objects;
	public GameMap map;
	public GameMapChunk() {
		objects = new HashSet<GameObject>();
	}
	public GameMapChunk(Iterable<GameObject> objs) {
		this();
		for(GameObject o: objs) {
			objects.add(o);
		}
	}
	public boolean isSubChunk(GameMapChunk c) {
		return this == c;
	}
	@Override
	public boolean add(GameObject e) {
		if(!e.chunk.isSubChunk(this)) return false;
		return objects.add(e);
	}
	@Override
	public boolean addAll(Collection<? extends GameObject> c) {
		boolean flag = false;
		for(GameObject e: c) {
			flag = flag || add(e); 
		}
		return flag;
	}
	@Override
	public void clear() {
		objects.clear();
	}
	@Override
	public boolean contains(Object o) {
		return objects.contains(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return objects.containsAll(c);
	}
	@Override
	public boolean isEmpty() {
		return objects.isEmpty();
	}
	@Override
	public Iterator<GameObject> iterator() {
		return objects.iterator();
	}
	@Override
	public boolean remove(Object o) {
		return objects.remove(o);
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		return objects.removeAll(c);
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		return objects.retainAll(c);
	}
	@Override
	public int size() {
		return objects.size();
	}
	@Override
	public Object[] toArray() {
		return objects.toArray();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return objects.toArray(a);
	}
	public Set<GameObject> toSet() {
		return new HashSet<GameObject>();
	}
}
