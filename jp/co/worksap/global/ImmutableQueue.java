package jp.co.worksap.global;

import java.util.NoSuchElementException;

public class ImmutableQueue<E> {

	List FRONT = null, REAR = null;
	int SIZE = 0;
	ImmutableQueue<E> immutableQueue = null;
	
	public ImmutableQueue(){
		
	}
	
	// add other constructors if necessary
	
	public ImmutableQueue<E> enqueue(E e){
		return null;
	}	
	
	public ImmutableQueue<E> dequeue(){
		if((this.FRONT == null)||(this.REAR.NEXT == this.FRONT)){
			this.FRONT = null;
			this.REAR = null;
            
            throw new NoSuchElementException();
            /** <<YOU_MAY_HANDLE_THIS_EXCEPTION_ON_YOUR_OWN>> **/
        }
       
        this.immutableQueue = new ImmutableQueue<E>();
        this.immutableQueue.FRONT = this.FRONT.NEXT;
        this.immutableQueue.REAR = this.REAR;
        this.immutableQueue.SIZE=SIZE-1;
       
        return this.immutableQueue;
	}
	
	public E peek(){
		return null;
	}
	
	public int size(){
		return -1;
	}
		
	
	
	class List<E>{
	    
	    
	    E object;/**<<Node Data Part Can Contain Any Object>>**/
	    List NEXT = null;/***<<Reference to next Node>>**/
	}
}
