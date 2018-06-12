import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.TreeSet;



public class PointSET {
    private TreeSet<Point2D> set;

    public PointSET()                               // construct an empty set of points
    {
        set = new TreeSet<Point2D>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return set.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return set.size();
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if(p == null) throw new java.lang.IllegalArgumentException();
        set.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if(p == null) throw new java.lang.IllegalArgumentException();
        return set.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for(Point2D p: set)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if(rect == null) throw new java.lang.IllegalArgumentException();
        LinkedList<Point2D> res = new LinkedList<Point2D>();
        for(Point2D p: set) {
            if(rect.contains(p)) res.add(p);
        }
        return res;

    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if(p == null) throw new java.lang.IllegalArgumentException();

        double min = Double.MAX_VALUE;
        Point2D pMin = null;

        for(Point2D pt: set) {
            double d = p.distanceSquaredTo(pt);
            if(d < min) {
                min = d;
                pMin = pt;
            }
        }

        return pMin;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        PointSET t = new PointSET();
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
