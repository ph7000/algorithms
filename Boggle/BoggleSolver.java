/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.HashMap;
import java.util.HashSet;

public class BoggleSolver {
    // private final TST<Integer> trie;
    // private final PatriciaSET patricia;
    // private HashSet<String> dictOfPrefixes;
    private HashMap<String, HashMap<String, TST<String>>> dictOfPrefixes;
    private HashSet<String> dict;
    private String[] dict2;
    private Integer[] dictMonadPositions = new Integer[26];
    private Integer[] dictDiadPositions = new Integer[26 * 26];
    private Integer[] dictTriadPositions = new Integer[26 * 26 * 26];

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dict2 = dictionary;
        for (int i = 0; i < dictionary.length; i++) {
            if (dictionary[i].length() > 2) {
                String firstLetter = dictionary[i].substring(0, 1);
                String secondLetter = dictionary[i].substring(1, 2);
                String thirdLetter = dictionary[i].substring(2, 3);
                int firstLetterInt = letterToNumber(firstLetter);
                int secondLetterInt = letterToNumber(secondLetter);
                int thirdLetterInt = letterToNumber(thirdLetter);

                int triadIndex = firstLetterInt * 26 * 26 + secondLetterInt * 26 + thirdLetterInt;

                if (dictTriadPositions[triadIndex] == 0) {
                    dictTriadPositions[triadIndex] = i;
                }
            }
        }


        /*
        this.trie = new TST<Integer>();
        for (int i = 0; i < dictionary.length; i++) {
            this.trie.put(dictionary[i], 0); // scoreOf(dictionary[i]));
        }

        /**/


        /*
        this.dictOfPrefixes = new HashSet<String>();
        for (int i = 0; i < dictionary.length; i++) {
            for (int j = 1; j <= dictionary[i].length(); j++) {
                this.dictOfPrefixes.add(dictionary[i].substring(0, j));
                // System.out.print(" " + dictionary[i].substring(0, j));
            }
            // this.dict.append(" " + dictionary[i]);
        }
        /**/


        /*
        this.dictOfPrefixes = new HashMap<String, HashMap<String, TST<String>>>();
        for (int i = 0; i < dictionary.length; i++) {
            String firstChar = dictionary[i].substring(0, 1);
            if (dictionary[i].length() > 1) {
                String firstTwoChar = dictionary[i].substring(0, 2);
                if (!this.dictOfPrefixes.containsKey(firstChar)) {
                    this.dictOfPrefixes.put(firstChar, new HashMap<String, TST<String>>());
                }
                if (!this.dictOfPrefixes.get(firstChar).containsKey(firstTwoChar)) {
                    this.dictOfPrefixes.get(firstChar).put(firstTwoChar, new TST<String>());
                }
                TST<String> trie = this.dictOfPrefixes.get(firstChar).get(firstTwoChar);
                // System.out.println(trie.toString());
                // trie.add(dictionary[i]);
                for (int j = 1; j <= dictionary[i].length(); j++) {
                    trie.put(dictionary[i].substring(0, j), "");
                    // System.out.print(" " + dictionary[i].substring(0, j));
                }
            }
        }

        this.dict = new HashSet<String>();
        // Collections.addAll(this.dict, dictionary);
        /**/


