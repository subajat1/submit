package jp.co.worksap.global;

import java.util.NoSuchElementException;

/// ==================================================================
// 
// inspired from Vivek Mangla
/// ==================================================================
public class ImmutableQueue<E> {

	List<E> FRONT = null, REAR = null;
	int SIZE = 0;
	ImmutableQueue<E> immutableQueue = null;

	@SuppressWarnings("rawtypes")
	private List helper;

	public ImmutableQueue() {
		// modify this constructor if necessary, but do not remove default
		// constructor
	}

	// add other constructors if necessary

	public ImmutableQueue<E> enqueue(E e) {
		this.helper = new List();

		if ((this.FRONT == null) || (this.REAR.NEXT == this.FRONT)) {
			this.immutableQueue.REAR = helper;
			this.immutableQueue.FRONT = helper;
		} else if (this.REAR.NEXT == null) {
			this.immutableQueue.FRONT = this.FRONT;
			this.immutableQueue.REAR = this.helper;
			this.REAR.NEXT = this.helper;
		}
		return this.immutableQueue;
	}

	public ImmutableQueue<E> dequeue() {
		if ((this.FRONT == null) || (this.REAR.NEXT == this.FRONT)) {
			this.FRONT = null;
			this.REAR = null;
		}

		this.immutableQueue = new ImmutableQueue<E>();
		this.immutableQueue.FRONT = this.FRONT.NEXT;
		this.immutableQueue.REAR = this.REAR;
		this.immutableQueue.SIZE = SIZE - 1;

		return this.immutableQueue;
	}

	public E peek() {
		return (E) this.FRONT.object;
	}

	public int size() {
		return this.SIZE;
	}

	public class List<e> {
		e object;
		List<e> NEXT = null;
	}
}
