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

public class FastCollinearPoints {

    private final List<LineSegment> segments = new LinkedList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        checkDuplicates(points);

        Point[] aux = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                aux[j] = points[j];
            }
            double[] slopes = calcSlopes(aux, points[i]);
            // System.out.println("Before sort: " + Arrays.toString(slopes));
            sort(aux, points[i]);
            // slopes = calcSlopes(aux, points[i]);
            // StdOut.println("After sort : " + Arrays.toString(slopes));

            int lo = 0;
            int hi = 0;
            double slope = points[i].slopeTo(aux[0]);
            for (int j = 1; j < aux.length; j++) {
                double slopeJ = points[i].slopeTo(aux[j]);
                if (points[i].compareTo(aux[j]) == 0) {
                    // hi = j;
                    slope = slopeJ;
                }
                else if (Double.compare(slope, slopeJ) == 0) {
                    hi = j;
                }
                else {
                    if (hi - lo >= 2 && points[i].compareTo(aux[lo]) < 0) {
                        segments.add(new LineSegment(points[i], aux[hi]));
                    }
                    lo = j;
                    hi = j;
                    slope = slopeJ;
                }
            }
            if (hi - lo >= 2 &&  points[i].compareTo(aux[lo]) < 0) {
                segments.add(new LineSegment(points[i], aux[hi]));
            }
        }
    }

    private double[] calcSlopes(Point[] points, Point point) {
        double[] slopes = new double[points.length];
        for (int j = 0; j < points.length; j++) {
            slopes[j] = point.slopeTo(points[j]);
        }

        return slopes;
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

    private void sort(Point[] a, Point p) {
        Point[] aux = new Point[a.length];
        sort(a, aux, p, 0, a.length - 1);
    }

    private void sort(Point[] a, Point[] aux, Point p, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, p, lo, mid);
        sort(a, aux, p, mid + 1, hi);
        merge(a, aux, p, lo, mid, hi);
    }

    private void merge(Point[] a, Point[] aux, Point p, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            }
            else if (j > hi) {
                a[k] = aux[i++];
            }
            else if (p.slopeOrder().compare(aux[i], aux[j]) < 0 ||
                    (p.slopeOrder().compare(aux[i], aux[j]) == 0
                            && aux[i].compareTo(aux[j]) < 0)) {
                a[k] = aux[i++];
            }
            else {
                a[k] = aux[j++];
            }
        }
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        // StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
