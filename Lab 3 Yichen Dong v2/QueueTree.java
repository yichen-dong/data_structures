
import java.io.BufferedWriter;
import java.io.IOException;

/*
 * File: QueueTree.java
 * Author: Yichen Dong
 * Date: April 24th
 * Purpose: This creates a structure called a QueueTree. The purpose is to function both as a mechanism
 * for sorting, and as a mechanism for creating the Hoffman codes. The Queue is created first by sorting based
 * on frequency, length, and alphabet, in that order. Then, we use the queue properties of the QueueTree to pop out the
 * two least used letters, and combine them together. A new node is created with the two popped nodes on the left and right
 * branches of that node. To this end, each node requires two places to store data- a String and an int. It also requires 4 pointers-
 * a previous and a next for sorting, and a left and a right for the Huffman Tree.
 */

/**
 *
 * @author Yichen
 */
public class QueueTree {

    private int total;

    public HuffmanNode first, last;

    private class HuffmanNode {

        String letter;
        int frequency;
        HuffmanNode left = null;
        HuffmanNode right = null;
        HuffmanNode sortPrevious = null;
        HuffmanNode sortNext = null;

        public HuffmanNode(String letter, int frequency, HuffmanNode left, HuffmanNode right) {
            this.letter = letter;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }
    }

    //This adds a new node to the queue. This adds it based on frequency, then letter length, then alphabetic order.
    //The current node will always be set to last when first called.
    public QueueTree enQueue(String letter, int frequency, HuffmanNode current, HuffmanNode left, HuffmanNode right) {
        //System.out.println(total);
        HuffmanNode temp = new HuffmanNode(letter, frequency, left, right);

        //Recursive sorting procedure. First looks for the first node with which it has higher or equal frequency. If frequency is higher, insert it between current and next.
        //If frequency is equal, compare character lengths. If length is less than the current node, then we move to the previous node.
        //If length is greater than the current node, we put it after the current node, making sure to switch the points of the next node as well.
        //If the lengths are equal, compare first letter alphabets. If first letter is greater alphabetically, put it after.
        //If first letter is less alphabetically, go to previous one. If first letter is the same, then there is an error.
        //testing to see if the queue is empty. If it is, then we set the first and last to the temp node.
        if (total == 0) {
            last = temp;
            first = last;
            total++;
            //testing to see if we have reached the first node. If we have, change first.
        } else if (current == null) {
            temp.sortNext = first;
            first.sortPrevious = temp;
            first = temp;
            total++;
        } else {
            if (temp.frequency > current.frequency) {
                temp.sortNext = current.sortNext;
                temp.sortPrevious = current;
                if (temp.sortNext == null) {
                    last = temp;
                } else {
                    current.sortNext.sortPrevious = temp;
                }
                current.sortNext = temp;
                total++;

            } else if (temp.frequency == current.frequency) {
                if (temp.letter.length() > current.letter.length()) {
                    temp.sortNext = current.sortNext;
                    temp.sortPrevious = current;
                    current.sortNext.sortPrevious = temp;
                    current.sortNext = temp;
                    total++;
                } else if (temp.letter.length() == current.letter.length()) {
                    if (temp.letter.compareTo(current.letter) > 0) { //the one we are trying to insert has a greater alphabetic value
                        temp.sortNext = current.sortNext;
                        temp.sortPrevious = current;
                        current.sortNext.sortPrevious = temp;
                        current.sortNext = temp;
                        total++;
                    } else if (temp.letter.compareTo(current.letter) < 0) { //the one we are looking at is less alphabetically
                        enQueue(letter, frequency, current.sortPrevious, left, right);
                    } else if (temp.letter.compareTo(current.letter) == 0) {
                        System.out.println("ERROR: Letters are the same");
                        System.exit(0);
                    }
                } else if (temp.letter.length() < current.letter.length()) {
                    enQueue(letter, frequency, current.sortPrevious, left, right);
                }
            } else if (temp.frequency < current.frequency) {
                enQueue(letter, frequency, current.sortPrevious, left, right);
            }
        }
        //current.sortNext = last;

        //System.out.println(total);
        return this;
    }

