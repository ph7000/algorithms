/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private final int n;
    private final HashMap<String, Integer> teams;
    private final HashMap<Integer, String> teamsByNumber;
    private final SET<String> teamsSet;
    private final int[][] g;
    private final int[] w;
    private final int[] losses;
    private final int[] r;
    private HashMap<String, Queue<String>> teamsNumberSet;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        this.n = in.readInt();
        this.g = new int[this.n][this.n];
        this.w = new int[this.n];
        this.losses = new int[this.n];
        this.r = new int[this.n];
        this.teams = new HashMap<String, Integer>();
        this.teamsByNumber = new HashMap<Integer, String>();
        this.teamsSet = new SET<String>();
        this.teamsNumberSet = new HashMap<String, Queue<String>>();
        for (int i = 0; i < n; i++) {
            String team = in.readString();
            this.teams.put(team, i);
            this.teamsByNumber.put(i, team);
            this.teamsSet.add(team);
            this.w[i] = in.readInt();
            this.losses[i] = in.readInt();
            this.r[i] = in.readInt();
            for (int j = 0; j < this.n; j++) {
                this.g[i][j] = in.readInt();
            }
        }
        // System.out.println(teams.toString());
        // System.out.println(teamsByNumber.toString());
    }

    // number of teams
    public int numberOfTeams() {
        return this.n;
    }

    // all teams
    public Iterable<String> teams() {
        return this.teamsSet;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!this.teamsSet.contains(team)) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        return this.w[this.teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!this.teamsSet.contains(team)) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        return this.losses[this.teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!this.teamsSet.contains(team)) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        return this.r[this.teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!this.teamsSet.contains(team1) || !this.teamsSet.contains(team2)) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        return this.g[this.teams.get(team1)][this.teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!this.teamsSet.contains(team)) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        Queue<String> eliminators = new Queue<String>();
        for (String other : this.teams()) {
            if (this.wins(team) + this.remaining(team) < this.wins(other)) {
                eliminators.enqueue(other);
            }
        }

        if (!eliminators.isEmpty()) {
            this.teamsNumberSet.put(team, eliminators);
            return true;
        }

        int teamInt = this.teams.get(team);
        // System.out.println(team);
        // System.out.println(this.teams.get(team));
        int v = 2 + this.n - 1 + (this.n - 1) * (this.n - 2) / 2;
        FlowNetwork flowNetwork = new FlowNetwork(v);
        int vertexIndex = 0;

        int vertexIndex1;
        int vertexIndex2;

        for (int i = 0; i < this.n; i++) {
            for (int j = i + 1; j < this.n; j++) {
                if (i != teamInt && j != teamInt) {
                    if (i > teamInt) vertexIndex1 = i - 1;
                    else vertexIndex1 = i;

                    if (j > teamInt) vertexIndex2 = j - 1;
                    else vertexIndex2 = j;

                    FlowEdge sourceEdge = new FlowEdge(this.n - 1 + (this.n - 1) * (this.n - 2) / 2,
                                                       vertexIndex,
                                                       this.g[j][i]);
                    flowNetwork.addEdge(sourceEdge);
                    FlowEdge middleEdge1 = new FlowEdge(vertexIndex,
                                                        (this.n - 1) * (this.n - 2) / 2
                                                                + vertexIndex1,
                                                        Double.POSITIVE_INFINITY);
                    flowNetwork.addEdge(middleEdge1);
                    FlowEdge middleEdge2 = new FlowEdge(vertexIndex,
                                                        (this.n - 1) * (this.n - 2) / 2
                                                                + vertexIndex2,
                                                        Double.POSITIVE_INFINITY);
                    flowNetwork.addEdge(middleEdge2);
                    vertexIndex++;
                }
            }
        }

        for (int i = 0; i < this.n; i++) {
            if (i != teamInt) {
                FlowEdge sinkEdge = new FlowEdge(vertexIndex,
                                                 this.n - 1 + (this.n - 1) * (this.n - 2) / 2 + 1,
                                                 this.w[teamInt] + this.r[teamInt] - this.w[i]);
                flowNetwork.addEdge(sinkEdge);
                vertexIndex++;
            }
        }

        FordFulkerson ff = new FordFulkerson(flowNetwork,
                                             this.n - 1 + (this.n - 1) * (this.n - 2) / 2,
                                             this.n - 1 + (this.n - 1) * (this.n - 2) / 2 + 1);

        int newVertexIndex = 0;
        for (int i = 0; i < this.n; i++) {
            if (i != teamInt) {
                // System.out.println(teamsByNumber.get(i));
                // System.out.println((this.n - 1) * (this.n - 2) / 2 + newVertexIndex);
                if (ff.inCut((this.n - 1) * (this.n - 2) / 2 + newVertexIndex)) {
                    eliminators.enqueue(teamsByNumber.get(i));
                }
                newVertexIndex++;
            }
        }

        this.teamsNumberSet.put(team, eliminators);

        int capacity = 0;
        for (FlowEdge edge : flowNetwork.adj(this.n - 1 + (this.n - 1) * (this.n - 2) / 2)) {
            if (edge.from() == this.n - 1 + (this.n - 1) * (this.n - 2) / 2) {
                capacity += edge.capacity();
            }
        }
        // System.out.println(ff.value() + " " + capacity);
        // System.out.print(flowNetwork.toString());
        return (int) ff.value() != capacity;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        // System.out.print(this.teamsNumberSet.toString());

        if (!teamsSet.contains(team)) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        if (this.isEliminated(team)) {
            return teamsNumberSet.get(team);
        }
        else return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
