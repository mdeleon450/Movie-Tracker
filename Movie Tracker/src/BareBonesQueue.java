// Author: Maik De Leon Lopez
// Interface for Queue
public interface BareBonesQueue {
	// We will have only 4 methods here
	public void offer(Object obj); 	// Adding Data
	public Object poll();			// Removing Data
	public boolean isEmpty();		// Check if empty
	public boolean isFull();		// Check if full
	public Object peek();			// Check the first element
}