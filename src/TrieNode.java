/**
 * File Name:   TrieNode.java
 * Description: A node in the trie
 * Author:      Hengfeng Li
 * Student ID:  383606
 */

public class TrieNode {
    /** The flag represents whether it is a word */
    private boolean flag;
    /** The frequency of a word being queried */
    private int frequency;
    /** The branches of this node */
    private TrieNode[] branch = new TrieNode[Trie.NUM_CHARS];

    /** Initialize the node */
    public TrieNode(){
        flag = false;
        frequency = 1;
    }

    public void addFrequency(){
        this.frequency++;
    }

    public int getFrequency(){
        return this.frequency;
    }

    public TrieNode getBranch(int index){
        return this.branch[index];
    }

    public void createBranch(int index){
        this.branch[index] = new TrieNode();
    }

    public void setFlag(boolean flag){
        this.flag = flag;
    }

    public boolean getFlag(){
        return this.flag;
    }
}
