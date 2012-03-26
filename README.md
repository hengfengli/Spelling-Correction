## How to compile it?

just compile all the java files.

	>>javac *.java

## How to run it?

a. Run the main entry:

	>> java main [dictionary] [corpus] [output]
	e.g.
	>> java main words.txt enron-skilling.txt output.txt

when you enter the main system, there are four choices.

1. output wrong words.
2. output the answers of 2-gram.
3. output the answers of editex.
4. output the answers of edit-distance.

(The answer means that if a word is a misspelling word, it will output
 the alternatives, otherwise output the new word information.)

b. Run each algorithm to make a query:

	>> java [algorithm] [dictionary]
	e.g. 
	>> java N_Gram words.txt
	or >> java Editex words.txt
	or >> java Edit_distance words.txt

and then, you can input words to get the answers.

## Pooling is a program to test and compute the 
precision, recall, average precision.

e.g. 
the "query_test.txt" contains 50 misspelling 
words, every line one word.
And then you can use the pooling to compute
precision, recall, average precision for three
techniques.

	>> java Pooling [dictionary] [query]
	e.g. 
	>> java Pooling words.txt query_test.txt
