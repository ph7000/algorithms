/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.LSD;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String thingToTransform = BinaryStdIn.readString(); // "ABRACADABRA!"; //
        // StdOut.print(thingToTransform);
        CircularSuffixArray suffixArray = new CircularSuffixArray(thingToTransform);

        for (int i = 0; i < suffixArray.length(); i++) {
            // StdOut.println(i + " is original Index " + suffixArray.index(i) + ". ");
            if (suffixArray.index(i) == 0) {
                BinaryStdOut.write(i);
            }
        }

        for (int i = 0; i < suffixArray.length(); i++) {
            int charAt = suffixArray.index(i) - 1;
            if (charAt < 0) {
                charAt = suffixArray.length() - 1;
            }
            BinaryStdOut.write(thingToTransform.charAt(charAt));
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();

        String thingToInvert = BinaryStdIn.readString();

        int stringLength = thingToInvert.length();

        int[] next = new int[stringLength];
        String[] sortedT = new String[stringLength];

        HashMap<Character, Queue<Integer>> firstCharToLastPos
                = new HashMap<Character, Queue<Integer>>();

        for (int i = 0; i < stringLength; i++) {
            sortedT[i] = String.valueOf(thingToInvert.charAt(i));

            if (!firstCharToLastPos.containsKey(thingToInvert.charAt(i))) {
                firstCharToLastPos.put(thingToInvert.charAt(i), new Queue<Integer>());
            }

            Queue<Integer> queue = firstCharToLastPos.get(thingToInvert.charAt(i));
            queue.enqueue(i);
        }

        LSD.sort(sortedT, 1);

        for (int i = 0; i < stringLength; i++) {
            char letterToFindNext = sortedT[i].charAt(0);

            Queue<Integer> queue = firstCharToLastPos.get(letterToFindNext);
            next[i] = queue.dequeue();

        }

        int[] paulsNext = new int[stringLength];

        int nextIndex = first;

        for (int i = 0; i < stringLength; i++) {
            // StdOut.println(next[i]);
            paulsNext[i] = nextIndex;

            nextIndex = next[nextIndex];
        }

        for (int i = 0; i < stringLength; i++) {
            BinaryStdOut.write(sortedT[paulsNext[i]]);
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
