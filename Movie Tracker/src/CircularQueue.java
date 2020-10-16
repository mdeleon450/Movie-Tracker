// Author: Maik De Leon Lopez
// Queue implementation using circular array
public class CircularQueue implements BareBonesQueue {
	
	// Data
	private Object [] Q;	// Array reference for the actual Queue
	private int front;		// Front of the queue
	private int rear;		// Rear of the queue
	private int size;		// Number of elements in the queue
	private int capacity;	// Maximum elements in the queue
	private final int DEFAULT_CAPACITY = 5;	// Default Capactiy
	
	// Default Constructor
	public CircularQueue() {
		Q = new Object[DEFAULT_CAPACITY];
		this.capacity = this.DEFAULT_CAPACITY;
		this.front = 0;
		this.rear = 0;
		this.size = 0;
	}
	
	// Overloaded Constructor
	public CircularQueue(int capacity) {
		Q = new Object[capacity];
		this.capacity = capacity;
		this.front = 0;
		this.rear = 0;
		this.size = 0;
	}
	
	@Override
	public void offer(Object obj) {
		// This method adds elements to the Queue
		if(this.isFull()) {		// The Queue is full
			System.out.println("The Queue is full, cannot insert!");
			return;
		}
		// There is space to insert data
		// Insert at the rear
		Q[rear] = obj; 			// The data is inserted
		rear = (rear+1) % capacity;		// Update the rear for next data
		size++;					// Update the number in the queue
	}

	@Override
	public Object poll() {
		// This method deletes an element from the Queue
		// Check if there is an element to delete
		if(this.isEmpty()) {	// If there is nothing in the Queue
			System.out.println("The Queue is empty, cannot delete!");
			return null;
		}
		// There is an element to delete in the Queue
		Object temp = Q[front];			// Save the element that will be deleted to return
		Q[front] = null;				// Making the space empty, actual deletion
		front = (front+1) % capacity;	// Update front, don't forget mod
		size--;							// Update size
		return temp;					// Return the deleted element
	}

	@Override
	public boolean isEmpty() {
		return (size == 0);
	}

	@Override
	public boolean isFull() {
		/* 
		if(size == capacity) {
			return true;
		}
		return false;*/
		return(capacity == size);
	}

	@Override
	public Object peek() {
		// This method returns the Object at the front
		if(this.isEmpty()) {
			System.out.println("Queue Empty");
			return null;
		}
		Object temp = Q[front];
		return temp;
	}

	// Method to display the contents of the Queue
	public String toString() {
		String s = "Front:\n";
		// We will loop over the actual elements in the Queue
		for(int i = front; i< front + size; i++) {
			s += " | "+ Q[i % capacity];		// Point to actual data, use mod
		}
		return s;
		
		 //for(int i = 0; i<capacity; i++){
		//	 s += " | " + Q[i];
		// }
		 //return s;
	}
	// Helper Method to get the Size of the Queue
	public int getSize() {
		return this.size;
	}
}
