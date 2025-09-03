/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private int numSegments = 0;
    private LineSegment[] segmentHolder;

    private class Merge {
        private void merge(Point[] a, Point[] aux, int lo, int mid, int hi) {
            for (int k = lo; k <= hi; k++)
                aux[k] = a[k];
            int i = lo, j = mid + 1;
            for (int k = lo; k <= hi; k++) {
                if (i > mid) a[k] = aux[j++];
                else if (j > hi) a[k] = aux[i++];
                else if (aux[j].compareTo(aux[i]) < 0) a[k] = aux[j++];
                else a[k] = aux[i++];
            }
        }

        private void sort(Point[] a, Point[] aux, int lo, int hi) {
            if (hi <= lo) return;
            int mid = lo + (hi - lo) / 2;
            sort(a, aux, lo, mid);
            sort(a, aux, mid + 1, hi);
            merge(a, aux, lo, mid, hi);
        }

        public void sort(Point[] a) {
            Point[] aux = new Point[a.length];
            sort(a, aux, 0, a.length - 1);
        }
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Illegal argument");
        }
        Point[] pointsCopy = points.clone();

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Illegal argument");
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].equals(points[j])) {
                    throw new IllegalArgumentException("Illegal argument");
                }
            }
        }

        segmentHolder = new LineSegment[
                pointsCopy.length * (pointsCopy.length - 1) * (pointsCopy.length - 2)
                        * (pointsCopy.length - 3) / (4 * 3 * 2)];
        int q = 0;
        Merge merge = new Merge();
        merge.sort(pointsCopy);


        for (int i = 0; i < pointsCopy.length; i++) {
            for (int j = i + 1; j < pointsCopy.length; j++) {
                for (int k = j + 1; k < pointsCopy.length; k++) {
                    for (int l = k + 1; l < pointsCopy.length; l++) {
                        if ((pointsCopy[i].slopeTo(pointsCopy[j]) == pointsCopy[i].slopeTo(
                                pointsCopy[k])) && (
                                pointsCopy[j].slopeTo(pointsCopy[k]) == pointsCopy[i].slopeTo(
                                        pointsCopy[l]))) {
                            numSegments += 1;

                            segmentHolder[q] = new LineSegment(pointsCopy[i], pointsCopy[l]);
                        }
                        q += 1;
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return numSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[numSegments];
        int j = 0;
        for (int i = 0; i < segmentHolder.length; i++) {
            if (segmentHolder[i] != null) {
                segs[j] = segmentHolder[i];
                j += 1;
            }
        }
        return segs;
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
