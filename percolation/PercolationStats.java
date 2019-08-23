/* *****************************************************************************
 *  Name:              Oleksii Onishchenko
 *  Coursera User ID:  9b3122cdaa15288f1eb7320f1480fa06
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        // do calculation
        double[] results = new double[trials];
        int count = n * n;
        for (int i = 0; i < results.length; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int p = StdRandom.uniform(count);
                int row = p / n + 1;
                int col = p % n + 1;
                percolation.open(row, col);
            }
            results[i] = percolation.numberOfOpenSites() / (double) count;
        }

        // make answer
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceLo = (mean - CONFIDENCE_95 * stddev / Math.sqrt(results.length));
        confidenceHi = (mean + CONFIDENCE_95 * stddev / Math.sqrt(results.length));

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]),
                                                      Integer.parseInt(args[1]));
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = ["
                               + stats.confidenceLo() +
                               ", "
                               + stats.confidenceHi()
                               + "]");
    }
}
