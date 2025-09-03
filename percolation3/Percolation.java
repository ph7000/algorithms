/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF UF;
    private int numOpenSites = 0;
    private int numGrid;
    private boolean[][] grid;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        numGrid = n;
        grid = new boolean[n][n];
        UF = new WeightedQuickUnionUF(n * n + 2);
        for (int i = 1; i <= n; i++) {
            UF.union(0, i);
            UF.union(n * n + 1, n * n + 1 - i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > numGrid || row <= 0 || col > numGrid || col <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        if (!isOpen(row, col)) {
            numOpenSites += 1;
        }
        grid[col - 1][row - 1] = true;
        int num = (row - 1) * numGrid + (col);
        if (col > 1) {
            int left = num - 1;
            if (isOpen(row, col - 1)) {
                UF.union(left, num);
            }
        }
        if (col < numGrid) {
            int right = num + 1;
            if (isOpen(row, col + 1)) {
                UF.union(right, num);
            }
        }
        if (row > 1) {
            int above = num - numGrid;
            if (isOpen(row - 1, col)) {
                UF.union(above, num);
            }
        }
        if (row < numGrid) {
            int below = num + numGrid;
            if (isOpen(row + 1, col)) {
                UF.union(below, num);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > numGrid || row <= 0 || col > numGrid || col <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        return grid[col - 1][row - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > numGrid || row <= 0 || col > numGrid || col <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        int num = (row - 1) * numGrid + (col);
        return (UF.find(num) == UF.find(0) && isOpen(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return UF.find(0) == UF.find(numGrid * numGrid + 1);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
