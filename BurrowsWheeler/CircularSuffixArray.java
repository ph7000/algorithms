/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Merge;

public class CircularSuffixArray {

    private int len;
    private int[] suffixIndices;

    private class SuffixNode implements Comparable<SuffixNode> {
        private int originalPosition;
        private String suffix;

        public SuffixNode(String suff, int origP) {
            this.suffix = suff;
            this.originalPosition = origP;
        }

        public int getOriginalPosition() {
            return this.originalPosition;
        }

        public String getSuffix() {
            return this.suffix;
        }

        public int compareTo(SuffixNode other) {
            return other.getSuffix().compareTo(this.suffix);
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        this.len = s.length();

        SuffixNode[] listOfStrings = new SuffixNode[this.len];

        for (int i = 0; i < this.len; i++) {
            if (i == 0) {
                listOfStrings[i] = new SuffixNode(s, i);
            }
            else {
                String newSuffix = listOfStrings[i - 1].getSuffix().substring(1) + listOfStrings[i
                        - 1].getSuffix().charAt(
                        0);
                listOfStrings[i] = new SuffixNode(newSuffix, i);
            }
        }

        Merge.sort(listOfStrings);

        this.suffixIndices = new int[this.len];

        for (int i = 0; i < this.len; i++) {
            this.suffixIndices[this.len - 1 - i] = listOfStrings[i].getOriginalPosition();
            // System.out.println(listOfStrings[i].getOriginalPosition());
        }
    }

    // length of s
    public int length() {
        return this.len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= this.len) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        return this.suffixIndices[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray paulsArray = new CircularSuffixArray("ABRACADABRA!");
        System.out.println("Length: " + paulsArray.length());
        
        for (int i = 0; i < paulsArray.length(); i++) {
            System.out.println(paulsArray.index(i));
        }
    }
}
