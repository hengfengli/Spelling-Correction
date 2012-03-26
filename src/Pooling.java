/**
 * File Name:   Pooling.java
 * Description: A evaluation program using pooling strategy
 * Author:      Hengfeng Li
 * Student ID:  383606
 */
import java.io.*;
import java.util.ArrayList;

public class Pooling {
    /** The size of items applied to computing the precision,recall,AP */
    private static int sizeOfComputing = 12;

    /** Find the same set from two list */
    public static ArrayList<String> findSameSet(ArrayList<String> list1,
                                                ArrayList<String> list2){
        ArrayList<String> sameSet = new ArrayList<String>();

        for(String str1 : list1){
            for(String str2 : list2 ){
                if(str1.equals(str2)){
                    sameSet.add(str1);
                    break;
                }
            }
        }
        return sameSet;
    }

    /** Find out the position of words in the same set */
    public static int[] outputWordsPosition(ArrayList<String> list,
                        ArrayList<String> sameSet, int[] counts){
        int i = list.size();
        int[] positions = new int[sizeOfComputing];
        counts[0] = 0;
        counts[1] = 0;

        for(String str : list) {
            for(String sameStr: sameSet) {
                if(str.equals(sameStr)){
                    // total relevant items
                    counts[1]++;

                    if(i <= sizeOfComputing)
                    {
                        // the relevant items in the 12-item
                        counts[0]++;
                        positions[i-1] = 1;
                    }
                    break;
                }
            }
            i--;
        }

        return positions;
    }

    /** Compute the average precision */
    public static double computeAP(int[] positions, int R){
        int d = positions.length;
        double sum1 = 0;
        int sum2;

        for(int i = 0; i < d; i++){
            if(positions[i] == 1){
                sum2 = 0;
                for(int j = 0; j < i+1; j++){
                    if(positions[j] == 1){

                        sum2 += 1;
                    }
                }
                sum1 += (double)sum2/(i+1);
            }
        }

        return sum1 == 0 ? 0 : sum1 / R;
    }

    public static void main(String[] args) throws Exception{
        String filepath = "query_test.txt";
        String dictFile = "words.txt";
        BufferedReader reader = new BufferedReader((new FileReader(filepath)));
        String line;

        ArrayList<String> topQueue1;
        ArrayList<String> topQueue2;
        ArrayList<String> topQueue3;
        ArrayList<String> sameSet;
        int i = 0;

        double precision1=0;
        double precision2=0;
        double precision3=0;
        double recall1=0;
        double recall2=0;
        double recall3=0;
        double average1=0;
        double average2=0;
        double average3=0;

        int[] positions;
        int[] counts = new int[2];

        if(args.length == 2) {
            dictFile = args[0];
            filepath = args[1];
        }
        else if(args.length == 0) {
            System.out.println("The system will using the default setting.");
            System.out.println("java Pooling words.txt query_test.txt");
        }
        else {
            System.out.println("usage: java Pooling [dictionary] [query]");
            return;
        }


        while((line = reader.readLine()) != null){
            // records the number of queries
            i++;

            // found out the same set of answers from three
            // different approximate matching techniques
            topQueue1 = N_Gram.search(line, dictFile).getCollection();
            topQueue2 = Editex.search(line, dictFile).getCollection();
            sameSet = findSameSet(topQueue1, topQueue2);
            topQueue3 = Edit_distance.search(line,dictFile).getCollection();
            sameSet = findSameSet(sameSet, topQueue3);

            // output the position of words in answers of 2-gram from same set
            positions = outputWordsPosition(topQueue1,sameSet,counts);

            // compute precision, recall, average precision
            precision1 += (double)counts[0]/12;
            recall1 += (double)counts[0]/counts[1];
            average1 += computeAP(positions,counts[1]);

            // output the position of words in answers of Editex from same set
            positions = outputWordsPosition(topQueue2,sameSet,counts);

            // compute precision, recall, average precision
            precision2 += (double)counts[0]/12;
            recall2 += (double)counts[0]/counts[1];
            average2 += computeAP(positions,counts[1]);

            // output the position of words in answers of edit-distance from same set
            positions = outputWordsPosition(topQueue3,sameSet,counts);

            // compute precision, recall, average precision
            precision3 += (double)counts[0]/12;
            recall3 += (double)counts[0]/counts[1];
            average3 += computeAP(positions,counts[1]);

        }
        System.out.println("Number of line: " + i);
        System.out.println("2-gram:");
        System.out.println("Precision: " + precision1/i);
        System.out.println("Recall: " + recall1/i);
        System.out.println("Average precision: " + average1/i);

        System.out.println();

        System.out.println("Editex:");
        System.out.println("Precision: " + precision2/i);
        System.out.println("Recall: " + recall2/i);
        System.out.println("Average precision: " + average2/i);

        System.out.println();

        System.out.println("Edit-distance:");
        System.out.println("Precision: " + precision3/i);
        System.out.println("Recall: " + recall3/i);
        System.out.println("Average precision: " + average3/i);

        reader.close();
    }
}
