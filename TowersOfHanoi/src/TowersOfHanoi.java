/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yichen
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yichen
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * File: TowersOfHanoi.java
 * Author: Yichen Dong
 * Date: April 3rd
 * Purpose: This accepts a number from a command line argument, and creates output up to that number. Only number of disks up to 10
 * has a file created due to space considerations. However, I decided to not change the underlying algorithm in order to keep the 
 * computing times consistent across all variations. 
 */
public class TowersOfHanoi {

    /*Declaring the recursive towers of Hanoi. This is very simple- for each level, there are three actions. You want to move n-1
    number of disks from the tower to an auxilliary tower. Once this is done, you move the main disk from the source to the destination
    tower. Then, you move the n-1 disks from the auxilliary tower to the destination tower. This is applied for every level, with the
    destination and aux tower switching for each level (so that a larger one never goes onto a smaller one). 
     */
    static void towerOfHanoiROutput(int n, char from_tower, char to_tower, char aux_tower, BufferedWriter writer) throws IOException {
        if (n == 1) {
            writer.append("Move disk 1 from tower " + from_tower + " to tower " + to_tower + System.getProperty("line.separator"));
            return;
        }
        towerOfHanoiROutput(n - 1, from_tower, aux_tower, to_tower, writer);
        writer.append("Move disk " + n + " from tower " + from_tower + " to tower " + to_tower + System.getProperty("line.separator"));
        towerOfHanoiROutput(n - 1, aux_tower, to_tower, from_tower, writer);
    }

    /*This is the beginning of the iterative solution. Unlike the recursive solution, there is no easy way to keep track of 
    the disks. Therefore, we have to use a stack as an intermediate function that keeps track of where the disks are. We 
    also have to keep track of the movement of the disks. We can see that there is a simple process, where the first move is between
    the source and the aux if the number of disks is even, or the source and the dest if the number of disks is odd. The second move
    is between the source and the dest if even, or source and aux if odd. The third move is always between the aux and the dest.
    We compare the sizes of the disks to know which one to move- the smaller one is always moved on top of the larger one. We calculate
    the total number of moves beforehand, which is 2^n-1. The is the upper limit for our for loop. 
     */
    static void moveDisks(int numDisks, Stack src, Stack dest,
            char s, char d, BufferedWriter writer) throws IOException {
        boolean pole1Empty = src.isEmpty();
        boolean pole2Empty = dest.isEmpty();
        int pole1TopDisk = -1;
        int pole2TopDisk = -1;
        //testing for stack underflow
        if (pole1Empty == false) {
            pole1TopDisk = src.pop();
        }
        if (pole2Empty == false) {
            pole2TopDisk = dest.pop();
        }
        // When the first pole is empty, we push whatever was on pole 2 without checking the inequality
        if (pole1Empty == true) {
            //testing for stack overflow
            if (src.isFull() == true) {
                writer.append("Error: Source stack overflow" + System.getProperty("line.separator"));
                System.exit(0);
            }
            src.push(pole2TopDisk);
            createMoveDiskDirections(d, s, pole2TopDisk, writer);
        } // When the second pole is empty, we push whatever was on pole 1 without checking the inequality
        else if (pole2Empty == true) {
            if (dest.isFull() == true) {
                writer.append("Error: Source stack overflow" + System.getProperty("line.separator"));
                System.exit(0);
            }
            dest.push(pole1TopDisk);
            createMoveDiskDirections(s, d, pole1TopDisk, writer);
        } // When top disk of pole1 > top disk of pole2, we push the top disk of pole1 back onto the stack, then
        // we push the top disk of pole2.
        else if (pole1TopDisk > pole2TopDisk) {
            if (src.isFull() == true) {
                writer.append("Error: Source stack overflow" + System.getProperty("line.separator"));
                System.exit(0);
            }
            src.push(pole1TopDisk);
            if (src.isFull() == true) {
                writer.append("Error: Source stack overflow" + System.getProperty("line.separator"));
                System.exit(0);
            }
            src.push(pole2TopDisk);
            createMoveDiskDirections(d, s, pole2TopDisk, writer);
        } // When top disk of pole1 < top disk of pole2, we push the top disk of pole2 back onto the stack, then
        // we push the top disk of pole1.
        else {
            if (dest.isFull() == true) {
                writer.append("Error: Source stack overflow" + System.getProperty("line.separator"));
                System.exit(0);
            }
            dest.push(pole2TopDisk);
            if (dest.isFull() == true) {
                writer.append("Error: Source stack overflow" + System.getProperty("line.separator"));
                System.exit(0);
            }
            dest.push(pole1TopDisk);
            createMoveDiskDirections(s, d, pole1TopDisk, writer);
        }
    }

