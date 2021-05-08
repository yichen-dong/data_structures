/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yichen
 */
public class Stack {
    private int maxSize;
    private Integer[] stackArray;
    private int top;

    public Stack(int s) {
        maxSize = s;
        stackArray = new Integer[maxSize];
        top = -1;
    }

    public void push(Integer c) {
        stackArray[++top] = c;
    }

    public Integer pop() {
        return stackArray[top--];
    }

    public Integer peek() {
        return stackArray[top];
    }

    public boolean isEmpty() {
        return (top == -1);
    }

    public boolean isFull() {
        return (top == maxSize - 1);
    }

    public void clearStack() {
        while (this.isEmpty() == false) {
            this.pop();
        }
    }

    public int getLength() {
        return (top + 1);
    }
}
