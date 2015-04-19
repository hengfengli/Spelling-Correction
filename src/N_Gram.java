/**
 * File Name:   N_Gram.java
 * Description: An implementation of 2-gram algorithm
 * Author:      Hengfeng Li
 */
import java.io.*;
import java.util.*;

public class N_Gram {

    /** Compute the distance in 2-gram */
    public static int BiGram_compute(String target, String word){
        int similarity = 0;
        char char1, char2;

        StringBuilder wordStr1 = new StringBuilder('#' + word);
        StringBuilder wordStr2 = new StringBuilder(word + '#');

        Queue<Character> targetQueue1 = new LinkedList<Character>();
        Queue<Character> targetQueue2 = new LinkedList<Character>();

        // the target word becomes two queue, e.g. "words"
        // the first queue:  #words
        // the second queue: words#
        targetQueue1.offer('#');
        for(int i = 0; i < target.length(); i++) {
            targetQueue1.offer(target.charAt(i));
            targetQueue2.offer(target.charAt(i));
        }
        targetQueue2.offer('#');

        while(!targetQueue1.isEmpty()){
            // dequeue the first element of
            // the two queue, and then
            // will produce the pairs of "#w","wo",
            // "or","rd","ds","s#".
            char1 = targetQueue1.poll();
            char2 = targetQueue2.poll();

            for(int i = 0; i < wordStr1.length(); i++) {
                if(char1 == wordStr1.charAt(i) && char2 == wordStr2.charAt(i)) {

                    similarity += 1;
                    wordStr1.deleteCharAt(i);
                    wordStr2.deleteCharAt(i);
                    break;
                }
            }
        }
        // the less, the more similar
        return target.length() + word.length() - 2 * similarity;
        // return similarity;
    }

    /** Search a word by computing the number of distance in 2-gram */
    public static TopQueue search(String target, String dictFile) throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(dictFile));

        TopQueue topQueue = new TopQueue();
        String word;

        int distance;

        while((word = reader.readLine()) != null)
        {
            // get the number of distance in 2-gram
            distance = BiGram_compute(target, word);

            if(!topQueue.isEmpty()){
                if(distance < topQueue.getEntryDistance()){
                    topQueue.add(word,distance);
                }
            } else {
                topQueue.add(word,distance);
            }
        }
        return topQueue;
    }

    /** Output the alternatives according to the wrong words */
    public static void outputAlternatives(HashSet<String> wrongWords,
                    String outputFile, String dictFile, Trie trie) throws IOException {

        PrintWriter writer;
        writer = new PrintWriter(new FileOutputStream(outputFile));

        TopQueue topQueue;
        TopNode topNode;

        double probability;
        int wordFrequency;

        String word;

        for (Iterator<String> iter = wrongWords.iterator();iter.hasNext();) {
            // get a wrong word
            word = iter.next();

            writer.println(word);

            // search the alternatives by using the approximate techniques
            topQueue = search(word, dictFile);
            // get the best match
            topNode = topQueue.getTopOne();
            // get the frequency of the best match
            wordFrequency = trie.search(topNode.getWord()).getFrequency();
            // compute how probably this best match could be a correction to the query
            probability = Selection.isNewWord(topNode.getDistance(), wordFrequency);

            if(probability < Selection.thresholdNGram ) {
                // if the probability less than a threshold,
                // then it may be a new word.
                writer.println("This is a new word.");
            }
            else {
                topQueue.print(writer);
            }
        }

        writer.flush();
        writer.close();
    }

    public static void main(String[] args) throws Exception{
        String dictFile = "words.txt";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        TopQueue topQueue;
        long startTime;
        long consumingTime;

        if(args.length == 1) {
            dictFile = args[0];
        }
        else if(args.length == 0) {
            System.out.println("The system will using the default setting.");
            System.out.println("java N_Gram words.txt");
        }
        else {
            System.out.println("usage: java N_Gram [dictionary]");
            return;
        }

        while((line = reader.readLine()) != null){
            System.out.println("2-gram search:");
            startTime = System.currentTimeMillis();
            topQueue = search(line, dictFile);
            topQueue.print();
            consumingTime = System.currentTimeMillis() - startTime;
            System.out.println(consumingTime + " ms");
        }
    }
}