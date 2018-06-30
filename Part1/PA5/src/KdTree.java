import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;


public class KdTree {
    private static final boolean HORIZONAL = true;
    private static final boolean VERTICAL = false;
    private Node root;
    private int size;

    public KdTree()                               // construct an empty set of points
    {
        root = null;
        size = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return root == null;
    }

    public int size()                         // number of points in the set
    {
        return size;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if(p == null) throw new java.lang.IllegalArgumentException();
        root = insert(root, null, p);
    }

    private Node insert(Node x, Node parent, Point2D p) {
        if(x == null) {
            size++;
            if(parent == null) return new Node(p, getRectHV(p, null), VERTICAL);
            return new Node(p, getRectHV(p, parent), !parent.direction);
        }
        if(x.p.equals(p)) return x;
        double cmp = 0;

        if(x.direction == VERTICAL) cmp = p.x() - x.p.x();
        else cmp = p.y() - x.p.y();

        if(cmp < 0) x.lb = insert(x.lb, x, p);
        else x.rt = insert(x.rt, x, p);

        return x;
    }

    private RectHV getRectHV(Point2D p, Node parent) {
        double xmin = 0, ymin = 0, xmax = 1, ymax = 1;
        if(parent == null) return new RectHV(xmin, ymin, xmax, ymax);
        if(parent.direction == VERTICAL) {
            ymin = parent.rect.ymin();
            ymax = parent.rect.ymax();
            if(p.x() < parent.p.x())  {
                xmin = parent.rect.xmin();
                xmax = parent.p.x();
            } else {
                xmin = parent.p.x();
                xmax = parent.rect.xmax();
            }
        } else {
            xmin = parent.rect.xmin();
            xmax = parent.rect.xmax();
            if(p.y() < parent.p.y())  {
                ymin = parent.rect.ymin();
                ymax = parent.p.y();
            } else {
                ymin = parent.p.y();
                ymax = parent.rect.ymax();
            }
        }
        return new RectHV(xmin, ymin, xmax, ymax);

    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if(p == null) throw new java.lang.IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if(x == null) return false;
        if(x.p.equals(p)) return true;

        double cmp = 0;
        if(x.direction == VERTICAL) cmp = p.x() - x.p.x();
        else cmp = p.y() - x.p.y();

        if(cmp < 0) return contains(x.lb, p);
        else return contains(x.rt, p);

    }

    public void draw()                         // draw all points to standard draw
    {
        draw(root);
    }

    private void draw(Node x) {
        if(x == null) return;
        draw(x.lb);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        StdDraw.setPenRadius();
        if(x.direction == VERTICAL) {
            StdDraw.setPenRadius();
            StdDraw.setPenColor(StdDraw.RED);
            double x0 = x.p.x();
            double ymin = x.rect.ymin();
            double ymax = x.rect.ymax();
            StdDraw.line(x0, ymin, x0, ymax);
        } else {
            StdDraw.setPenRadius();
            StdDraw.setPenColor(StdDraw.BLUE);
            double y0 = x.p.y();
            double xmin = x.rect.xmin();
            double xmax = x.rect.xmax();
            StdDraw.line(xmin, y0, xmax, y0);
        }

        draw(x.rt);
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if(rect == null) throw new java.lang.IllegalArgumentException();
        LinkedList<Point2D> res = new LinkedList<Point2D>();
        range(root, rect, res);
        return res;
    }

    private void range(Node x, RectHV rect, LinkedList<Point2D> list) {
        if(x == null) return;
        if(rect.contains(x.p)) list.add(x.p);
        if(x.direction == VERTICAL) {
            if(x.p.x() > rect.xmin()) {
                range(x.lb, rect, list);
            }
            if(x.p.x() <= rect.xmax()) {
                range(x.rt, rect, list);
            }
        } else {
            if(x.p.y() > rect.ymin()) {
                range(x.lb, rect, list);
            }
            if(x.p.y() <= rect.ymax()) {
                range(x.rt, rect, list);
            }
        }
    }




    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if(p == null) throw new java.lang.IllegalArgumentException();
        if(root == null) return null;
        return nearest(root, p, root.p);
    }


    private Point2D nearest(Node x, Point2D p, Point2D pt) {
        if(x == null) return pt;
        
        if(x.p.distanceSquaredTo(p) <= pt.distanceSquaredTo(p))
            pt = x.p;

        if((x.direction == VERTICAL && p.x() < x.p.x()) || (x.direction == HORIZONAL && p.y() < x.p.y())) {
            pt = nearest(x.lb, p, pt);
            if(x.rt != null && pt.distanceSquaredTo(p) > x.rt.rect.distanceSquaredTo(p))
                pt = nearest(x.rt, p, pt);
        } else {
            pt = nearest(x.rt, p, pt);
            if(x.lb != null && pt.distanceSquaredTo(p) > x.lb.rect.distanceSquaredTo(p))
                pt = nearest(x.lb, p, pt);
        }
        return pt;
    }




    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean direction;

        private Node(Point2D p, RectHV rect, boolean direction) {
            this.p = p;
            this.rect = rect;
            this.direction = direction;
        }
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        KdTree t = new KdTree();
        Point2D p1 = new Point2D(.7, .2);
        t.insert(p1);
        System.out.println(t.contains(p1));
        Point2D p2 = new Point2D(.5, .4);
        t.insert(p2);
        System.out.println(t.contains(p2));
        Point2D p3 = new Point2D(.2, .3);
        t.insert(p3);
        System.out.println(t.contains(p3));
        Point2D p4 = new Point2D(.4, .7);
        t.insert(p4);
        System.out.println(t.contains(p4));
        Point2D p5 = new Point2D(.9, .6);
        t.insert(p5);
        System.out.println(t.contains(p5));

        Point2D p = new Point2D(.9, .7);
        System.out.println(t.nearest(p));
        t.draw();

    }
}
