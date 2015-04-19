/**
 * File Name:   Predecessor.java
 * Description: A interface applied to a Top-queue
 * Author:      Hengfeng Li
 */

public interface Predecessor {
	
	/** Get the next ListNode. */
	public TopNode getNext();
	
	/** Set the next ListNode. */
	public void setNext(TopNode next);
	
}