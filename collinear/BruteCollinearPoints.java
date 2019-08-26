/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> segments = new LinkedList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        checkDuplicates(points);

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int t = k + 1; t < points.length; t++) {
                        if (examine(points[i], points[j], points[k], points[t])) {
                            segments.add(
                                    makeLineSegment(points[i], points[j], points[k], points[t]));
                        }
                    }
                }
            }
        }
    }

    private void checkDuplicates(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private boolean examine(Point p, Point q, Point r, Point s) {
        double slope = p.slopeTo(r);
        return Double.compare(p.slopeTo(q), slope) == 0 && Double.compare(slope, p.slopeTo(s)) == 0;
    }

    private LineSegment makeLineSegment(Point... points) {
        Point min = null;
        Point max = null;
        for (Point point : points) {
            if (min == null || point.compareTo(min) < 0) {
                min = point;
            }
            if (max == null || point.compareTo(max) > 0) {
                max = point;
            }
        }
        return new LineSegment(min, max);
    }


    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("input8.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
