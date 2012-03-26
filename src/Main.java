/**
 * File Name:   Main.java
 * Description: The main entry of program
 * Author:      Hengfeng Li
 * Student ID:  383606
 */
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    /** The number of words in the corpus */
    public static double NUM_WORDS;

    /** Read the dictionary words into trie */
    public static void readDictionary(String filepath, Trie trie) throws IOException{
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(filepath));

        String word;

        while((word = reader.readLine()) != null)
        {
            trie.insert(word);
        }
        reader.close();
    }

    /** Read the corpus and stores the wrong words into a HashSet */
    public static double readCorpus(Trie dictTrie,String filepath,
                             HashSet<String> wrongWords
            ) throws IOException {
        BufferedReader reader;
        reader = new BufferedReader((new FileReader(filepath)));

        StringTokenizer lineTokenizer;

        String line;
        String word;
        double numWords = 0;

        TrieNode node;
        Pattern pattern = Pattern.compile("^[A-Za-z]*$");
        Matcher matcher;

        while((line = reader.readLine()) != null)
        {
            lineTokenizer = new StringTokenizer(line," ");
            if(lineTokenizer.countTokens() != 0) {
                word = lineTokenizer.nextToken().toLowerCase();
                matcher = pattern.matcher(word);
                if(matcher.matches()){
                    // records how many words are found
                    numWords++;

                    node = dictTrie.search(word);
                    if (node == null) {
                        // if the query can not be found in
                        // dictionary, then add it into the
                        // HashSet
                        wrongWords.add(word);
                    } else {
                        // if the word is found in dictionary,
                        // add its frequency
                        node.addFrequency();
                    }
                }
            }
        }
        reader.close();

        return numWords;
    }

    /** Output the wrong words from hashset to a file */
    public static void outputWrongWords(HashSet<String> wrongWords,
                    String outputFile) throws IOException {
        PrintWriter writer;
        writer = new PrintWriter(new FileOutputStream(outputFile));

        String key;

        for (Iterator<String> iter = wrongWords.iterator();iter.hasNext();) {
            key = iter.next();

            writer.println(key);
        }
        writer.close();
    }


    public static void main(String[] args) throws Exception{
        Scanner keyboard;

        String dictionary = "words.txt";
        String corpus = "enron-skilling.txt";
        String output = "output.txt";

        HashSet<String> wrongWords = new HashSet<String>();

        Trie trie = new Trie();

        if(args.length == 3) {
            dictionary = args[0];
            corpus = args[1];
            output = args[2];
        }
        else if(args.length == 0) {
            System.out.println("The system will using the default setting.");
            System.out.println("java Main words.txt enron-skilling.txt output.txt");
        }
        else {
            System.out.println("usage: java Main [dictionary] [corpus] [output]");
            return;
        }

        keyboard = new Scanner(System.in);

        System.out.println("-----------------------------");
        System.out.println("Welcome to Spelling Correction System!");
        System.out.println("Menu:");
        System.out.println("1. Output the unknown words.");
        System.out.println("2. 2-grams.");
        System.out.println("3. editex.");
        System.out.println("4. edit-distance.");
        System.out.print("\nInput function number:");
        int menuNum = keyboard.nextInt();
        keyboard.nextLine();

        // read the dictionary into a trie
        readDictionary(dictionary, trie);

        NUM_WORDS = readCorpus(trie, corpus, wrongWords);

        long startTime = System.currentTimeMillis();

        if(menuNum == 1) {
            // output the wrong words into a file
            outputWrongWords(wrongWords,output);
        }
        else if(menuNum == 2 ) {
            N_Gram.outputAlternatives(wrongWords,output,dictionary,trie);
        }
        else if(menuNum == 3) {
            Editex.outputAlternatives(wrongWords,output,dictionary,trie);
        }
        else {
            Edit_distance.outputAlternatives(wrongWords,output,dictionary,trie);
        }

        long consumingTime = System.currentTimeMillis() - startTime;
        System.out.println(consumingTime + " ms");
    }
}
