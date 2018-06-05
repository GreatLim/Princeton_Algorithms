import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
//import java.util.Stack;
import edu.princeton.cs.algs4.Stack;
import java.util.Comparator;
import java.util.Iterator;


public class Solver {
    private boolean isSolvable;
    private Stack<Board> stack;

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if(initial == null) throw new java.lang.IllegalArgumentException();
        stack = new Stack<Board>();
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>(new SolverComparator());
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>(new SolverComparator());

        SearchNode search = new SearchNode(null, initial, 0);

        SearchNode searchTwin = new SearchNode(null, initial.twin(), 0);

        while(!search.board.isGoal() && !searchTwin.board.isGoal()) {
            for (Board neighbor : search.board.neighbors()) {
                if (search.moves == 0 || !neighbor.equals(search.predecessor.board))
                    pq.insert(new SearchNode(search, neighbor, search.moves + 1));
            }
            for (Board neighbor : searchTwin.board.neighbors()) {
                if (searchTwin.moves == 0 || !neighbor.equals(searchTwin.predecessor.board))
                    pqTwin.insert(new SearchNode(searchTwin, neighbor, searchTwin.moves + 1));
            }

            search = pq.delMin();
            searchTwin = pqTwin.delMin();
        }

        isSolvable = search.board.isGoal();

        if(isSolvable) {
            for(SearchNode n : search) {
                stack.push(n.board);
            }
        }

    }


    public boolean isSolvable()            // is the initial board solvable?
    {
        return isSolvable;
    }
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return stack.size() - 1;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if(isSolvable) return stack;
        else return null;
    }

    private class SolverComparator implements Comparator<SearchNode> {
        public int compare(SearchNode s1, SearchNode s2) {
             return s1.priority  - s2.priority;
        }
    }

    private class SearchNode implements Iterable<SearchNode> {
        private SearchNode predecessor;
        private int priority;
        private Board board;
        private int moves;

        public SearchNode(SearchNode predecessor, Board board, int moves) {
            this.moves = moves;
            this.board = board;
            this.predecessor = predecessor;
            priority = board.manhattan() + moves;
        }

        public Iterator<SearchNode> iterator() {
            return new SearchNodeIterator();
        }

        private class SearchNodeIterator implements Iterator<SearchNode> {
            private SearchNode cur = SearchNode.this;
            @Override
            public boolean hasNext() {
                return cur != null;
            }

            public SearchNode next() {
                SearchNode res = cur;
                cur = cur.predecessor;
                return res;
            }

            public void remove() {};
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
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
