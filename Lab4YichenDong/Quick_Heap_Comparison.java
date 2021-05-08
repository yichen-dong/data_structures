
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * File: Quick_Heap_Comparison.java
 * Author: Yichen Dong
 * Date: May 9th
 * Purpose: This accepts a file of numbers and sorts using Heap and Quicksort, then compares the runtimes. 
 */
public class Quick_Heap_Comparison {

    /*
    Quicksort implementation. String type is used to denote what type of pivot we are using. It only 
    accepts first and mid, for first value and median value. 
     */
    public static Integer makePartition(int[] arr, int low, int high, String type) {
        int l = low;
        int h = high;
        int pivot = arr[low]; //setting to low by default
        if (type == "first") {
            pivot = arr[low];
        } else if (type == "mid") {
            //assigns the mid pivot based on comparisons of the beginning, end, and midpoint numbers
            int midpoint = low + (high - low) / 2;
            int temp = arr[midpoint];
            if ((arr[midpoint] <= arr[high] & arr[midpoint] >= arr[low])
                    | (arr[midpoint] >= arr[high] & arr[midpoint] <= arr[low])) {
                pivot = arr[midpoint];
            } else if ((arr[low] <= arr[high] & arr[low] >= arr[midpoint])
                    | (arr[low] >= arr[high] & arr[low] <= arr[midpoint])) {
                pivot = arr[low];
            } else if ((arr[high] >= arr[low] & arr[high] <= arr[midpoint])
                    | (arr[high] <= arr[low] & arr[high] >= arr[midpoint])) {
                pivot = arr[high];
            } else {
                System.out.print("Error in pivot assignment for " + arr.length + type);
                System.exit(0);
            }
        }

        int temp;
        boolean done = false;
        while (!done) {
            //Move the lower pointer up by one until it finds a number greater than or equal 
            //to the pivot.
            while (arr[l] < pivot) {
                ++l;
            }
            //Move the high pointer down by one until it finds a number less than or equal 
            //to the pivot.
            while (pivot < arr[h]) {
                --h;
            }
            //switch low and high unless the pointers are past each other. 
            if (l >= h) {
                done = true;
            } else {
                //if 
                temp = arr[l];
                arr[l] = arr[h];
                arr[h] = temp;

                ++l;
                --h;
            }
        }

        return h;
    }

    public static int[] quickSort(int[] arr, int low, int high, int n, String type) {
        //end the sort once the array is n units long or less. The plus one is because
        //the subtraction does not account for the base elements. E.g. 20-0 contains 21 elements because
        // 20 and 0 are both included. Once that point is reached, do an insertion sort on what's left
        if (high - low + 1 <= n) {
            return insertionSort(arr, low, high);
        }
        int j = makePartition(arr, low, high, type); //j is set to the 

        //recursively sort the two partitions
        quickSort(arr, low, j, n, type);
        quickSort(arr, j + 1, high, n, type);

        return arr;
    }

    public static int[] insertionSort(int[] arr, int low, int high) {
        for (int i = low + 1; i <= high; ++i) {
            int value = arr[i];
            int j = i - 1;

            //Shift everything down one spot to make room for a smaller value
            while (j >= 0 & arr[j] > value) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = value;
        }
        return arr;
    }

    static void printArray(int[] arr, BufferedWriter writer) throws IOException {
        int n = arr.length;
        for (int i = 0; i < n; ++i) {
            writer.append(arr[i] + " ");
        }
        writer.append(System.getProperty("line.separator"));
    }

