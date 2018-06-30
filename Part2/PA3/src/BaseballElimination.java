import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {
    private HashMap<String, Integer> map;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    private int n;
    private int full;
    private FordFulkerson ff;
    private String first;
    private boolean easy;


    public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
    {
        In in = new In(filename);
        map = new HashMap<String, Integer>();
        n = in.readInt();
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        first = "";
        for(int i = 0; i < n; i++) {
            String team = in.readString();
            map.put(team, i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for(int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
            first = (i == 0 || w[i] > w[map.get(first)]) ? team : first;
        }
    }


    public int numberOfTeams()                        // number of teams
    {
        return n;
    }

    public Iterable<String> teams()                                // all teams
    {
        return map.keySet();
    }

    public int wins(String team)                      // number of wins for given team
    {
        if(!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
        return w[map.get(team)];
    }

    public int losses(String team)                    // number of losses for given team
    {
        if(!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
        return l[map.get(team)];
    }

    public int remaining(String team)                 // number of remaining games for given team
    {
        if(!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
        return r[map.get(team)];
    }

    public int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        if(!map.containsKey(team1) || !map.containsKey(team2)) throw new java.lang.IllegalArgumentException();
        return g[map.get(team1)][map.get(team2)];
    }

    public boolean isEliminated(String team)              // is given team eliminated?
    {
        if(!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
        if(wins(team) + remaining(team) < w[map.get(first)]) {
            easy = true;
            return true;
        }
        easy = false;
        int V = n + (n - 1) * (n - 2) / 2 + 2;
        int s = V - 2, t = V - 1;

        FlowNetwork G = G(team, s, t, V);
        ff = new FordFulkerson(G, s, t);
        if(ff.value() == full) return false;
        else return true;
    }

    private FlowNetwork G(String team, int s, int t, int V) {
        if(!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
        int index = map.get(team);
        full = 0;
        FlowNetwork G = new FlowNetwork(V);
        for(int i = 0; i < n; i++) {
            if(i == index) continue;
            double c = w[index] + r[index] - w[i];
            FlowEdge e = new FlowEdge(i, t, c);
            G.addEdge(e);
        }

        int k = n;

        for(int i = 0; i < n; i++) {
            if(i == index) continue;
            for(int j = i + 1; j < n; j ++) {
                if(j == index) continue;
                FlowEdge e1 = new FlowEdge(k, i, Double.POSITIVE_INFINITY);
                FlowEdge e2 = new FlowEdge(k, j, Double.POSITIVE_INFINITY);
                FlowEdge e3 = new FlowEdge(s, k, g[i][j]);
                full += g[i][j];

                G.addEdge(e1);
                G.addEdge(e2);
                G.addEdge(e3);

                k++;
            }
        }
//        System.out.println(G);
        return G;
    }

    public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        if(!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
        int index = map.get(team);
        if(!isEliminated(team)) return null;
        HashSet<String> set = new HashSet<String>();
        if(easy) {
            set.add(first);
            return set;
        }
        for(String s: teams()) {
            int i = map.get(s);
            if(i != index && ff.inCut(i)) set.add(s);
        }
        return set;
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
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
