/**
 * File Name:   Editex.java
 * Description: A phonetic matching technique "Editex"
 * Author:      Hengfeng Li
 * Student ID:  383606
 */
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;

public class Editex {

    /**
     * The group letters which sounds are similar.
     * The number means which group this letter locates
     */
    public static int[][] lettersInfo =
        {
         /*a*/{0},   /*b*/{1}, /*c*/{2,9}, /*d*/{3}, /*e*/{0}, /*f*/{7},
         /*g*/{6},   /*h*/{},  /*i*/{0},   /*j*/ {6},/*k*/{2}, /*l*/{4},
         /*m*/{5},   /*n*/{5}, /*o*/{0},   /*p*/{7}, /*q*/{2}, /*r*/{4},
         /*s*/{8,9}, /*t*/{3}, /*u*/{0},   /*v*/{7}, /*w*/{},  /*x*/{8},
         /*y*/{0},   /*z*/{8,9}
        };

    /** Judge whether two letters are in a group or not. */
    private static boolean isSameGroup(char char1, char char2){
        if(char1 == 'h' || char1 == 'w' || char2 == 'h' || char2 == 'w'
                || char1 == '#' || char2 == '#'){
            return false;
        }
        else {
            int [] char1GroupNum = lettersInfo[char1-'a'];
            int [] char2GroupNum = lettersInfo[char2-'a'];
            for(int i = 0; i < char1GroupNum.length; i++) {
                for(int j = 0; j < char2GroupNum.length; j++) {
                    if(char1GroupNum[i] == char2GroupNum[j]){
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Compute that two adjacent letters in a string
     * is equal or in a similar group
     */
    public static int d(char char1, char char2){
        if( (char1 == 'h' || char1 == 'w') && char1 != char2){
            return 1;
        }
        else {
            return r(char1, char2);
        }
    }

    /**
     * Compute that two letters in different strings
     * is equal or in a similar group
     */
    public static int r(char char1, char char2){
        int result = 2;
        if(char1 == char2) {
            result = 0;
        }
        else if(isSameGroup(char1,char2)){
            result = 1;
        }
        return result;
    }

    /**
     * The Editex algorithm which is similar to the normal
     * edit distance algorithm.
     */
    public static int editex(String string1, String string2){
        char[] str1 = ("#" + string1).toCharArray();
        char[] str2 = ("#" + string2).toCharArray();
        int length1, length2;
        length1 = str1.length;
        length2 = str2.length;

        int F[][] = new int[length1][length2];
        int dStr1[] = new int[length1-1];
        int dStr2[] = new int[length2-1];

        F[0][0] = 0;
        for(int i = 1; i < length1; i++) {
            dStr1[i-1] = d(str1[i-1],str1[i]);
            F[i][0] = F[i-1][0] + dStr1[i-1];
        }
        for(int i = 1; i < length2; i++) {
            dStr2[i-1] = d(str2[i-1],str2[i]);
            F[0][i] = F[0][i-1] + dStr2[i-1];
        }

        for(int i = 1; i < length1; i++) {
            for(int j = 1; j < length2; j++) {
                F[i][j] = min3(F[i - 1][j] + dStr1[i - 1],
                        F[i][j - 1] + dStr2[j - 1],
                        F[i - 1][j - 1] + r(str1[i], str2[j]));

            }

        }

        return  F[length1-1][length2-1];
    }

    /** Get the minimum in three numbers */
    public static int min3(int a, int b, int c) {
        if( a > b )
            return (( c > b) ? b : c);
        else
            return (( c > a) ? a : c);
    }

    /** Search a word by computing the number of distance in Editex */
    public static TopQueue search(String target, String dictFile) throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(dictFile));

        TopQueue topQueue = new TopQueue();
        String word;

        int distance;

        while((word = reader.readLine()) != null)
        {
            // get the number of distance in Editex
            distance = Editex.editex(target,word);

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

            if(probability < Selection.threshholdEditex) {
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

        if(args.length == 1) {
            dictFile = args[0];
        }
        else if(args.length == 0) {
            System.out.println("The system will using the default setting.");
            System.out.println("java Editex words.txt");
        }
        else {
            System.out.println("usage: java Editex [dictionary]");
            return;
        }

        while((line = reader.readLine()) != null){
            System.out.println("Editex:");
            long startTime = System.currentTimeMillis();
            topQueue = search(line, dictFile);
            topQueue.print();
            long consumingTime = System.currentTimeMillis() - startTime;
            System.out.println(consumingTime + " ms");
        }
    }
}
