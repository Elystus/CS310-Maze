/*
 * Author: Andrew Black
 * Course: CS310 Data Structures
 */
package data_structures;

import java.util.Iterator;

public class Stack<E> implements Iterable<E> {
	LinearListADT<E> objList;
	
	public Stack() {
		objList = new LinearList<E>();
	}
	
	// inserts the object obj into the stack
    public void push(E obj) {
    	objList.addFirst(obj);
    }
    
    // pops and returns the element on the top of the stack    
    public E pop() {
    	return (E) objList.removeFirst();
    }
    
    // returns the number of elements currently in the stack
    public int size() {
    	return objList.size();
    }
    
    // return true if the stack is empty, otherwise false
    public boolean isEmpty() {
    	return objList.isEmpty();
    }
    
    // returns but does not remove the element on the top of the stack    
    public E peek() {
    	return (E) objList.peekFirst();
    }
     
    // returns true if the object obj is in the stack,
    // otherwise false   
    public boolean contains(E obj) {
    	return objList.contains(obj);
    }
    
    // returns the stack to an empty state    
    public void makeEmpty() {
    	objList.clear();
    }
    
    // returns true, otherwise returns false.
    public boolean remove(E obj) {
    	return ( objList.remove(obj) != null ); 
    }
    
    // returns a iterator of the elements in the stack.  The elements
    // must be in the same sequence as pop() would return them.    
    public Iterator<E> iterator() {
    	return (Iterator) objList.iterator();
    }
}
