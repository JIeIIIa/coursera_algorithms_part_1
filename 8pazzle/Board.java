/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;

public class Board {
    private final int[][] tiles;
    private final int dimension;
    private final int[] twinElements;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = deepCopy(tiles);
        this.dimension = tiles.length;
        this.twinElements = createTwinElements();
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder(tiles.length + "\n");

        for (int[] row : tiles) {
            for (int cell : row) {
                sb
                        .append(cell < 10 ? " " : "")
                        .append(cell)
                        // .append(cell < 100 ? " " : "");
                        .append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < dimension * dimension; i++) {
            int cell = tiles[i / dimension][i % dimension];
            if (cell != 0 && cell != i + 1) {
                count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int result = 0;
        for (int i = 0; i < dimension * dimension; i++) {
            int row = i / dimension;
            int col = i % dimension;

            int cell = tiles[row][col] - 1;
            if (cell != -1) {
                result += Math.abs(row - cell / dimension) + Math.abs(col - cell % dimension);
            }
        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension * dimension - 1; i++) {
            if (tiles[i / dimension][i % dimension] != i + 1) {
                return false;
            }
        }

        return true;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board board = (Board) y;
        return Arrays.deepEquals(tiles, board.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        final int zeroPos = findZero();
        final int row = zeroPos / dimension;
        final int col = zeroPos % dimension;

        int size = 4;
        if (row == 0 || row == dimension - 1) {
            size--;
        }
        if (col == 0 || col == dimension - 1) {
            size--;
        }
        final Board[] boards = new Board[size];
        for (int i = 0; i < boards.length; i++) {
            boards[i] = new Board(deepCopy(tiles));
        }

        int p = 0;
        if (row != 0) {
            boards[p].tiles[row][col] = tiles[row - 1][col];
            boards[p].tiles[row - 1][col] = 0;
            p++;
        }
        if (row != dimension - 1) {
            boards[p].tiles[row][col] = tiles[row + 1][col];
            boards[p].tiles[row + 1][col] = 0;
            p++;
        }
        if (col != 0) {
            boards[p].tiles[row][col] = tiles[row][col - 1];
            boards[p].tiles[row][col - 1] = 0;
            p++;
        }
        if (col != dimension - 1) {
            boards[p].tiles[row][col] = tiles[row][col + 1];
            boards[p].tiles[row][col + 1] = 0;
        }

        return () -> new Iterator<Board>() {
            private int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < boards.length;
            }

            @Override
            public Board next() {
                return boards[pos++];
            }
        };
    }

    private int findZero() {
        for (int i = 0; i < dimension * dimension; i++) {
            if (tiles[i / dimension][i % dimension] == 0) {
                return i;
            }
        }
        return -1;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(deepCopy(tiles));
        swap(twin.tiles, twinElements[0], twinElements[1]);
        if (this.equals(twin)) {
            throw new IllegalArgumentException("Wrong twin");
        }

        return twin;
    }

    private int[] createTwinElements() {
        int i;
        int j;
        int size = dimension * dimension;
        do {

            i = StdRandom.uniform(size);
            Direction d = Direction.rnd();
            j = (i / dimension + d.row) * dimension +
                    (i % dimension + d.col + dimension) % dimension;
        }
        while (j < 0 || j >= size
                || tiles[i / dimension][i % dimension] == 0
                || tiles[j / dimension][j % dimension] == 0);


        return new int[] { i, j };
    }

    private void swap(int[][] arr, int a, int b) {
        int tmp = arr[a / dimension][a % dimension];
        arr[a / dimension][a % dimension] = arr[b / dimension][b % dimension];
        arr[b / dimension][b % dimension] = tmp;
    }

    private static int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In("puzzle23.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);

        StdOut.println(initial);
        StdOut.println("dimension = " + initial.dimension());
        StdOut.println("hamming = " + initial.hamming());
        StdOut.println("manhattan = " + initial.manhattan());
        StdOut.println("isGoal = " + initial.isGoal());

        StdOut.println("\n\nNeightbors:");
        for (Board neighbor : initial.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("Is equal to initial:   " + neighbor.equals(initial) + "\n");
        }

        StdOut.println("\n\nInitial:");
        StdOut.println(initial);
        StdOut.println("Twin: \n" + initial.twin());
        for (int i = 0; i < 1000; i++) {
            StdOut.println(initial.twin());
        }
    }
}

enum Direction {
    TOP(-1, 0),
    RIGHT(0, 1),
    BOTTOM(1, 0),
    LEFT(0, -1);

    public int row;
    public int col;

    Direction(int col, int row) {
        this.row = row;
        this.col = col;
    }

    static Direction rnd() {
        int d = StdRandom.uniform(4);
        switch (d) {
            case 0:
                return TOP;
            case 1:
                return RIGHT;
            case 2:
                return BOTTOM;
            case 3:
                return LEFT;
            default:
                throw new IllegalArgumentException();
        }
    }
}
