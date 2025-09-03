/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Board {

    private int n;
    private int[][] board;

    private class LList {
        private Node last = null;

        private class Node {
            Board item;
            Node next;
        }

        // construct an empty linked list
        public LList() {
        }

        // add the item to the back
        public void addLast(Board item) {
            Node oldlast = last;
            last = new Node();
            last.item = item;
            last.next = oldlast;
        }

        // return an iterator over items in order from front to back
        public Iterable<Board> iterator() {
            return new ListIterator();
        }

        private class ListIterator implements Iterable<Board> {

            private Node current = last;

            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    public boolean hasNext() {
                        return current != null;
                    }

                    public Board next() {
                        Board item = current.item;
                        current = current.next;
                        return item;

                    }
                };
            }
        }
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][n];
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                board[row][col] = tiles[row][col];
    }

    // string representation of this board
    public String toString() {
        String boardString = Integer.toString(n) + "\n";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                boardString += String.format("%2d ", board[i][j]);
            }
            boardString += "\n";
        }
        return boardString;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingNumber = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                int value = board[i][j];

                if (value != (n * i + j + 1)) {
                    if (value != 0) {
                        hammingNumber += 1;
                    }
                }

            }
        }
        return hammingNumber;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                int value = board[i][j];
                int colDist = Math.abs((j) - ((value - 1) % n));
                int rowDist = Math.abs(i - ((value - 1 - ((value - 1) % n)) / n));
                if (value == 0) {
                    colDist = 0;
                    rowDist = 0;
                }

                manhattanDistance += colDist + rowDist;
                // StdOut.println(value + " " + colDist + " " + rowDist);

            }
        }

        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // self check
        if (this == y)
            return true;
        // null check
        if (y == null)
            return false;
        // type check and cast
        if (getClass() != y.getClass())
            return false;
        Board b = (Board) y;
        // field comparison
        return Objects.equals(n, b.n)
                && Arrays.deepEquals(board, b.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LList neighborList = new LList();
        int zeroCol = 0;
        int zeroRow = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board[row][col] == 0) {
                    zeroCol = col;
                    zeroRow = row;
                }
            }
        }
        if (zeroCol != 0) {
            Board neighbor1 = new Board(board);
            int exch = neighbor1.board[zeroRow][zeroCol];
            neighbor1.board[zeroRow][zeroCol] = neighbor1.board[zeroRow][zeroCol - 1];
            neighbor1.board[zeroRow][zeroCol - 1] = exch;
            neighborList.addLast(neighbor1);
        }
        if (zeroCol != n - 1) {
            Board neighbor2 = new Board(board);
            int exch = neighbor2.board[zeroRow][zeroCol];
            neighbor2.board[zeroRow][zeroCol] = neighbor2.board[zeroRow][zeroCol + 1];
            neighbor2.board[zeroRow][zeroCol + 1] = exch;
            neighborList.addLast(neighbor2);
        }
        if (zeroRow != 0) {
            Board neighbor3 = new Board(board);
            int exch = neighbor3.board[zeroRow][zeroCol];
            neighbor3.board[zeroRow][zeroCol] = neighbor3.board[zeroRow - 1][zeroCol];
            neighbor3.board[zeroRow - 1][zeroCol] = exch;
            neighborList.addLast(neighbor3);
        }
        if (zeroRow != n - 1) {
            Board neighbor4 = new Board(board);
            int exch = neighbor4.board[zeroRow][zeroCol];
            neighbor4.board[zeroRow][zeroCol] = neighbor4.board[zeroRow + 1][zeroCol];
            neighbor4.board[zeroRow + 1][zeroCol] = exch;
            neighborList.addLast(neighbor4);
        }
        return neighborList.iterator();
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] switchedTiles = new int[n][n];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                switchedTiles[i][j] = board[i][j];
            }
        }
        int switchRow = 0;
        int switchCol = 0;
        int switchRow2 = 0;
        int switchCol2 = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (switchedTiles[j][i] != 0) {
                    if ((switchRow == 0) && (switchCol == 0)) {
                        switchRow = i;
                        switchCol = j;
                    }
                    else {
                        switchRow2 = i;
                        switchCol2 = j;
                    }
                }
            }
        }
        int switchValue = switchedTiles[switchCol][switchRow];
        int switchValue2 = switchedTiles[switchCol2][switchRow2];
        switchedTiles[switchCol2][switchRow2] = switchValue;
        switchedTiles[switchCol][switchRow] = switchValue2;
        Board twinBoard = new Board(switchedTiles);
        return twinBoard;
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        StdOut.println(initial.twin());
        for (Board s : initial.neighbors())
            StdOut.println(s.toString());
        StdOut.println(initial.isGoal());
        int[][] secondBoard = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        StdOut.println(initial.equals(new Board(secondBoard)));
    }

}
