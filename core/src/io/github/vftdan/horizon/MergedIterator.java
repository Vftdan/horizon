package io.github.vftdan.horizon;

import java.util.Iterator;

public class MergedIterator<E> implements Iterator<E> {
	private Iterator<Iterator<E>> iterators;
	private Iterator<E> currentIterator;
	private boolean EOI = false;
 	public MergedIterator(Iterable<Iterator<E>> iters) {
		iterators = iters.iterator();
		skipEmpty();
	}

 	private void skipEmpty() {
 		while(currentIterator == null || !currentIterator.hasNext()) {
 			if(!iterators.hasNext()) {
 				EOI = true;
 				return;
 			}
 			currentIterator = iterators.next();
 		}
 	}
 	
	@Override
	public boolean hasNext() {
		return !EOI;
	}

	@Override
	public E next() {
		E v = currentIterator.next();
		skipEmpty();
		return v;
	}

}