        /*
        this.patricia = new PatriciaSET();
        for (int i = 0; i < dictionary.length; i++) {
            this.patricia.add(dictionary[i]);
        }

        /**/
    }

    private int letterToNumber(String letter) {
        switch (letter) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            case "E":
                return 4;
            case "F":
                return 5;
            case "G":
                return 6;
            case "H":
                return 7;
            case "I":
                return 8;
            case "J":
                return 9;
            case "K":
                return 10;
            case "L":
                return 11;
            case "M":
                return 12;
            case "N":
                return 13;
            case "O":
                return 14;
            case "P":
                return 15;
            case "Q":
                return 16;
            case "R":
                return 17;
            case "S":
                return 18;
            case "T":
                return 19;
            case "U":
                return 20;
            case "V":
                return 21;
            case "W":
                return 22;
            case "X":
                return 23;
            case "Y":
                return 24;
            case "Z":
                return 25;
        }
        return 0;
    }


    private void initiateNextRecursion(SET<Integer> tilesSoFar, int newStartingRow,
                                       int newStartingCol, String charsSoFar, BoggleBoard board,
                                       SET<String> setOfValidWords) {
        SET<Integer> tilesSoFar2 = new SET<Integer>(tilesSoFar);
        String charsSoFar2 = charsSoFar + board.getLetter(newStartingRow, newStartingCol);
        if (charsSoFar2.endsWith("Q")) {
            charsSoFar2 += "U";
        }
        tilesSoFar2.add((newStartingRow) * 10 + newStartingCol);

        // System.out.println(charsSoFar2.charAt(0));
        if (this.dictOfPrefixes.get(charsSoFar2.substring(0, 1))
                               .containsKey(charsSoFar2.substring(0, 2))) {
            if (this.dictOfPrefixes.get(charsSoFar2.substring(0, 1))
                                   .get(charsSoFar2.substring(0, 2))
                                   .contains(charsSoFar2)) {
                // if (this.trie.keysWithPrefix(charsSoFar2).iterator().hasNext()) {
                if (this.dict.contains(charsSoFar2)) {
                    if ((charsSoFar2.length() > 2) && (!tilesSoFar.contains(
                            (newStartingRow) * 10 + newStartingCol))) {
                        setOfValidWords.add(charsSoFar2);
                    }
                }
                if (!tilesSoFar.contains((newStartingRow) * 10 + newStartingCol)) {
                    recurseThroughBoard(board, setOfValidWords, newStartingRow,
                                        newStartingCol, charsSoFar2, tilesSoFar2);
                }
            }
        }
    }

    private SET<String> findWordsFromEachTile(BoggleBoard board) {
        SET<String> setOfValidWords = new SET<String>();
        int numRows = board.rows();
        int numCols = board.cols();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                String charsSoFar = "" + board.getLetter(i, j);
                if (charsSoFar.endsWith("Q")) {
                    charsSoFar += "U";
                }
                SET<Integer> tilesSoFar = new SET<Integer>();
                tilesSoFar.add(i * 10 + j);
                recurseThroughBoard(board, setOfValidWords, i, j, charsSoFar, tilesSoFar);
            }
        }
        return setOfValidWords;
    }

    private void recurseThroughBoard(BoggleBoard board, SET<String> setOfValidWords,
                                     int startingRow, int startingCol, String charsSoFar,
                                     SET<Integer> tilesSoFar) {
        int numRows = board.rows();
        int numCols = board.cols();
        if (startingCol < numCols - 1) {
            initiateNextRecursion(tilesSoFar, startingRow, startingCol + 1, charsSoFar, board,
                                  setOfValidWords);
            if (startingRow < numRows - 1) {
                initiateNextRecursion(tilesSoFar, startingRow + 1, startingCol + 1, charsSoFar,
                                      board, setOfValidWords);
            }
            if (startingRow > 0) {
                initiateNextRecursion(tilesSoFar, startingRow - 1, startingCol + 1, charsSoFar,
                                      board, setOfValidWords);
            }
        }
        if (startingCol > 0) {
            initiateNextRecursion(tilesSoFar, startingRow, startingCol - 1, charsSoFar,
                                  board, setOfValidWords);
            if (startingRow < numRows - 1) {
                initiateNextRecursion(tilesSoFar, startingRow + 1, startingCol - 1, charsSoFar,
                                      board, setOfValidWords);
            }
            if (startingRow > 0) {
                initiateNextRecursion(tilesSoFar, startingRow - 1, startingCol - 1, charsSoFar,
                                      board, setOfValidWords);
            }
        }
        if (startingRow > 0) {
            initiateNextRecursion(tilesSoFar, startingRow - 1, startingCol, charsSoFar,
                                  board, setOfValidWords);
        }
        if (startingRow < numRows - 1) {
            initiateNextRecursion(tilesSoFar, startingRow + 1, startingCol, charsSoFar,
                                  board, setOfValidWords);
        }
    }

    /**/

    /*

    private void initiateNextRecursion(int newStartingRow,
                                       int newStartingCol, String charsSoFar, BoggleBoard board,
                                       SET<String> setOfValidWords) {
        String charsSoFar2 = charsSoFar + board.getLetter(newStartingRow, newStartingCol);
        if (charsSoFar2.endsWith("Q")) {
            charsSoFar2 += "U";
        }
        if (trie.keysWithPrefix(charsSoFar2).iterator().hasNext()) {
            if (trie.contains(charsSoFar2)) {
                if ((charsSoFar2.length() > 2)) {
                    setOfValidWords.add(charsSoFar2);
                }
            }
            recurseThroughBoard(board, setOfValidWords, newStartingRow, newStartingCol,
                                charsSoFar2);
        }
    }

    private SET<String> findWordsFromEachTile(BoggleBoard board) {
        SET<String> setOfValidWords = new SET<String>();
        int numRows = board.rows();
        int numCols = board.cols();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                String charsSoFar = "" + board.getLetter(i, j);
                if (charsSoFar.endsWith("Q")) {
                    charsSoFar += "U";
                }
                SET<Integer> tilesSoFar = new SET<Integer>();
                tilesSoFar.add(i * 10 + j);
                recurseThroughBoard(board, setOfValidWords, i, j, charsSoFar);
            }
        }
        return setOfValidWords;
    }

    private void recurseThroughBoard(BoggleBoard board, SET<String> setOfValidWords,
                                     int startingRow, int startingCol, String charsSoFar) {
        int numRows = board.rows();
        int numCols = board.cols();
        if (startingCol < numCols - 1) {
            initiateNextRecursion(startingRow, startingCol + 1, charsSoFar, board,
                                  setOfValidWords);
            if (startingRow < numRows - 1) {
                initiateNextRecursion(startingRow + 1, startingCol + 1, charsSoFar,
                                      board, setOfValidWords);
            }
            if (startingRow > 0) {
                initiateNextRecursion(startingRow - 1, startingCol + 1, charsSoFar,
                                      board, setOfValidWords);
            }
        }
        if (startingCol > 0) {
            initiateNextRecursion(startingRow, startingCol - 1, charsSoFar,
                                  board, setOfValidWords);
            if (startingRow < numRows - 1) {
                initiateNextRecursion(startingRow + 1, startingCol - 1, charsSoFar,
                                      board, setOfValidWords);
            }
            if (startingRow > 0) {
                initiateNextRecursion(startingRow - 1, startingCol - 1, charsSoFar,
                                      board, setOfValidWords);
            }
        }
        if (startingRow > 0) {
            initiateNextRecursion(startingRow - 1, startingCol, charsSoFar,
                                  board, setOfValidWords);
        }
        if (startingRow < numRows - 1) {
            initiateNextRecursion(startingRow + 1, startingCol, charsSoFar,
                                  board, setOfValidWords);
        }
    }

     */

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        // return new Queue<String>();
        return findWordsFromEachTile(board);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        // if (this.trie.contains(word)) {
        if (this.dict.contains(word)) {
            int count = word.length();
            if (count < 3) return 0;
            else if (count < 5) return 1;
            else if (count < 6) return 2;
            else if (count < 7) return 3;
            else if (count < 8) return 5;
            else return 11;
        }
        else return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