    public static void main(String[] args) throws IOException {

        //int[] listSizes = {50, 500, 1000, 2000, 5000}; //anyway to do this with command line arguments?
        String[] fileTypes = args[0].split(",");
        String[] listSizeString = args[1].split(",");
        int[] listSizes = new int[listSizeString.length];
        //converting the second command line argument to int
        for (int i = 0; i < listSizeString.length; i++) {
            listSizes[i] = Integer.parseInt(listSizeString[i]);
        }
        String inputDirectory = args[2];
        String outputDirectory = args[3];

        int numRows = fileTypes.length * listSizeString.length;
        long[][] runTimeArray = new long[numRows][6];
        int rowCounter = 0;

        String timeOutputPath = outputDirectory + "TimeofRuns.csv";
        FileWriter outputTimesWriter = new FileWriter(timeOutputPath);
        BufferedWriter timesBufferedWriter = new BufferedWriter(outputTimesWriter);

        for (String type : fileTypes) {
            for (int i : listSizes) {
                int n = i;
                int[] sortArray = new int[n];
                String path = inputDirectory + type + n + ".txt";
                String outputPath = outputDirectory + "OutputOf" + type + n + ".txt";
                String line;
                int arrayCounter = 0;
                File file = new File(path);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                FileWriter fileWriter = new FileWriter(outputPath);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                //Reading each line of the file into the array, incrementing the array counter each time
                while ((line = bufferedReader.readLine()) != null) {
                    //System.out.println(line);
                    try {
                        sortArray[arrayCounter] = Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        writer.append("Cannot convert " + line + "in" + path + "to integer. Please make sure file "
                                + "has a single integer per line.");
                        System.exit(n);
                    }

                    sortArray[arrayCounter] = Integer.parseInt(line);
                    arrayCounter++;
                }

                writer.append("Original list: " + System.getProperty("line.separator"));
                printArray(sortArray, writer);

                Heap heapArray = new Heap(sortArray);
                long startTimeHeap;
                long endTimeHeap;
                long startTimeQuick2;
                long endTimeQuick2;
                long startTimeQuick50;
                long endTimeQuick50;
                long startTimeQuick100;
                long endTimeQuick100;
                long startTimeQuickMid;
                long endTimeQuickMid;

                //Start timing the heap runs
                startTimeHeap = System.nanoTime();
                heapArray.sortHeap();
                endTimeHeap = System.nanoTime();
                writer.append("Output for heap sort" + System.getProperty("line.separator"));
                heapArray.printArray(writer);
                int[] quickArray;

                //Start timing the 4 different cases of quicksorts
                startTimeQuick2 = System.nanoTime();
                quickArray = quickSort(sortArray, 0, sortArray.length - 1, 2, "first");
                endTimeQuick2 = System.nanoTime();
                writer.append("Output for Quicksort with first pivot and parition size 2:" + System.getProperty("line.separator"));
                printArray(quickArray, writer);

                startTimeQuick50 = System.nanoTime();
                quickArray = quickSort(sortArray, 0, sortArray.length - 1, 50, "first");
                endTimeQuick50 = System.nanoTime();
                writer.append("Output for Quicksort with first pivot and parition size 50:" + System.getProperty("line.separator"));
                printArray(quickArray, writer);

                startTimeQuick100 = System.nanoTime();
                quickArray = quickSort(sortArray, 0, sortArray.length - 1, 100, "first");
                endTimeQuick100 = System.nanoTime();
                writer.append("Output for Quicksort with first pivot and parition size 100:" + System.getProperty("line.separator"));
                printArray(quickArray, writer);

                startTimeQuickMid = System.nanoTime();
                quickArray = quickSort(sortArray, 0, sortArray.length - 1, 2, "mid");
                endTimeQuickMid = System.nanoTime();
                writer.append("Output for Quicksort with median pivot and parition size 2:" + System.getProperty("line.separator"));
                printArray(quickArray, writer);

                long runTimeHeap = endTimeHeap - startTimeHeap;
                long runTimeQuick2 = endTimeQuick2 - startTimeQuick2;
                long runTimeQuick50 = endTimeQuick50 - startTimeQuick50;
                long runTimeQuick100 = endTimeQuick100 - startTimeQuick100;
                long runTimeQuickMid = endTimeQuickMid - startTimeQuickMid;

                writer.append("For file type " + type + " of size " + n + ":" + System.getProperty("line.separator"));
                writer.append("Time for Heap: " + runTimeHeap + System.getProperty("line.separator"));
                writer.append("Time for Quick with first item pivot and partition size =<2: "
                        + runTimeQuick2 + System.getProperty("line.separator"));
                writer.append("Time for Quick with first item pivot and partition size =<50: "
                        + runTimeQuick50 + System.getProperty("line.separator"));
                writer.append("Time for Quick with first item pivot and partition size =<100: "
                        + runTimeQuick100 + System.getProperty("line.separator"));
                writer.append("Time for Quick with median item pivot and partition size =<2: "
                        + runTimeQuickMid + System.getProperty("line.separator"));
                writer.append(System.getProperty("line.separator"));

                //Saving the runtimes for output as a csv
                runTimeArray[rowCounter][0] = n;
                runTimeArray[rowCounter][1] = runTimeHeap;
                runTimeArray[rowCounter][2] = runTimeQuick2;
                runTimeArray[rowCounter][3] = runTimeQuick50;
                runTimeArray[rowCounter][4] = runTimeQuick100;
                runTimeArray[rowCounter][5] = runTimeQuickMid;
                rowCounter++;

                fileReader.close();
                writer.close();
            }
        }
        //outputting as csv. The headers were added manually later by me.
        for (int i = 0; i < runTimeArray.length; i++) {
            for (int j = 0; j < runTimeArray[i].length; j++) {
                timesBufferedWriter.append(runTimeArray[i][j] + ",");
            }
            timesBufferedWriter.append(System.getProperty("line.separator"));
        }
        timesBufferedWriter.close();
    }
}
