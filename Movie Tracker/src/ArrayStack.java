// Author: Maik De Leon Lopez

public class ArrayStack <E> implements BareBonesStack <E>{
	
	//Storage for the Stack
	private E[] theData;
	private int TOS = -1;	//Indicates nothing is there initially
	private static final int INITIAL_CAPACITY = 5;
	
	//Default Constructor to create Stack
	public ArrayStack() {
		this.theData = (E[]) new Object[INITIAL_CAPACITY];
	}
	
	public ArrayStack(int size) {
		this.theData = (E[]) new Object[size];
	}
	
	@Override
	public E push(E obj) {
		//Adds elements to the Stack
		//First check if there is enough space to add elements
		if(TOS == this.theData.length -1) {	//Stack Full
			System.out.println("Stack Overflow");
			//We can now call reallocate() to increase the capacity
			return null;
		}
		//TOS ++; //Go to the proper index
		//this.theData[TOS] = obj; //And insert the Object
		return (this.theData[++TOS] = obj);
	}

	@Override
	public E pop() {
		//This method deletes an element from the stack
		//First check if there is an element to delete
		if(!isEmpty()) {
			E temp = this.theData[TOS];	//Save the data first
			TOS --;						//Then decrement the TOS
			return temp;				//Then return the element
			//return (this.theData[TOS--]);
		}
		else {
			System.out.println("Stack Underflow");
		}
		return null;
	}
	
	@Override
	public E peek() {
		//
		return null;
	}

	@Override
	public boolean isEmpty() {
		//This method checks if the Stack is Empty
		/*if(TOS == -1) {	
			return true;
		}
		return false;*/
		return (TOS == -1);
	}
	//Use toString to display the Stack Contents
	public String toString() {
		String s = "Stack: ";
		for(int i = 0 ; i <= TOS; i++) {
			s += this.theData[i]+" | ";
		}
		return s;
	}
	public int getSize() {
		return theData.length;
	}
}
