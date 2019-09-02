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

        Node(Point2D point) {
            this.point = point;
        }

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
        return size == 0;
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
        Node node = new Node(p);
        if (root == null) {
            root = node;
            size++;
        }
        else {
            if (insert(p, root)) {
                size++;
            }
        }
    }

    private boolean insert(Point2D p, Node parent) {
        if (p.equals(parent.point)) {
            return false;
        }
        if (compare(p, parent) <= 0) {
            if (parent.left == null) {
                parent.left = new Node(p, parent.height + 1);
                return true;
            }
            else {
                return insert(p, parent.left);
            }
        }
        else {
            if (parent.right == null) {
                parent.right = new Node(p, parent.height + 1);
                return true;
            }
            else {
                return insert(p, parent.right);
            }
        }
    }

    private int compare(Point2D p, Node node) {
        if (node.height % 2 == 0) {
            return Double.compare(p.x(), node.point.x());
        }
        else {
            return Double.compare(p.y(), node.point.y());
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return contains(p, root);
    }

    private boolean contains(Point2D p, Node node) {
        if (node == null) {
            return false;
        }
        else if (p.equals(node.point)) {
            return true;
        }
        else if (compare(p, node) <= 0) {
            return contains(p, node.left);
        }
        else {
            return contains(p, node.right);
        }
    }

    // draw all points to standard draw
    public void draw() {
        // for (Point2D p : set) {
        //     StdDraw.point(p.x(), p.y());
        // }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return () -> new Iterator<Point2D>() {
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
