/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private int numSegments = 0;
    private Queue<Node> nodeQueue;

    private class Node {
        Point startPoint;
        Point endPoint;
        double slope;
        LineSegment lineSegment;

        public Node(Point stPt, Point endPt, double slpe, LineSegment line) {
            this.startPoint = stPt;
            this.endPoint = endPt;
            this.slope = slpe;
            this.lineSegment = line;
        }
    }

    private class Merge1 {
        private void merge(Point[] a, Point[] aux, int lo, int mid, int hi, Point b) {
            for (int k = lo; k <= hi; k++)
                aux[k] = a[k];
            int i = lo, j = mid + 1;
            for (int k = lo; k <= hi; k++) {
                if (i > mid) a[k] = aux[j++];
                else if (j > hi) a[k] = aux[i++];
                else if (b.slopeOrder().compare(aux[j], aux[i]) < 0) a[k] = aux[j++];
                else a[k] = aux[i++];
            }
        }

        private void sort(Point[] a, Point[] aux, int lo, int hi, Point b) {
            if (hi <= lo) return;
            int mid = lo + (hi - lo) / 2;
            sort(a, aux, lo, mid, b);
            sort(a, aux, mid + 1, hi, b);
            merge(a, aux, lo, mid, hi, b);
        }

        public void sort(Point[] a, Point b) {
            Point[] aux = new Point[a.length];
            sort(a, aux, 0, a.length - 1, b);
        }
    }

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

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Illegal argument");
        }

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

        if (points.length < 4) {
            return;
        }

        nodeQueue = new Queue<Node>();
        Point[] points2;
        Point[] points3 = points.clone();
        Merge merge = new Merge();
        merge.sort(points3);

        for (int m = 0; m < points.length; m++) {
            points2 = points3.clone();
            Merge1 merge1 = new Merge1();
            merge1.sort(points2, points3[m]);

            int lineSegmentLength = 2;
            boolean isMaximal = true;
            for (int n = 1; n < points.length - 1; n++) {
                if (points3[m].slopeTo(points2[n]) == points3[m].slopeTo(points2[n + 1])
                        && points3[m].compareTo(points2[n]) < 0
                        && points2[n].compareTo(points2[n + 1]) < 0) {
                    lineSegmentLength += 1;
                    if (points2[n - 1].compareTo(points3[m]) < 0
                            && (points3[m].slopeTo(points2[n]) == points3[m].slopeTo(
                            points2[n - 1]))) {
                        isMaximal = false;
                    }

                    if (n == points.length - 2 && lineSegmentLength >= 4) {
                        if (isMaximal) {
                            nodeQueue.enqueue(
                                    new Node(points3[m], points2[n + 1],
                                             points3[m].slopeTo(points2[n]),
                                             new LineSegment(points3[m], points2[n + 1])));
                        }
                        else isMaximal = true;
                    }
                }
                else if (lineSegmentLength >= 4) {
                    lineSegmentLength = 2;
                    if (isMaximal) {
                        nodeQueue.enqueue(
                                new Node(points3[m], points2[n], points3[m].slopeTo(points2[n]),
                                         new LineSegment(points3[m], points2[n])));
                    }
                    else isMaximal = true;
                }
                else {
                    lineSegmentLength = 2;
                    isMaximal = true;
                }
            }
        }
        numSegments = nodeQueue.size();
    }

    // the number of line segments
    public int numberOfSegments() {
        return numSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[numSegments];
        if (numSegments > 0) {
            int i = 0;
            for (Node node : nodeQueue) {
                segs[i] = node.lineSegment;
                i++;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
