import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;


public class SAP {
    private Digraph G;
    private int ancestor;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if(G == null) throw new java.lang.IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if(v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new java.lang.IllegalArgumentException();
        int length = Integer.MAX_VALUE;
        ancestor = -1;
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(G, w);
        for(int i = 0; i < G.V(); i++) {
            if(vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                int d = vBFS.distTo(i) + wBFS.distTo(i);
                if(d < length) {
                    length = d;
                    ancestor = i;
                }
            }
        }
        if(length == Integer.MAX_VALUE) return -1;
        return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        length(v, w);
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if(v == null || w == null) throw new java.lang.IllegalArgumentException();

        ancestor = -1;
        int length = Integer.MAX_VALUE;
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(G, w);

        for(int i = 0; i < G.V(); i++) {
            if(vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                int d = vBFS.distTo(i) + wBFS.distTo(i);
                if(d < length) {
                    length = d;
                    ancestor = i;
                }
            }
        }

        if(length == Integer.MAX_VALUE) return -1;
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        length(v, w);
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}