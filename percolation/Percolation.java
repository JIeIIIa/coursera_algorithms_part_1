
/* *****************************************************************************
 *  Name:              Oleksii Onishchenko
 *  Coursera User ID:  9b3122cdaa15288f1eb7320f1480fa06
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int size;
    private final boolean[][] sites;
    private final int top;
    private final int bottom;
    private final WeightedQuickUnionUF fullWQUUF;
    private final WeightedQuickUnionUF percolateWQUUF;
    private int numberOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.size = n;
        bottom = n * n + 1;
        top = 0;
        this.fullWQUUF = new WeightedQuickUnionUF(n * n + 1);
        this.percolateWQUUF = new WeightedQuickUnionUF(n * n + 2);
        this.sites = new boolean[n + 1][n + 1]; // n+1 to use indeces from 1 to n
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateWithException(row, col);

        if (sites[row][col]) {
            return;
        }
        sites[row][col] = true;

        unionIfPosiible(row, col, 1, 0);
        unionIfPosiible(row, col, -1, 0);
        unionIfPosiible(row, col, 0, 1);
        unionIfPosiible(row, col, 0, -1);

        int p = toUnidemention(row, col) + 1;
        if (row == 1) {
            fullWQUUF.union(top, p);
            percolateWQUUF.union(top, p);
        }
        if (row == size) {
            percolateWQUUF.union(bottom, p);
        }

        numberOfOpenSites++;
    }

    private void unionIfPosiible(int row, int col, int dRow, int dCol) {
        if (!validate(row + dRow, col + dCol)) {
            return;
        }
        int p = toUnidemention(row, col) + 1;

        int t = toUnidemention(row + dRow, col + dCol) + 1;
        if (p != t && sites[row + dRow][col + dCol]) {
            fullWQUUF.union(p, t);
            percolateWQUUF.union(p, t);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateWithException(row, col);
        return sites[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateWithException(row, col);

        if (sites[row][col] &&
                // weightedQuickUnionUF.connected(toUnidemention(aRow, aCol) + 1, bottom) &&
                fullWQUUF.connected(toUnidemention(row, col) + 1, top)) {
            return true;
        }

        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolateWQUUF.connected(bottom, top);
    }

    private int toUnidemention(int row, int col) {
        return (row - 1) * size + col - 1;
    }

    private boolean validate(int row, int col) {
        return 0 < row && row <= size && 0 < col && col <= size;
    }

    private void validateWithException(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException();
        }
    }

    private void print() {
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                System.out.print(String.format("%8s", sites[i][j]));
            }
            System.out.println();
        }
    }

    // test client (optional)
    public static void main(String[] args) {

        Percolation percolation = new Percolation(6);
        percolation.open(1, 6);
        percolation.open(2, 6);
        percolation.open(3, 6);
        percolation.print();
        System.out.println(percolation.isFull(3, 6));

    }
}
