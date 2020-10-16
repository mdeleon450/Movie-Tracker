//@Author: Maik De Leon Lopez
public interface BareBonesStack <E> {
	E push(E obj);			//Method to add to stack
	E pop();				//Method to remove from stack
	E peek();				//Returns but does not remove TOS
	boolean isEmpty();		//Returns true if stack is empty
}