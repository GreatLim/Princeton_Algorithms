import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.LinkedList;


public class WordNet {
    private HashMap<Integer, String> st;
    private TreeMap<String, LinkedList<Integer>> map;
    private Digraph G;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if(synsets == null || hypernyms == null) throw new java.lang.IllegalArgumentException();
        st = new HashMap<Integer, String>();
        map = new TreeMap<String, LinkedList<Integer>>();
        readSynsets(new In(synsets));
        G = new Digraph(st.size());
        readhypernyms(new In(hypernyms));
        DirectedCycle directedCycle = new DirectedCycle(G);
        if(directedCycle.hasCycle()) throw new java.lang.IllegalArgumentException();
        int count = 0;
        for(int v = 0; v < G.V(); v++) {
            if(G.indegree(v) != 0 && G.outdegree(v) == 0) {
                count++;
            }
            if(count == 2) throw new java.lang.IllegalArgumentException();
        }
        sap = new SAP(G);

    }

    private void readSynsets(In in) {
        if(in == null) throw new java.lang.IllegalArgumentException();
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] fields = line.split("\\,");
            String[] nouns = fields[1].split(" ");
            int id = Integer.parseInt(fields[0]);
            st.put(id, fields[1]);
            for(String noun : nouns) {
                if(!map.containsKey(noun)) {
                    LinkedList<Integer> list = new LinkedList<Integer>();
                    list.add(id);
                    map.put(noun, list);
                } else {
                    map.get(noun).add(id);
                }
            }
        }
    }

    private void readhypernyms(In in) {
        if(in == null) throw new java.lang.IllegalArgumentException();
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] fields = line.split("\\,");
            for (int i = 1; i < fields.length; i++) {
                G.addEdge(Integer.parseInt(fields[0]), Integer.parseInt(fields[i]));
            }
        }
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if(word == null) throw new java.lang.IllegalArgumentException();
        return map.containsKey(word);
    }
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if(!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException();
        return sap.length(map.get(nounA), map.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if(!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException();
        int ancestor = sap.ancestor(map.get(nounA), map.get(nounB));
        return st.get(ancestor);
    }


    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("/Users/Lim/@inbox/Princeton_Algorithms/Part2/PA1/src/wordnet/synsets3.txt", "/Users/Lim/@inbox/Princeton_Algorithms/Part2/PA1/src/wordnet/hypernyms3InvalidCycle.txt");
    }
}