    public QueueTree iterateHuffman(BufferedWriter bw) throws IOException {

        //testing to see if there is only one node left to be sorted. Returning without doing anything if the case.
        if (total == 1) {
            return this;
        } else {
            //iterating until there is only one node left in the "queue" part of the QueueTree. This will be the root
            //of the Hoffman Tree.
            while (total > 1) {
                bw.append(this.toString() + System.getProperty("line.separator"));
                //popping off the first two elements of the queue, while checking to see if there will be an error.
                //System.out.println(total);
                if (total == 0) {
                    throw new java.util.NoSuchElementException();
                }
                HuffmanNode temp = first;
                //temp.sortNext.sortPrevious = null; //why does this not cause an error?
                first = temp.sortNext;
                temp.sortNext = null;
                total--;
                if (total == 0) {
                    throw new java.util.NoSuchElementException();
                }
                HuffmanNode temp2 = first;
                //temp2.sortNext.sortPrevious = null; //why does this cause an error?
                first = temp2.sortNext;
                temp2.sortNext = null;
                total--;
                /*
                Doing some fancy comparisons to determine which node should be on the left and which on the right
                of the newly created node. It is then passed to the enQueue function to properly sort.
                 */
                if (temp.frequency < temp2.frequency) {
                    enQueue(temp.letter + temp2.letter, temp.frequency + temp2.frequency, last, temp, temp2);
                } else if (temp2.frequency < temp.frequency) {
                    enQueue(temp2.letter + temp.letter, temp.frequency + temp2.frequency, last, temp2, temp);
                } else {
                    if (temp.letter.length() < temp2.letter.length()) {
                        enQueue(temp.letter + temp2.letter, temp.frequency + temp2.frequency, last, temp, temp2);
                    } else if (temp2.letter.length() < temp.letter.length()) {
                        enQueue(temp2.letter + temp.letter, temp.frequency + temp2.frequency, last, temp2, temp);
                    } else {
                        if (temp.letter.compareTo(temp2.letter) < 0) { //temp letter comes before temp2 alphabetically
                            enQueue(temp.letter + temp2.letter, temp.frequency + temp2.frequency, last, temp, temp2);
                        } else if (temp.letter.compareTo(temp2.letter) > 0) {//temp letter comes after temp2 alphabetically
                            enQueue(temp2.letter + temp.letter, temp.frequency + temp2.frequency, last, temp2, temp);
                        } else { //letters should never be the same
                            System.out.println("Error: Letters are the same");
                            System.exit(0);
                        }
                    }
                }
            }
        }
        return this;
    }

    /*
    Provides a good way of keeping track of the current sorted queue. Prints the letters and their corresponding frequency
    */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        HuffmanNode temp = first;
        while (temp != null) {
            sb.append(temp.letter + " - " + temp.frequency + " " /*+ System.getProperty("line.separator")*/);
            temp = temp.sortNext;
        }
        return sb.toString();
    }
    /*
    Creating the codes for the finished HuffmanTree.
    */
    
    //Created a new function to print the Huffman values in preorder with their related frequencies. 
    public void printHuffmanPreorderTraversal(HuffmanNode current, BufferedWriter bw, String currentCode) throws IOException{
        if(current != null){
            bw.append(current.letter + " with frequency: " + current.frequency +System.getProperty("line.separator"));
            printHuffmanPreorderTraversal(current.left,bw,currentCode + "0");
            printHuffmanPreorderTraversal(current.right,bw,currentCode + "1");
        }
    }
    
    public StringBuilder getHuffmanValues(HuffmanNode current, String currentCode, StringBuilder sb) {
        if (current.left == null) {
            sb.append(current.letter + " - " + currentCode + System.getProperty("line.separator"));
            return sb;
        } else {
            getHuffmanValues(current.left, currentCode + "0", sb);
            getHuffmanValues(current.right, currentCode + "1", sb);
        }
        return sb;
    }
    
    /*
    Takes a string and decodes it by following the nodes of the tree.
    */
    public String decodeHuffman(String s) {
        String answer = "";
        HuffmanNode current = first;
        for (int i = 0; i < s.length(); i++) {
            if (s.substring(i, i + 1).equals("0")) {
                current = current.left;
            } else if (s.substring(i, i + 1).equals("1")) {
                current = current.right;
            } else {
                System.out.println("Non-valid letter found, skipping letter");
                continue;
            }

            if (current.left == null & current.right == null) {
                answer += current.letter;
                current = first;
            }
        }
        return answer;
    }
}
