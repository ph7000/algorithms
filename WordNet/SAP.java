/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class SAP {

    private final Digraph digraph;

    private class DirectedBFSIterable {
        private HashMap<Integer, Integer> markedV;
        private HashMap<Integer, Integer> markedW;
        private int ancestor = -1;
        private int len = -1;
        private final Digraph graph;


        public DirectedBFSIterable(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {
            this.graph = G;
            this.markedV = new HashMap<Integer, Integer>();
            this.markedW = new HashMap<Integer, Integer>();

            bfs(v, w);
        }

        private void bfs(Iterable<Integer> v, Iterable<Integer> w) {
            Queue<Integer> qV = new Queue<Integer>();
            Queue<Integer> qW = new Queue<Integer>();
            for (int i : v) {
                qV.enqueue(i);
            }
            for (int i : w) {
                qW.enqueue(i);
            }
            Queue<Integer> qV2 = new Queue<Integer>();
            Queue<Integer> qW2 = new Queue<Integer>();

            int round = 0;
            boolean stop = false;
            while (!stop) {
                for (int i : qV) {
                    if (!markedV.containsKey(i)) {
                        markedV.put(i, round);
                        for (int j : graph.adj(i)) {
                            qV2.enqueue(j);
                        }
                    }
                    if (markedW.containsKey(i)) {
                        if (this.len == -1 || this.len > round + markedW.get(i)) {
                            this.len = round + markedW.get(i);
                            this.ancestor = i;
                        }
                    }
                }
                qV = new Queue<Integer>();
                for (int i : qV2) {
                    qV.enqueue(i);
                }
                qV2 = new Queue<Integer>();

                for (int i : qW) {
                    if (!markedW.containsKey(i)) {
                        markedW.put(i, round);
                        for (int j : graph.adj(i)) {
                            qW2.enqueue(j);
                        }
                    }
                    if (markedV.containsKey(i)) {
                        if (this.len == -1 || this.len > round + markedV.get(i)) {
                            this.len = round + markedV.get(i);
                            this.ancestor = i;
                        }
                    }
                }
                qW = new Queue<Integer>();
                for (int i : qW2) {
                    qW.enqueue(i);
                }
                qW2 = new Queue<Integer>();
                round++;
                if (qW.isEmpty() && qV.isEmpty()) {
                    stop = true;
                }
            }
        }

        public int ancestor() {
            return ancestor;
        }

        public int length() {
            return len;
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        digraph = new Digraph(G);
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V()) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        Queue<Integer> vQ = new Queue<Integer>();
        vQ.enqueue(v);
        Queue<Integer> wQ = new Queue<Integer>();
        wQ.enqueue(w);

        DirectedBFSIterable bfs = new DirectedBFSIterable(digraph, vQ, wQ);
        return bfs.length();
    }

    // a common ancestor of v and w that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V()) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        Queue<Integer> vQ = new Queue<Integer>();
        vQ.enqueue(v);
        Queue<Integer> wQ = new Queue<Integer>();
        wQ.enqueue(w);

        DirectedBFSIterable bfs = new DirectedBFSIterable(digraph, vQ, wQ);
        return bfs.ancestor();
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }
        for (Integer x : v) {
            if (x == null || x < 0 || x >= digraph.V()) {
                throw new IllegalArgumentException("Illegal Argument");
            }
        }
        for (Integer x : w) {
            if (x == null || x < 0 || x >= digraph.V()) {
                throw new IllegalArgumentException("Illegal Argument");
            }
        }

        DirectedBFSIterable bfs = new DirectedBFSIterable(digraph, v, w);
        return bfs.length();
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        for (Integer x : v) {
            if (x == null || x < 0 || x >= digraph.V()) {
                throw new IllegalArgumentException("Illegal Argument");
            }
        }
        for (Integer x : w) {
            if (x == null || x < 0 || x >= digraph.V()) {
                throw new IllegalArgumentException("Illegal Argument");
            }
        }
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }

        DirectedBFSIterable bfs = new DirectedBFSIterable(digraph, v, w);
        return bfs.ancestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        StdOut.println(sap.length(78092, 45850));
        StdOut.println(sap.length(78092, 15467));

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }
}
