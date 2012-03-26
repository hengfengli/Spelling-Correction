/**
 * File Name:   TopQueue.java
 * Description: A Top-queue can records the top-ranking nodes
 * Author:      Hengfeng Li
 * Student ID:  383606
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

public class TopQueue implements Predecessor {

	/** The first node in the linked list. */
	private TopNode front;

    private int size = 0;

    /** The number of items printed out */
    private int printSize = 12;
    /** The number of items recorded by this queue*/
    private int topSize = 30;


	/** A LinkedList is initially empty. */
	public TopQueue() {
		front = null;
	}

    /** Get the ranking highest one */
    public TopNode getTopOne(){
        TopNode node = front;
        while (node.getNext() != null){
           node = node.getNext();
        }
        return node;
    }

    /** Get the distance of the last item */
    public int getEntryDistance(){
        return front.getDistance();
    }

    /** Print out the items of queue on the screen */
    public void print(){
        TopNode node = front;
        Stack<TopNode> stack = new Stack<TopNode>();

        while(node != null){
            stack.push(node);
            node = node.getNext();
        }

        for( int i = 0; i < this.size && i < printSize; i++){
            node = stack.pop();
            System.out.printf(" %d.%s", i + 1, node.getWord());
        }
        System.out.println();
    }

    /** Convert this queue to a collection */
    public final ArrayList<String> getCollection(){
        ArrayList<String> set = new ArrayList<String>();
        TopNode node = front;
        while (node != null){
           set.add(node.getWord());
           node = node.getNext();
        }
        return set;
    }

    /** Output the items of queue to a file */
    public void print(PrintWriter writer) throws IOException{
        TopNode node = front;
        Stack<TopNode> stack = new Stack<TopNode>();

        while(node != null){
            stack.push(node);
            node = node.getNext();
        }

        for( int i = 0; i < this.size && i < printSize; i++){
            node = stack.pop();
            writer.printf(" %d.%s",i+1, node.getWord());
        }
        writer.println();
    }

	/** A LinkedList is empty or not. */
	public boolean isEmpty() {
		return front == null;
	}

    /** Add a item to the queue */
	public boolean add(String word, int edit_distance) {

		// records the previous node
		Predecessor prev = this;

		// stores current node
		TopNode node = front;

		while( node != null) {
            if (edit_distance < node.getDistance()){
			    prev = node;
			    node = node.getNext();
            } else {
                break;
            }
        }

        TopNode newNode = new TopNode(word, edit_distance);
		prev.setNext(newNode);
        newNode.setNext(node);
        this.size++;

        if(this.size > topSize) {
            prev = this;
            node = front;
            this.setNext(front.getNext());
            this.size--;
        }

		return true;
	}

	/** Get the first node in the LinkedList.*/
	public TopNode getNext() {
		return front;
	}

	/** Set the first node in the LinkedList. */
	public void setNext(TopNode next) {
		front = next;
	}

}