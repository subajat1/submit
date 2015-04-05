package jp.co.worksap.global;

import java.util.NoSuchElementException;

/// ==================================================================
// 
// author: Bayu Munajat | bayumunajat@outlook.com
// Assumptions :
// 1. since this class only the skeleton code,
//    please call its object from other file.
// 2. Enqueue() return an ImmutableQueue object,
//    with an additional element,
//    so please provide a different  ImmutableQueue object. 
// inspired from Vivek Mangla and many resources I read.
/// ==================================================================
public class ImmutableQueue<E> {

	Element<E> FRONT;
	Element<E> REAR;
	int SIZE = 0;
	ImmutableQueue<E> immutableQueue = null;

	@SuppressWarnings("rawtypes")
	private Element element;

	Element activePtr;
	Element inLoopPtr;
	
	public ImmutableQueue() {
		// modify this constructor if necessary, but do not remove default
		// constructor
		this.FRONT = null;
		this.REAR = null;		
		this.activePtr = null;
		
		this.element = null;
	}

	// add other constructors if necessary

	public ImmutableQueue<E> enqueue(E e) {
				
		this.immutableQueue = new ImmutableQueue<E>();
		
		// make sure element e is inside object of enqueued element
		this.element = new Element();
		this.element.object = e;

		// in case empty queue or only 1 element in queue
		if ((this.FRONT == null) || (this.FRONT == this.REAR.NEXT)) {
			this.immutableQueue.REAR = element;
			this.immutableQueue.FRONT = element;
		} else if (this.REAR.NEXT == null) {
			this.immutableQueue.FRONT = this.FRONT;
			this.immutableQueue.REAR = this.element;
			this.REAR.NEXT = this.element;
			
		}else{
			Element inLoop_element = null;
			inLoop_element = null;
			
			// activePtr begin from FRONT
			this.activePtr = this.FRONT;
			this.immutableQueue.FRONT = inLoop_element;			
			
			// shifting activePtr 1-by-1 until getting REAR
			while(this.activePtr != this.REAR.NEXT){
                inLoop_element = new Element();                
                inLoop_element.object = activePtr.object;
                
                inLoopPtr.NEXT = inLoop_element;
                inLoopPtr = inLoop_element; 
                activePtr = activePtr.NEXT;
            }
			// REAR have to point the enqueued element
			this.immutableQueue.REAR = this.element;
		}
		
		return this.immutableQueue;
	}

	public ImmutableQueue<E> dequeue() {
		// in case empty queue or only 1 element in queue
		if ((this.FRONT == null) || (this.FRONT == this.REAR.NEXT)) {
			this.FRONT = null;
			this.REAR = null;
		}

		// in case queue with more than 1 element
		this.immutableQueue = new ImmutableQueue<E>();
		this.immutableQueue.FRONT = this.FRONT.NEXT;
		this.immutableQueue.REAR = this.REAR;
		this.immutableQueue.SIZE = SIZE - 1;
		
		return this.immutableQueue;
	}

	public E peek() {
		return (E) this.FRONT.object;
	}

	/// 
	public int size() {
		return this.SIZE;
	}

	/// Custom class for helping contain object inside it
	public class Element<e> {
		e object;
		Element<e> NEXT = null;
	}
}
