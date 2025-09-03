/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Iterator;
import java.util.LinkedList;

// Given a text file in which sequences of the same character occur near each other many times,
// convert it to a text file in which certain characters appear much more frequently than others.
public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> encodeList = new LinkedList<>();
        for (int i = 0; i < 256; i++) {
            encodeList.addLast((char) i);
            // if (i > 40 && i < 50) {
            // System.out.println((char) i);
            // }
        }

        // System.out.println((int) 'a');

        while (!BinaryStdIn.isEmpty()) {
            char newChar = BinaryStdIn.readChar();

            boolean foundCharYet = false;
            Iterator<Character> iterator = encodeList.iterator();

            int counter = 0;
            while (iterator.hasNext() && !foundCharYet) {
                char searchChar = iterator.next();

                if (searchChar == newChar) {
                    BinaryStdOut.write((char) counter);
                    // BinaryStdOut.flush();
                    // StdOut.print(counter + " ");
                    foundCharYet = true;
                    counter -= 1;
                }

                counter += 1;
            }

            encodeList.remove(counter);
            encodeList.addFirst(newChar);
        }

        BinaryStdOut.close();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> encodeList = new LinkedList<>();
        for (int i = 0; i < 256; i++) {
            encodeList.addLast((char) i);
            // if (i > 40 && i < 50) {
            // System.out.println((char) i);
            // }
        }

        // System.out.println((int) 'a');

        while (!BinaryStdIn.isEmpty()) {
            int newCharPosition = BinaryStdIn.readChar();

            // System.out.println(newCharPosition + " " + (char) newCharPosition);

            boolean foundCharYet = false;
            Iterator<Character> iterator = encodeList.iterator();

            char searchChar = (char) newCharPosition;

            int counter = 0;
            while (iterator.hasNext() && !foundCharYet) {
                searchChar = iterator.next();

                if (counter == newCharPosition) {
                    BinaryStdOut.write(searchChar);
                    // BinaryStdOut.flush();
                    // StdOut.print(counter + " ");
                    foundCharYet = true;
                    counter -= 1;
                }

                counter += 1;
            }

            encodeList.remove(counter);
            encodeList.addFirst(searchChar);
        }

        BinaryStdOut.close();

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
    }

}
