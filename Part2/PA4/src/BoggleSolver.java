import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;

public class BoggleSolver {
    private BoggleBoard board;
    private boolean[][] marked;
    private static final int R = 26;
    private Node root;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        int i = 0;
        for(String s: dictionary)
            add(s);
    }

    private static class Node {
        private boolean isString;
        Node[] next = new Node[R];
    }

    private void add(String key) {
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if(x == null) x = new Node();
        if(d == key.length()) {
            x.isString = true;
            return x;
        }
        int c = key.charAt(d) - 'A';
        x.next[c] = add(x.next[c], key, d + 1);
        return x;
    }

    private boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if(x == null) return null;
        if(d == key.length()) return x;
        int c = key.charAt(d) - 'A';
        return get(x.next[c], key, d + 1);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        HashSet<String> set = new HashSet<String>();

        marked = new boolean[board.rows()][board.cols()];

        for(int i = 0; i < board.rows(); i++) {
            for(int j = 0; j < board.cols(); j++) {
                dfs(i, j, set, "", root);
            }
        }

        return set;
    }

    private void dfs(int i, int j, HashSet<String> set, String prefix, Node x) {
        if(!isValid(i, j) || marked[i][j]) return;

        char c = board.getLetter(i, j);
        if(c == 'Q') {
            prefix = prefix + "QU";
            x = x.next['Q' - 'A'];
            if(x == null) return;
            x = x.next['U' - 'A'];
        } else {
            prefix = prefix + c;
            x = x.next[c - 'A'];
        }

        if(x == null) return;
        if(x.isString && prefix.length() >= 3) set.add(prefix);

        marked[i][j] = true;

        dfs(i + 1, j, set, prefix, x);
        dfs(i - 1, j, set, prefix, x);
        dfs(i, j + 1, set, prefix, x);
        dfs(i, j - 1, set, prefix, x);
        dfs(i + 1, j + 1, set, prefix, x);
        dfs(i - 1, j - 1, set, prefix, x);
        dfs(i + 1, j - 1, set, prefix, x);
        dfs(i - 1, j + 1, set, prefix, x);

        marked[i][j] = false;
    }

    private boolean isValid(int i, int j) {
        return i >= 0 && i < board.rows() && j >= 0 && j < board.cols();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int len = word.length();
        if(len < 3 || !contains(word)) return 0;
        if(len < 5) return 1;
        if(len == 5) return 2;
        if(len == 6) return 3;
        if(len == 7) return 5;
        else return 11;

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}