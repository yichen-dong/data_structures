
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/*
 * File: PostFixConverter.java
 * Author: Yichen Dong
 * Date: March 5th
 * Purpose: This accepts a txt file with a postfix expression, and converts it into instructions for a machine. Calculations are not performed,
 * the instructins is merely printed and returned. Only alphabet letters and operators are accepted.
 */
public class PostFixConverter {

    public static void main(String[] args) {
        //Read in file using a command line argument, storing the letters in a character array
        String path = args[0];
        File file = new File(path);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            /*Reading the file line by line, and then converting it to a character array for processing*/
            while ((line = bufferedReader.readLine()) != null) {
                /*Checking if the line is blank*/
                if ("".equals(line)) {
                    System.out.println("ERROR: Blank line detected. Please check your expression and try again.");
                    continue;
                }

                char postfix[] = line.toCharArray();
                Stack stack = new Stack(postfix.length);
                int tempCounter = 1;
                System.out.print("\n");

                /*Checking to see if the first expression is an operator. Assumes that it's prefix and goes to the nxt line if it is*/
                if ("+-*/".indexOf(postfix[0]) != -1) {
                    System.out.println("ERROR: Prefix operation detected. Please check your expression and try again.");
                    stack.clearStack();
                    continue;
                }

                /**
                 * *
                 * Going through each character one by one. If it is an operand,
                 * it is pushed into the stack. If it is an operator, then the
                 * stack is popped two times and stored in temp variables. The
                 * function then prints the necessary operations. A temp
                 * variable is created and pushed into the stack using a
                 * counter. Then it moves on to the next letter.
                 *
                 * If, after the first pop, the stack is empty, then the program
                 * will flag an error for too many operators. If the character
                 * is a white space or a tab, the program does not make any
                 * assumptions, and returns and error for the user to correct.
                 * The same occurs if the program encounters something that is
                 * not an uppercase or lowercase letter or an operator (not
                 * including $). At the very end, a check is performed to see if
                 * there is only one value left in the stack. If that is not
                 * true, then we know there were too little operators for the
                 * operands, and return an error. *
                 */
                for (char letter : postfix) {
                    String operand1;
                    String operand2;
                    if (Character.isLetter(letter)) {
                        stack.push(Character.toString(letter));
                    } else if ("+".indexOf(letter) != -1) {
                        operand2 = stack.pop();
                        if (stack.isEmpty() == true) {
                            System.out.println("ERROR: Too many operators. Please check your expression and try again.");
                            stack.clearStack();
                            break;
                        }
                        operand1 = stack.pop();
                        System.out.println("LD " + operand1);
                        System.out.println("AD " + operand2);
                        System.out.println("ST " + "TEMP" + tempCounter);
                        stack.push("TEMP" + tempCounter);
                        tempCounter = tempCounter + 1;
                    } else if ("-".indexOf(letter) != -1) {
                        operand2 = stack.pop();
                        if (stack.isEmpty() == true) {
                            System.out.println("ERROR: Too many operators. Please check your expression and try again.");
                            stack.clearStack();
                            break;
                        }
                        operand1 = stack.pop();
                        System.out.println("LD " + operand1);
                        System.out.println("SB " + operand2);
                        stack.push("TEMP" + tempCounter);
                        System.out.println("ST " + "TEMP" + tempCounter);
                        tempCounter = tempCounter + 1;
                    } else if ("*".indexOf(letter) != -1) {
                        operand2 = stack.pop();
                        if (stack.isEmpty() == true) {
                            System.out.println("ERROR: Too many operators. Please check your expression and try again.");
                            stack.clearStack();
                            break;
                        }
                        operand1 = stack.pop();
                        System.out.println("LD " + operand1);
                        System.out.println("ML " + operand2);
                        System.out.println("ST " + "TEMP" + tempCounter);
                        stack.push("TEMP" + tempCounter);
                        tempCounter = tempCounter + 1;
                    } else if ("/".indexOf(letter) != -1) {
                        operand2 = stack.pop();
                        if (stack.isEmpty() == true) {
                            System.out.println("ERROR: Too many operators. Please check your expression and try again.");
                            stack.clearStack();
                            break;
                        }
                        operand1 = stack.pop();
                        System.out.println("LD " + operand1);
                        System.out.println("DV " + operand2);
                        System.out.println("ST " + "TEMP" + tempCounter);
                        stack.push("TEMP" + tempCounter);
                        tempCounter = tempCounter + 1;
                    } else if (letter == ' ' || letter == '\n') {
                        System.out.println("ERROR: White space or tabs are not accepted.Please check your expression and try again.");
                        stack.clearStack();
                        break;
                    } else {
                        System.out.println("ERROR: " + letter + " is not accepted. Please check your expression and try again.");
                        stack.clearStack();
                        break;
                    }
                }
                if (stack.getLength() > 1) {
                    System.out.println("ERROR: Items still remaining in stack. Check number of operands and try again.");
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
