import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int numberOfSegments = 0;
    private LineSegment[] segments = new LineSegment[1];

//    Corner cases. Throw a java.lang.IllegalArgumentException if the argument to the constructor is null,
//    if any point in the array is null, or if the argument to the constructor contains a repeated point.

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if(points == null) throw new java.lang.IllegalArgumentException();
        int len = points.length;
        for(int i = 0; i < len; i++) {
            if(points[i] == null) throw new java.lang.IllegalArgumentException();
            for(int j = i + 1; j < len; j++) {
                if(points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException();
            }
        }



        Point[] temp = points.clone();
        Arrays.sort(temp);

        for(int i = 0; i < len; i++) {
            for(int j = i + 1; j < len; j++) {
                double slope1 = temp[i].slopeTo(temp[j]);
                for(int k = j + 1; k < len; k++) {
                    double slope2 = temp[i].slopeTo(temp[k]);
                    if(slope1 != slope2) continue;
                    for(int l = k + 1; l < len; l++) {
                        double slope3 = temp[i].slopeTo(temp[l]);
                        if(slope2 == slope3) {
                            if(segments.length == numberOfSegments) resize(2 * numberOfSegments);
                            segments[numberOfSegments++] = new LineSegment(temp[i], temp[l]);
                        }
                    }
                }
            }
        }
        resize(numberOfSegments);
    }

    private void resize(int n) {
        LineSegment[] temp = new LineSegment[n];
        for(int i = 0; i < numberOfSegments; i++)
            temp[i] = segments[i];
        segments = temp;
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numberOfSegments;
    }

    public LineSegment[] segments()                // the line segments
    {
        return segments.clone();
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
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