
import java.io.BufferedWriter;
import java.io.IOException;

/*
 * File: Heap.java
 * Author: Yichen Dong
 * Date: May 9th
 * Purpose: This implements a Heap class for heapSort. 
 */

/**
 *
 * @author Yichen
 */
public class Heap{
        int[] heap;
        int size;
        
        public Heap(int[] array){
            heap = array;
            this.size = array.length;
        }
        
        public void sortHeap(){
            //int n = arr.length;
            //we are starting at the n/2-1th node, because that is the first node that will have a child
            //nodes past that will be leaf nodes.
            for(int i =size/2-1; i >=0; i--){
                heapify(heap,size,i);
            }
            //Move largest element (root) to end, and remove the "node" from the heap calculation
            for (int i = size-1; i>=0; i--){
                int temp = heap[0];
                heap[0] = heap[i];
                heap[i] = temp;
                //reheapify the reduced array
                heapify(heap,i,0);
            }
        }

        //i is the root of the node, calculate left and right based on root. Compare values.
        void heapify(int[] arr, int n, int i){
            int largest = i; //set the largest value intially to the root.
            int left = 2*i + 1;
            int right = 2*i + 2;

            if (left < n){
                if(arr[left] > arr[largest])
                largest = left; 
            }

            if(right < n){
                if(arr[right] > arr[largest])
                largest = right;
            }
            //compare to see if the largest value has changed 
            if(largest != i){
                int temp = arr[i];
                arr[i] = arr[largest];
                arr[largest] = temp;

                //Recursively run the heapify function on the subtree
                heapify(arr,n,largest);
            }
        }
        
        void printArray(BufferedWriter writer) throws IOException
        {
            for (int i=0; i<size; ++i)
                writer.append(heap[i]+" ");
            writer.append(System.getProperty("line.separator"));
        }
    }