    // Creating a method that outputs disk movement to a file to save on space/redundancy
    static void createMoveDiskDirections(char fromTower, char toTower, int diskNum, BufferedWriter writer) throws IOException {
        writer.append("Move disk " + diskNum
                + " from tower " + fromTower + " to tower " + toTower + System.getProperty("line.separator"));
    }

    // Iterative function that creates the for loop and calls the other methods to construct a sequence of instructions
    static void TowerOfHanoiI(int numDisks, Stack src, Stack dest, Stack aux, BufferedWriter writer) throws IOException {
        int i, total_num_of_moves;
        char sourceTower = 'A', destTower = 'B', auxTower = 'C';

        /* If number of disks is even, set dest to aux and aux to dest. This is because when the number
        of plates is even, we need to move to the dest first and aux second. When the number of plates is odd,
        we move to the dest first and aux second.
         */
        if (numDisks % 2 == 0) {
            char temp = destTower;
            destTower = auxTower;
            auxTower = temp;
        }
        total_num_of_moves = (int) (Math.pow(2, numDisks) - 1);

        // Create the initial "pole" using a stack. Larger numbers on the bottom.
        for (i = numDisks; i >= 1; i--) {
            src.push(i);
        }

        for (i = 1; i <= total_num_of_moves; i++) {
            switch (i % 3) {
                case 1:
                    moveDisks(numDisks, src, dest, sourceTower, destTower, writer);
                    break;
                case 2:
                    moveDisks(numDisks, src, aux, sourceTower, auxTower, writer);
                    break;
                case 0:
                    moveDisks(numDisks, aux, dest, auxTower, destTower, writer);
                    break;
                default:
                    break;
            }
        }
    }

    //  Driver method
    public static void main(String args[]) throws IOException {
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid integer");
            System.exit(0);
        }

        int n = Integer.parseInt(args[0]); // Number of disks based on command line argument
        String methodTimes[][] = new String[n + 1][3];
        methodTimes[0][0] = "Size";
        methodTimes[0][1] = "Recursive";
        methodTimes[0][2] = "Iterative";
        for (int i = 1; i <= n; i++) {
            String outputFile = "Output of size " + i + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            writer.append("Running for size " + i + System.getProperty("line.separator"));
            writer.append("Recursive" + System.getProperty("line.separator"));
            long startTime;
            long endTime;

            //Time and call the recursive functions
            startTime = System.nanoTime();
            towerOfHanoiROutput(i, 'A', 'B', 'C', writer);
            endTime = System.nanoTime();
            float runtimeR = (float) ((endTime - startTime) / 1000000.0);
            writer.append("Method time in ms: " + runtimeR + System.getProperty("line.separator"));

            //Time and call the iterative functions. Stacks have to be declared
            writer.append("\nIterative" + System.getProperty("line.separator"));
            Stack src = new Stack(i), dest = new Stack(i), aux = new Stack(i);
            startTime = System.nanoTime();
            TowerOfHanoiI(i, src, dest, aux, writer);
            endTime = System.nanoTime();
            float runtimeI = (float) ((endTime - startTime) / 1000000.0);
            writer.append("Method time in ms: " + runtimeI + System.getProperty("line.separator"));

            methodTimes[i][0] = Integer.toString(i);
            methodTimes[i][1] = Float.toString(runtimeR);
            methodTimes[i][2] = Float.toString(runtimeI);
            writer.close();
        }

        String outputFileTimes = "Runtimes of size " + n + ".csv";
        StringBuilder outputTimes = new StringBuilder();
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileTimes));
        for (int i = 0; i < methodTimes.length; i++) {
            for (int j = 0; j < methodTimes[i].length; j++) {
                outputTimes.append(methodTimes[i][j] + ",");
            }
            outputTimes.append(System.getProperty("line.separator"));
        }
        writer.append(outputTimes);
        writer.close();
    }
}
