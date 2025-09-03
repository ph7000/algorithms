/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int num;
    private double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        num = trials;
        thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            int j = 0;
            while (!perc.percolates()) {
                int rand1 = StdRandom.uniformInt(1, n + 1);
                int rand2 = StdRandom.uniformInt(1, n + 1);
                while (perc.isOpen(rand1, rand2)) {
                    rand1 = StdRandom.uniformInt(1, n + 1);
                    rand2 = StdRandom.uniformInt(1, n + 1);
                }
                perc.open(rand1, rand2);
            }
            int numOpen = perc.numberOfOpenSites();
            thresholds[i] = ((double) numOpen) / ((double) (n * n));
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt((double) num);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt((double) num);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]),
                                                      Integer.parseInt(args[1]));
        System.out.println("mean \t\t\t= " + stats.mean());
        System.out.println("stddev \t\t\t= " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", "
                                   + stats.confidenceHi() + "]");
    }

}
