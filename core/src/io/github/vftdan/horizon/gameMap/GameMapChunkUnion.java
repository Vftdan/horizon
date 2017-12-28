package io.github.vftdan.horizon.gameMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.github.vftdan.horizon.MergedIterator;
import io.github.vftdan.horizon.gameMap.GameMap.*;

public class GameMapChunkUnion extends GameMapChunk {
	private Set<GameMapChunk> chunks;
	public GameMapChunkUnion() {
		super();
		chunks = new HashSet<GameMapChunk>();
	}
	public boolean isSubChunk(GameMapChunk c) {
		if(c == this) return true;
		for(GameMapChunk e: chunks) {
			if(e.isSubChunk(c)) return true;
		}
		return false;
	}
	public Set<GameMapChunk> getChunkSet() {
		return chunks;
	}
	public void clear() {
		super.clear();
		chunks.clear();
	}
	public boolean contains(GameObject e) {
		if(super.contains(e)) return true;
		for(GameMapChunk c: chunks) {
			if(c.contains(e)) return true;
		}
		return false;
	}
	public boolean isEmpty() {
		return chunks.isEmpty() && super.isEmpty();
	}
	public Iterator<GameObject> iterator() {
		ArrayList<Iterator<GameObject>> iters = new ArrayList<Iterator<GameObject>>(chunks.size() + 1);
		//int i = 1;
		//iters.set(0, super.iterator());
		iters.add(super.iterator());
		for(GameMapChunk c: chunks) {
			//iters.set(i++, c.iterator());
			iters.add(c.iterator());
		}
		return new MergedIterator<GameObject>(iters);
	}
	public boolean remove(GameObject e) {
		boolean flag;
		flag = super.remove(e);
		for(GameMapChunk c: chunks) {
			flag = flag || c.remove(e);
		}
		return flag;
	}
	public boolean removeAll(Collection<?> c) {
		boolean flag = false;
		for(Object e: c) {
			flag = flag || remove(e);
		}
		return flag;
	}
	public boolean retainAll(Collection<?> c) {
		boolean flag = false;
		flag = flag || super.retainAll(c);
		for(GameMapChunk e: chunks) {
			flag = flag || e.retainAll(c);
		}
		return flag;
	}
	public int size() {
		int s = super.size();
		for(GameMapChunk c: chunks) {
			s += c.size();
		}
		return s;
	}
	public Object[] toArray() {
		return toSet().toArray();
	}
	public <T> T[] toArray(T[] a) {
		return toSet().toArray(a);
	}
	public Set<GameObject> toSet() {
		Set<GameObject> s = super.toSet();
		for(GameMapChunk c: chunks) {
			s.addAll(c);
		}
		return s;
	}
}
