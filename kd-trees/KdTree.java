
/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private class Node {
        private final Point2D point;
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
        else {
            return contains(p, getNext(p, node));
        }
    }

    private Node getNext(Point2D p, Node node) {
        if (node == null) {
            return null;
        }
        else if (compare(p, node) <= 0) {
            return node.left;
        }
        else {
            return node.right;
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV rect) {
        if (node == null) {
            return;
        }
        Point2D p = node.point;

        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(p.x(), p.y());

        StdDraw.setPenRadius(0.01);
        if (node.height % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
            draw(node.left, new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax()));
            draw(node.right, new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        }
        else {

            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
            draw(node.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y()));
            draw(node.right, new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax()));
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        SET<Point2D> set = new SET<>();
        range(root, rect, set);

        return set;
    }

    private void range(Node node, RectHV rect, SET<Point2D> set) {
        if (node == null) {
            return;
        }
        Point2D point = node.point;

        if (rect.contains(point)) {
            set.add(point);
        }

        if (node.height % 2 == 0) {
            if (point.x() >= rect.xmin()) {
                range(node.left, rect, set);
            }
            if (point.x() <= rect.xmax()) {
                range(node.right, rect, set);
            }
        }
        else {
            if (point.y() >= rect.ymin()) {
                range(node.left, rect, set);
            }
            if (point.y() <= rect.ymax()) {
                range(node.right, rect, set);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return nearest(p, root, null, new RectHV(0, 0, 1, 1));
    }

    private Point2D nearest(Point2D target, Node node, Point2D champion, RectHV rect) {
        if (node == null) {
            return champion;
        }
        Point2D point = node.point;
        if (champion == null ||
                point.distanceSquaredTo(target) < target.distanceSquaredTo(champion)) {
            champion = node.point;
        }
        else if (!rect.contains(target)
                && rect.distanceSquaredTo(target) >= target.distanceSquaredTo(champion)) {
            return champion;
        }

        if (compare(target, node) <= 0) {
            if (node.height % 2 == 0) {
                RectHV left = new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
                champion = nearest(target, node.left, champion, left);

                RectHV right = new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                champion = nearest(target, node.right, champion, right);
            }
            else {
                RectHV bottom = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
                champion = nearest(target, node.left, champion, bottom);

                RectHV top = new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
                champion = nearest(target, node.right, champion, top);
            }
        }
        else {
            if (node.height % 2 == 0) {
                RectHV right = new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                champion = nearest(target, node.right, champion, right);

                RectHV left = new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
                champion = nearest(target, node.left, champion, left);
            }
            else {
                RectHV top = new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
                champion = nearest(target, node.right, champion, top);

                RectHV bottom = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
                champion = nearest(target, node.left, champion, bottom);
            }
        }
        return champion;
    }

    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = "input10.txt"; // args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        kdtree.draw();
        Point2D target = new Point2D(0.318, 0.217);
        StdDraw.point(target.x(), target.y());
        Point2D nearest = kdtree.nearest(target);
        StdDraw.setPenRadius(0.03);
        StdDraw.point(nearest.x(), nearest.y());
        StdOut.println(nearest);
    }
}
