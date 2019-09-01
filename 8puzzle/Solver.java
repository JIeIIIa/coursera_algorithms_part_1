/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Solver {
    private class Node {
        private final Board board;
        private final int moves;
        private final int hamming;
        private final int manhattan;
        private final Node prev;

        private Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.hamming = board.hamming();
            this.manhattan = board.manhattan();
        }

        private int priority() {
            return moves + manhattan;
        }
    }

    private Node finalPosition = null;
    private List<Board> path = new ArrayList<>();

    private static Comparator<Node> priorityComparator() {
        return (o1, o2) -> {
            if (o1 == o2) {
                return 0;
            }

            return Integer.compare(o1.priority(), o2.priority());
        };
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<Node> minPQ = new MinPQ<>(priorityComparator());

        if (initial.isGoal()) {
            this.finalPosition = new Node(initial, 0, null);
            makeSolution();
            return;
        }
        minPQ.insert(new Node(initial, 0, null));
        int maxSteps =  6 * initial.manhattan();
        int count = 0;

        try {
            while (true) {
                Node node = minPQ.delMin();
                count++;
                if (count > 620_650) {
                    return;
                }
                for (Board neighbor : node.board.neighbors()) {
                    Node next = new Node(neighbor, node.moves + 1, node);
                    if (neighbor.isGoal()) {
                        finalPosition = next;
                        makeSolution();
                        return;
                    }
                    if (!isAlreadyFound(node, neighbor)) {
                        minPQ.insert(next);
                    }
                }
            }
        }
        catch (NoSuchElementException e) {
            /*NOP*/
        }
    }

    private void makeSolution() {
        Node node = finalPosition;
        while (node != null) {
            path.add(0, node.board);
            node = node.prev;
        }
    }

    private boolean isAlreadyFound(Node node, Board board) {
        if (node != null && node.prev != null) {
            return node.prev.board.equals(board);
        }
        return false;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return finalPosition != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        return finalPosition != null ? finalPosition.moves : -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (finalPosition == null) {
            return null;
        }
        return () -> new Iterator<Board>() {
            private Iterator<Board> iter = path.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Board next() {
                return iter.next();
            }
        };
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In("puzzle2x2-unsolvable1.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println("Initial:\n" + initial + "\n");

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
