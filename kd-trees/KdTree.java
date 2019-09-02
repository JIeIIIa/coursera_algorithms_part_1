/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Iterator;

public class KdTree {
    private class Node {
        private Point2D point;
        private Node left;
        private Node right;
        private int height;

        Node(Point2D point, int height) {
            this.point = point;
            this.height = height;
        }
    }

    private Node root = null;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        // for (Point2D p : set) {
        //     StdDraw.point(p.x(), p.y());
        // }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return ()->new Iterator<Point2D>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Point2D next() {
                return null;
            }
        };
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return null;
    }

    public static void main(String[] args) {

    }
}
