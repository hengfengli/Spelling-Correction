/**
 * File Name:   TopNode.java
 * Description: A node in the Top-queue
 * Author:      Hengfeng Li
 * Student ID:  383606
 */

public class TopNode implements Predecessor {

    /* Records the number of distance */
	private int edit_distance;
    /* Records the word in this node */
    private String word;

	/* The node following this one. */
	private TopNode next;

    /* Initialize the node */
	public TopNode(String word, int edit_distance) {
		this.edit_distance = edit_distance;
        this.word = word;
		next = null;
	}

    public int getDistance(){
        return edit_distance;
    }

    public String getWord(){
        return word;
    }

	/* Return the next node. */
	public TopNode getNext() {
		return next;
	}

	/* Set the next node. */
	public void setNext(TopNode next) {
		this.next = next;
	}
}