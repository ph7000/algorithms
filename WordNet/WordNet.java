/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    private final Digraph digraph;
    private final HashMap<String, Queue<Integer>> graphValues;
    private final HashMap<Integer, String> graphKeys;
    private final HashSet<String> nouns;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        In in = new In(synsets);
        int id;
        String line;
        String synsetLine;

        graphValues = new HashMap<String, Queue<Integer>>();
        graphKeys = new HashMap<Integer, String>();
        nouns = new HashSet<String>();
        int numVertices = 0;

        while (in.hasNextLine()) {
            line = in.readLine();
            String[] lineArray = line.split(",");
            id = Integer.parseInt(lineArray[0]);
            synsetLine = lineArray[1];
            String[] synsetArray = synsetLine.split(" ");
            String noun;
            for (int i = 0; i < synsetArray.length; i++) {
                noun = synsetArray[i];
                nouns.add(noun);
                Queue<Integer> set = new Queue<Integer>();
                set.enqueue(id);

                if (graphValues.containsKey(noun)) {
                    Queue<Integer> set2 = graphValues.get(noun);
                    set2.enqueue(id);
                    graphValues.put(noun, set2);
                    /* if (noun.equals("off-white")) {
                        for (int j : set2) {
                            System.out.print(" " + j);
                        }
                        System.out.println();
                    }

                     */
                }
                else graphValues.put(noun, set);
            }

            if (graphValues.containsKey(synsetLine)) {
                Queue<Integer> set2 = graphValues.get(synsetLine);
                set2.enqueue(id);
                graphValues.put(synsetLine, set2);
            }
            else {
                Queue<Integer> set = new Queue<Integer>();
                set.enqueue(id);
                graphValues.put(synsetLine, set);
            }

            graphKeys.put(id, synsetLine);
            numVertices += 1;
        }
        digraph = new Digraph(numVertices);

        In hypernymsIn = new In(hypernyms);
        int source = -1;
        while (hypernymsIn.hasNextLine()) {
            line = hypernymsIn.readLine();
            String[] hypernymArray = line.split(",");
            if (hypernymArray.length == 1) {
                source = Integer.parseInt(hypernymArray[0]);
            }
            for (int i = 1; i < hypernymArray.length; i++) {
                digraph.addEdge(Integer.parseInt(hypernymArray[0]),
                                Integer.parseInt(hypernymArray[i]));
            }
        }


        DepthFirstOrder dfo = new DepthFirstOrder(digraph);
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        for (int i : dfo.reversePost()) {
            if (i != source) {
                if (digraph.outdegree(i) == 0) {
                    throw new IllegalArgumentException("Illegal Argument");
                }
            }
        }

        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        Queue<String> nouns2 = new Queue<String>();
        for (String word : nouns) {
            nouns2.enqueue(word);
        }
        return nouns2;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Illegal Argument");
        }


        Queue<Integer> v = graphValues.get(nounA);
        Queue<Integer> w = graphValues.get(nounB);


        return sap.length(v, w);
    }

    // a syn-set (second field of syn-sets.txt) that is the common ancestor of nounA and nounB
    // in the shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Illegal Argument");
        }

        Queue<Integer> v = graphValues.get(nounA);
        Queue<Integer> w = graphValues.get(nounB);

        /* for (int i : v) {
            System.out.println(i);
        }


        for (int i : w) {
            System.out.print(i + " ");
        }

         */


        return graphKeys.get(sap.ancestor(v, w));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(wordnet.digraph.V());
        /* for (String noun : wordnet.nouns()) {
            System.out.println(noun);
        }

         */
        System.out.println(wordnet.sap("aye-aye", "off-white"));
        System.out.println(wordnet.distance("aye-aye", "off-white"));
    }
}
