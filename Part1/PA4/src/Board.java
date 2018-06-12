import java.util.LinkedList;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private char[] tiles;
    private int n;
    private int hamming;
    private int manhattan;
    private int[] emptyPosition;


    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    {
        n = blocks.length;
        tiles = new char[n * n];
        hamming = 0;
        manhattan = 0;
        emptyPosition = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i * n + j] = (char) blocks[i][j];
                if(blocks[i][j] == 0) {
                    emptyPosition[0] = i;
                    emptyPosition[1] = j;
                    continue;
                }
                if(blocks[i][j] != i * n + j + 1) {
                    hamming ++;
                    int it = (blocks[i][j] - 1) / n;
                    int jt = (blocks[i][j] - 1) % n;
                    int di = Math.abs(it - i);
                    int dj = Math.abs(jt - j);
                    manhattan += (di + dj);
                }
            }
        }
    }

    private int Index(int i, int j) {
        return i * n + j;
    }


    public int dimension()                 // board n n
    {
        return n;
    }


    public int hamming()                   // number of blocks out of place
    {
        return hamming;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        return manhattan;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        return hamming() == 0;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {

        if(tiles[0] != 0 && tiles[1] != 0) {
            return exchange(0, 0, 0, 1);
        } else {
            return exchange(1, 0, 1, 1);
        }

    }

    private Board exchange(int i1, int j1, int i2, int j2) {
        int[][] tempBlocks = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tempBlocks[i][j] = tiles[Index(i, j)];
            }
        }

        int temp = tempBlocks[i1][j1];
        tempBlocks[i1][j1] = tempBlocks[i2][j2];
        tempBlocks[i2][j2] = temp;

        return new Board(tempBlocks);
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if(y == this) return true;
        if(y == null) return false;
        if(y.getClass() != this.getClass()) return false;
        if(((Board) y).n != n) return false;
        for (int i = 0; i < n * n; i++) {
            if(tiles[i] != ((Board) y).tiles[i]) {
                return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        LinkedList<Board> res = new LinkedList<Board>();
        int row = emptyPosition[0];
        int col = emptyPosition[1];
        if(isValid(row - 1, col)) res.add(exchange(row, col, row - 1, col));
        if(isValid(row + 1, col)) res.add(exchange(row, col, row + 1, col));
        if(isValid(row, col - 1)) res.add(exchange(row, col, row, col - 1));
        if(isValid(row, col + 1)) res.add(exchange(row, col, row, col + 1));
        return res;
    }

    private boolean isValid(int row, int col) {
        return row < n && col < n && row >= 0 && col >= 0;
    }


    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", (int) tiles[Index(i,j)]));
            }
            s.append("\n");
        }
        return s.toString();
    }


    public static void main(String[] args) // unit tests (not graded)
    {
        int[][] blocks = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board b = new Board(blocks);
        StdOut.println("test n()");
        StdOut.println(b);
        StdOut.println(b.dimension());
        StdOut.println();

        StdOut.println("test hamming()");
        StdOut.println(b);
        StdOut.println(b.hamming());
        StdOut.println();

        StdOut.println("test manhattan()");
        StdOut.println(b);
        StdOut.println(b.manhattan());
        StdOut.println();

        StdOut.println("test neighbors()");
        StdOut.println(b);
        Iterable<Board> neighbors = b.neighbors();
        for(Board neighbor : neighbors) {
            StdOut.println(neighbor);
        }
        StdOut.println();
    }
}
