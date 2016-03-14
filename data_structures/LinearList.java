/*
 * Author: Andrew Black
 * Course: CS310 Data Structures
 */
package data_structures;

import java.util.Iterator;

public class LinearList<E> implements LinearListADT<E> {
	int listSize;
	
	// Linear List Pointers
	Node frontNode;
	Node rearNode;
	
	class Node<T> {
		E data;
		Node next;
		Node last;
		
		public Node() {
			next = last = null;
			data = null;
		}
	}
	
	public LinearList () {
		clear();
	}
	
	@Override
	public boolean addFirst(E obj) {
		if(isEmpty()) {
			frontNode = rearNode = new Node();
			frontNode.data = obj;
			
			listSize++;
			return true;
		}
		
		// Create new Node
		Node tmpObj = new Node();
		tmpObj.data = obj;
		tmpObj.next = frontNode;
		
		frontNode = tmpObj;
		frontNode.next.last = frontNode;
		listSize++;
		
		return true;
	}

	@Override
	public boolean addLast(E obj) {
		if(isEmpty()) {
			frontNode = rearNode = new Node();
			frontNode.data = obj;
			
			listSize++;
			return true;
		}
		
		// Create new Node
		Node tmpObj = new Node();
		tmpObj.data = obj;
		tmpObj.last = rearNode;
		
		rearNode.next = tmpObj;
		rearNode = tmpObj; // Set new Node as rear
		listSize++;
		
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E removeFirst() {
		if ( isEmpty() )
			return null;
		
		// Create a temporary container
		Node tmpObj = frontNode;
		
		frontNode = frontNode.next; // Remove front node 
		listSize--;
		
		return (E) tmpObj.data;
	}

	@Override
	public E removeLast() {
		if ( isEmpty() )
			return null;
		
		// Create a temporary container
		Node tmpObj = rearNode;
		rearNode = rearNode.last; // Remove front node
		listSize--;
		
		return (E) tmpObj.data;
	}

	@Override
	public E remove(E obj) {
		E tmpObj = null;
		Node iterObj = frontNode;
		
		while (iterObj != null) {
			if(((Comparable<E>)iterObj.data).compareTo(obj) == 0) {
				tmpObj = (E) iterObj.data; 		// Keep data to return
				Node tmpNode = iterObj;
				
				if(iterObj.last == null) 
					return removeFirst();
				if(iterObj.next == null) 
					return removeLast();
				
				iterObj.last.next = iterObj.next;
				iterObj.next.last = iterObj.last;
				
				break;
			} else {
				iterObj = iterObj.next;
			}
		}
		if( tmpObj != null )
			listSize--;
		return (E) tmpObj;
	}

	@Override
	public E peekFirst() {
		return (E) frontNode.data;
	}

	@Override
	public E peekLast() {
		return (E) rearNode.data;
	}

	@Override
	public boolean contains(E obj) {
		return ( find(obj) != null );
	}

	@Override
	public E find(E obj) {
		for( E tmpObj : this ) {
			if(((Comparable<E>)tmpObj).compareTo(obj) == 0 )
				return tmpObj; // Object found, return object
		}
		
		return null; // Object not found
	}

	@Override
	public void clear() {
		rearNode = frontNode = null;
		listSize = 0;
	}

	@Override
	public boolean isEmpty() {
		return ( listSize == 0 );
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public int size() {
		return listSize;
	}

	@Override
	public Iterator<E> iterator() {
		return new IteratorHelper();
	}
	
	 class IteratorHelper implements Iterator<E> {
			public Node iterNode;
			
			public IteratorHelper() {
				iterNode = frontNode;
			}
			
			public boolean hasNext() {
				return iterNode != null;
			}
			
			public E next() {
				E tmpData = (E) iterNode.data;
				iterNode = iterNode.next;
				return tmpData;
				
			}
			
			public void remove() {
				throw new UnsupportedOperationException();
			}
	    }

}
