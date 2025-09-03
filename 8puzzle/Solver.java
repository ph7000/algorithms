/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private int moves;
    private boolean solvable;
    private Stack<Board> solutionList = new Stack<Board>();

    private class SearchNode implements Comparable<SearchNode> {
        private Board item;
        private int numMoves;
        private SearchNode prev;
        private int priority;

        public int compareTo(SearchNode p) {
            return Integer.compare(priority, p.priority);
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        moves = -1;
        solvable = true;

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();

        SearchNode init = new SearchNode();
        init.item = initial;
        init.numMoves = 0;
        init.prev = null;
        init.priority = initial.manhattan();
        pq.insert(init);

        MinPQ<SearchNode> pq2 = new MinPQ<SearchNode>();

        SearchNode twin = new SearchNode();
        Board tw = initial.twin();
        twin.item = tw;
        twin.numMoves = 0;
        twin.prev = null;
        twin.priority = tw.manhattan();
        pq2.insert(twin);

        boolean noSolutionYet = true;

        while (noSolutionYet) {
            SearchNode searchNode = pq.delMin();

            if (searchNode.item.isGoal()) {
                noSolutionYet = false;
                getSolutionList(searchNode);
                moves = searchNode.numMoves;
            }

            if (searchNode.prev == null) {
                for (Board s : searchNode.item.neighbors()) {
                    SearchNode neighbor = new SearchNode();
                    neighbor.item = s;
                    neighbor.numMoves = searchNode.numMoves + 1;
                    neighbor.prev = searchNode;
                    neighbor.priority = neighbor.numMoves + neighbor.item.manhattan();
                    pq.insert(neighbor);
                }
            }
            else {
                for (Board s : searchNode.item.neighbors()) {
                    if (!s.equals(searchNode.prev.item)) {
                        SearchNode neighbor = new SearchNode();
                        neighbor.item = s;
                        neighbor.numMoves = searchNode.numMoves + 1;
                        neighbor.prev = searchNode;
                        neighbor.priority = neighbor.numMoves + neighbor.item.manhattan();
                        pq.insert(neighbor);
                    }
                }
            }

            SearchNode searchNode2 = pq2.delMin();

            if (searchNode2.item.isGoal()) {
                noSolutionYet = false;
                solvable = false;
            }

            if (searchNode2.prev == null) {
                for (Board s : searchNode2.item.neighbors()) {
                    SearchNode neighbor = new SearchNode();
                    neighbor.item = s;
                    neighbor.numMoves = searchNode2.numMoves + 1;
                    neighbor.prev = searchNode2;
                    neighbor.priority = neighbor.numMoves + neighbor.item.manhattan();
                    pq2.insert(neighbor);
                }
            }
            else {
                for (Board s : searchNode2.item.neighbors()) {
                    if (!s.equals(searchNode2.prev.item)) {
                        SearchNode neighbor = new SearchNode();
                        neighbor.item = s;
                        neighbor.numMoves = searchNode2.numMoves + 1;
                        neighbor.prev = searchNode2;
                        neighbor.priority = neighbor.numMoves + neighbor.item.manhattan();
                        pq2.insert(neighbor);
                    }
                }
            }
        }

    }

    private void getSolutionList(SearchNode endProduct) {
        solutionList.push(endProduct.item);
        while (endProduct.prev != null) {
            endProduct = endProduct.prev;
            solutionList.push(endProduct.item);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solvable) return solutionList;
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
