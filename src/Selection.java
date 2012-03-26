/**
 * File Name:   Selection.java
 * Description: The selection mechanism
 * Author:      Hengfeng Li
 * Student ID:  383606
 */

public class Selection {
    /** The threshold of Edit-distance */
    public static double thresholdED = 0.00000358;
    /** The threshold of 2-gram */
    public static double thresholdNGram = 0.00000179;
    /** The threshold of Editex */
    public static double threshholdEditex = 0.00000358;

    /** Judge the distance of a word whether is a new word or not */
    public static double isNewWord(int distance, double freCorrectWord){
        double probability;
        // compute the probability of the best match to be a correction of query
        probability = Math.pow(2, 0 - distance) * (freCorrectWord/Main.NUM_WORDS);

        return probability;
    }
}
