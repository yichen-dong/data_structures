
/** *
 * File: Stack.java
 * Author: Yichen Dong
 * Date: March 5th
 * Purpose: This declares a stack with very standard implementation. There is a pop, push, and isEmpty method.
 * There is also an "isFull" function that is not used within the context of this program. There is a "clearStack:
 * function that removes everything from the stack.
 ** */
public class Stack {

    private int maxSize;
    private String[] stackArray;
    private int top;

    public Stack(int s) {
        maxSize = s;
        stackArray = new String[maxSize];
        top = -1;
    }

    public void push(String c) {
        stackArray[++top] = c;
    }

    public String pop() {
        return stackArray[top--];
    }

    public String peek() {
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
