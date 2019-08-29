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

    private class Item {
        private final Point point;
        private final double slopeTo;
        private final int thatCompareTo;

        private Item(final Point point, final double slopeTo, final int thatCompareTo) {
            this.point = point;
            this.slopeTo = slopeTo;
            this.thatCompareTo = thatCompareTo;
        }
    }

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


        // checkDuplicates(points);

        Item[] items = new Item[points.length];

        // Point[] aux = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                items[j] = new Item(points[j], points[i].slopeTo(points[j]),
                                    points[i].compareTo(points[j]));
            }
            sort(items);

            int lo = 0;
            int hi = 0;
            double slope = items[0].slopeTo;
            for (int j = 1; j < items.length; j++) {

                if (points[i] == items[j].point) {
                    slope = items[j].slopeTo;
                }
                else if (items[j].thatCompareTo == 0) {
                    throw new IllegalArgumentException();
                }
                else if (Double.compare(slope, items[j].slopeTo) == 0) {
                    hi = j;
                }
                else {
                    if (hi - lo >= 2 && items[lo].thatCompareTo < 0) {
                        segments.add(new LineSegment(points[i], items[hi].point));
                    }
                    lo = j;
                    hi = j;
                    slope = items[j].slopeTo;
                }
            }
            if (hi - lo >= 2 && items[lo].thatCompareTo < 0) {
                segments.add(new LineSegment(points[i], items[hi].point));
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

    private void sort(Item[] a) {
        Item[] aux = new Item[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    private void sort(Item[] a, Item[] aux, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    private void merge(Item[] a, Item[] aux, int lo, int mid, int hi) {
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
            else {
                int compare = Double.compare(aux[i].slopeTo, aux[j].slopeTo);
                if (compare < 0 ||
                        (compare == 0 && aux[i].point.compareTo(aux[j].point) < 0)) {
                    a[k] = aux[i++];
                }
                else {
                    a[k] = aux[j++];
                }
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
