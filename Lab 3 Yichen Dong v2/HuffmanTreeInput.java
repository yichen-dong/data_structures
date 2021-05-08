
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

/*
 * File: HuffmanTreeInput.java
 * Author: Yichen Dong
 * Date: April 24th
 * Purpose: This file takes 3 arguments- the freqTable, the file to be read, and a "Encode"/"Decode" value that tells the program
 * whether you want to encode or decode. This creates a Huffman tree based on the freqtable, and outputs the process behind
 * making that tree, as well as a table of the letter and the code associated with it. It also spits out output for the 
 * finished encoding/decoding.
 */
/**
 *
 * @author Yichen
 */
public class HuffmanTreeInput {

    //creating a method that tests to see if the integer can be parsed. 
    public static boolean canParse(String str, Integer begin, Integer end) {
        try {
            Integer.parseInt(str.substring(begin, end));
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
    }

    //This will take a string and a encode array made by the program to encode a normal string
    //Somehow, the stringbuilder keeps getting overwritten when I use a string array. It's printing out everything
    //normally, but it just won't append without clearing the array. I commented this out because it wasn't writing
    //to the stringbuilder properly.
    

    public static void main(String[] args) {
        try {

            String frequencyPath = args[0]; 
            String secondaryFilePath = args[1];
            File frequencyFile = new File(frequencyPath);
            String fileType = args[2]; 
            String frequencyLine;
            QueueTree huffmanQueue = new QueueTree();
            FileReader fileReader = new FileReader(frequencyFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String outputFile = args[3];
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            Integer lineNum = 0;

            //Reading in line by line. Testing to see if the line has at least one letter first, to make sure
            //that it is not a blank line. Then, testing to see if we can parse two numbers to integer. If not,
            //we test to see if we can parse a single digit number. Each successful line is then added to a 
            //Hoffman QueueTree.
            while ((frequencyLine = bufferedReader.readLine()) != null) {
                //testing for blank lines, so that substring doesn't break later on
                try {
                    frequencyLine.substring(0, 1);
                } catch (StringIndexOutOfBoundsException e) {
                    continue;
                }

                String letter = frequencyLine.substring(0, 1);
                if (canParse(frequencyLine, 4, 6) == true) {
                    Integer frequency = Integer.parseInt(frequencyLine.substring(4, 6));
                    huffmanQueue.enQueue(letter, frequency, huffmanQueue.last, null, null);
                } else if (canParse(frequencyLine, 4, 5) == true) {
                    Integer frequency = Integer.parseInt(frequencyLine.substring(4, 5));
                    huffmanQueue.enQueue(letter, frequency, huffmanQueue.last, null, null);
                } else {
                    System.out.println("you done goofed");
                }
                lineNum++;
            }
            fileReader.close();

            //Calling iterateHuffman to turn Huffman into a tree. Writing the step-by-step tree output to a file.
            String outputBuild= "translatedCodes.txt";
            BufferedWriter treeWriter = new BufferedWriter(new FileWriter(outputBuild));
            huffmanQueue.iterateHuffman(treeWriter);
            
            

            //Creating a stringBuilder file of the code and the values for output
            StringBuilder huffmanCodeSB = new StringBuilder();
            huffmanQueue.getHuffmanValues(huffmanQueue.first, "", huffmanCodeSB);
            treeWriter.append("List of values and translated codes: " + System.getProperty("line.separator"));
            huffmanQueue.printHuffmanPreorderTraversal(huffmanQueue.first, treeWriter,"");
            treeWriter.append(System.getProperty("line.separator"));
            treeWriter.append("Translated codes: " + System.getProperty("line.separator"));
            treeWriter.append(huffmanCodeSB);

            //Creating an array to store the letter and its correspond Hoffman value
            String huffmanArrayLetter[] = new String[lineNum];
            String huffmanArrayCode[] = new String[lineNum];
            String codeLines[] = huffmanCodeSB.toString().split("\\n");
            int counter = 0;
            for(int i=0;i<codeLines.length;i++){
                //System.out.println(codeLines[i].substring(0, 1) + " - " + codeLines[i].substring(4));
                huffmanArrayLetter[i] = codeLines[i].substring(0, 1);
                huffmanArrayCode[i] = codeLines[i].substring(4);
                huffmanArrayCode[i] = huffmanArrayCode[i].trim();
                counter++;
            }
            

            /*I cannot for the life of me figure out why it's not appending to a stringBuilder or a normal string.
                I have tried to make it a character array. I have tried to make a multidimensional string array, and
                many others. This took me about 3 hours to figure out that I can just append it directly to and output file.
                I would still like to know why this is broken though.
             */
           
            
            //Translating based on testing for equality of each letter, and taking the corresponding encoded value
            if (fileType.equals("Encode")) {
                FileReader fileReaderEncode = new FileReader(secondaryFilePath);
                BufferedReader bufferedReaderEncode = new BufferedReader(fileReaderEncode);
                String line;
                while ((line = bufferedReaderEncode.readLine()) != null) {

                    String encodedString = line;
                    writer.append("Code for " + line + ":" + System.getProperty("line.separator"));

                    for (int i = 0; i < encodedString.length(); i++) {
                        if (encodedString.substring(i, i + 1).equals(" ")) {
                            continue;
                        }
                        for (int j = 0; j < counter; j++) {
                            if (encodedString.substring(i, i + 1).toUpperCase().equals(huffmanArrayLetter[j])) {
                                writer.append(huffmanArrayCode[j].toString());
                                break;
                            }
                        }
                    }
                    writer.append(System.getProperty("line.separator"));
                }
                
            }
            
            
            //Decoding using the method and writing to a file
            if(fileType.equals("Decode")){
                FileReader fileReaderDecode = new FileReader(secondaryFilePath);
                BufferedReader bufferedReaderDecode = new BufferedReader(fileReaderDecode);
                String line;
                
                while((line = bufferedReaderDecode.readLine()) != null){
                    writer.append("Decode for " + line + ": " + System.getProperty("line.separator"));
                    writer.append(huffmanQueue.decodeHuffman(line) + System.getProperty("line.separator"));
                }
            }
            treeWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
