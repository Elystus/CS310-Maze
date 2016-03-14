/*
 * Author: Andrew Black
 * Course: CS310 Data Structures
 */
package data_structures;

import java.util.Iterator;

public class Queue<E> implements Iterable<E> {
	LinearListADT<E> objList;
	
	public Queue () {
		objList = new LinearList<E>();
	}
	
	// inserts the object obj into the queue
    public void enqueue(E obj) {
    	objList.addLast(obj);
    }
     
    // removes and returns the object at the front of the queue   
    public E dequeue() {
    	return (E) objList.removeFirst();
    }
    
    // returns the number of objects currently in the queue    
    public int size() {
    	return objList.size();
    }
    
    // returns true if the queue is empty, otherwise false   
    public boolean isEmpty() {
    	return objList.isEmpty();
    }
    
    // returns but does not remove the object at the front of the queue   
    public E peek() {
    	return (E) objList.peekFirst();
    }
    
    // returns true if the Object obj is in the queue    
    public boolean contains(E obj) {
    	return objList.contains(obj);
    }
     
    // returns the queue to an empty state  
    public void makeEmpty() {
    	objList.clear();
    }
    
    // removes the Object obj if it is in the queue and
    // returns true, otherwise returns false.
    public boolean remove(E obj) {
    	return (objList.remove(obj) != null ); 
    }
    
    // returns an iterator of the elements in the queue.  The elements
    // must be in the same sequence as dequeue would return them.    
    public Iterator<E> iterator() {
    	return objList.iterator();
    }
}
