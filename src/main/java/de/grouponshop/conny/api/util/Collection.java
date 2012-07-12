package de.grouponshop.conny.api.util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

/**
 * Decorator for a list with support for paging
 * information
 * 
 * @author jankrems
 *
 * @param <T>
 */
public class Collection<T> implements List<T> {
	
	private List<T> wrapped;
	private int limit;
	private int prev;
	private int next;
	private int first;
	private int last;

	public Collection(List<T> wrapped) {
		
		this.wrapped = wrapped;
	}

	@Override
	public boolean add(T arg0) {
		return wrapped.add(arg0);
	}

	@Override
	public void add(int arg0, T arg1) {
		wrapped.add(arg0, arg1);
	}

	@Override
	public boolean addAll(java.util.Collection<? extends T> arg0) {
		return wrapped.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, java.util.Collection<? extends T> arg1) {
		return wrapped.addAll(arg0, arg1);
	}

	@Override
	public void clear() {
		wrapped.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return wrapped.contains(arg0);
	}

	@Override
	public boolean containsAll(java.util.Collection<?> arg0) {
		return wrapped.containsAll(arg0);
	}

	@Override
	public T get(int arg0) {
		return wrapped.get(arg0);
	}

	@Override
	public int indexOf(Object arg0) {
		return wrapped.indexOf(arg0);
	}

	@Override
	public boolean isEmpty() {
		return wrapped.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return wrapped.iterator();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return wrapped.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<T> listIterator() {
		return wrapped.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		return wrapped.listIterator(arg0);
	}

	@Override
	public boolean remove(Object arg0) {
		return wrapped.remove(arg0);
	}

	@Override
	public T remove(int arg0) {
		return wrapped.remove(arg0);
	}

	@Override
	public boolean removeAll(java.util.Collection<?> arg0) {
		return wrapped.removeAll(arg0);
	}

	@Override
	public boolean retainAll(java.util.Collection<?> arg0) {
		return wrapped.retainAll(arg0);
	}

	@Override
	public T set(int arg0, T arg1) {
		return wrapped.set(arg0,  arg1);
	}

	@Override
	public int size() {
		return wrapped.size();
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		return wrapped.subList(arg0, arg1);
	}

	@Override
	public Object[] toArray() {
		return wrapped.toArray();
	}

	@Override
	public <E> E[] toArray(E[] arg0) {
		return wrapped.toArray(arg0);
	}
	
	public Collection<T> parseHeader(String linkHeader) {
		
		//<http://conny.degro.vm/api-v1/order?format=json&limit=10&skip=10>; rel="next", <http://conny.degro.vm/api-v1/order?format=json&limit=10&skip=20>; rel="last"
		StringTokenizer tokens = new StringTokenizer(linkHeader, ",;=");
		while (tokens.hasMoreTokens()) {
			
			// TODO: parse link-header and find out where we are (paging wise)
			//String t = tokens.nextToken().trim();
		}
		
		return this;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPrev() {
		return prev;
	}

	public void setPrev(int prev) {
		this.prev = prev;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

}
