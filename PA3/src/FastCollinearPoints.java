import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {
    private int numberOfSegments = 0;
    private LineSegment[] segments = new LineSegment[1];

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null) throw new java.lang.IllegalArgumentException();
        int len = points.length;

        for(int i = 0; i < len; i++) {
            if(points[i] == null) throw new java.lang.IllegalArgumentException();
            for(int j = i + 1; j < len; j++) {
                if(points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException();
            }
        }


        Point[] temp = points.clone();
        Arrays.sort(temp);


        for (int i = 0; i < len; i++) {
            Comparator<Point> comparator = temp[i].slopeOrder();
            Arrays.sort(temp, i + 1, len, comparator);
            int j = i + 1;
            while (j < len) {
                int start = j;
                j = start + 1;
                while (j < len && comparator.compare(temp[j], temp[j - 1]) == 0) j++;
                int end = j;
                int num = end - start;
                if (num >= 3) {
                    boolean isDuplicated = false;
                    for (int k = 0; k < i + 1; k++) {
                        if (comparator.compare(temp[start], temp[k]) == 0) isDuplicated = true;
                    }
                    if (!isDuplicated) {
                        if (segments.length == numberOfSegments) resize(2 * numberOfSegments);
                        segments[numberOfSegments++] = new LineSegment(temp[i], temp[end - 1]);
                    }
                }
            }
            Arrays.sort(temp);
        }
        resize(numberOfSegments);
    }

    private void resize(int n) {
        LineSegment[] temp = new LineSegment[n];
        for (int i = 0; i < numberOfSegments; i++)
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
