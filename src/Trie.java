/**
 * File Name:   Trie.java
 * Description: A simple dictionary trie
 * Author:      Hengfeng Li
 * Student ID:  383606
 */

public class Trie {
    /** The number of alphabetic */
    public static final int NUM_CHARS = 26;

    /** The root of the trie */
    private TrieNode root;

    /** Initlize the trie and create the first element */
    public Trie(){
        root = new TrieNode();
    }

    /** Search a word whether exists in this trie */
    public TrieNode search(String word){
        TrieNode location = root;

        int char_code;

        for(int i = 0; location != null && i < word.length(); i++) {
            char_code = word.charAt(i) - 'a';

            location = location.getBranch(char_code);
        }
        if(location != null && location.getFlag()) {

            return location;
        }
        return null;
    }

    /** Insert a word into this dictionary trie */
    public boolean insert(String word){
        TrieNode location = root;
        boolean result = false;

        int char_code;

        for(int i = 0; i < word.length(); i++) {
            char_code = word.charAt(i) - 'a';

            if(location.getBranch(char_code) == null){
                location.createBranch(char_code);
            }

            location = location.getBranch(char_code);
        }

        if(!location.getFlag()){
            location.setFlag(true);
            result = true;
        }

        return result;
    }
}
