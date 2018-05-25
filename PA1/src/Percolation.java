import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private int n;
    private int virtualTop;
    private int virtualBottom;
    private WeightedQuickUnionUF union;
    private WeightedQuickUnionUF unionBackwater;
    private boolean[] isOpen;
    private int numberOfOpenSites;


    public Percolation(int n) {
        this.n = n;
        virtualTop = n * n;
        virtualBottom = n * n + 1;
        union = new WeightedQuickUnionUF(n * n + 2);
        unionBackwater = new WeightedQuickUnionUF(n * n + 2);
        isOpen = new boolean[n * n];
        if (n <= 0) throw new IllegalArgumentException();
        numberOfOpenSites = 0;
    }

    private boolean isValid(int row, int col) {
        return row > 0 && row <= n && col > 0 && col <= n;
    }

    private int getIndex(int row, int col) {
        if (!isValid(row, col))
            throw new IllegalArgumentException();
        return (row - 1) * n + col - 1;
    }

    public void open(int row, int col) {
        int index = getIndex(row, col);
        if (!isOpen(row, col)) {
            isOpen[index] = true;
            numberOfOpenSites++;
            if (row == 1) {
                union.union(index, virtualTop);
                unionBackwater.union(index, virtualTop);
            }
            if (row == n) union.union(index, virtualBottom);
            if (isValid(row + 1, col) && isOpen(row + 1, col)) {
                int indexDown = getIndex(row + 1, col);
                union.union(index, indexDown);
                unionBackwater.union(index, indexDown);
            }
            if (isValid(row - 1, col) && isOpen(row - 1, col)) {
                int indexUp = getIndex(row - 1, col);
                union.union(index, indexUp);
                unionBackwater.union(index, indexUp);
            }
            if (isValid(row, col - 1) && isOpen(row, col - 1)) {
                int indexLeft = getIndex(row, col - 1);
                union.union(index, indexLeft);
                unionBackwater.union(index, indexLeft);
            }
            if (isValid(row, col + 1) && isOpen(row, col + 1)) {
                int indexRight = getIndex(row, col + 1);
                union.union(index, indexRight);
                unionBackwater.union(index, indexRight);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        int index = getIndex(row, col);
        return isOpen[index];
    }

    public boolean isFull(int row, int col) {
        int index = getIndex(row, col);
        if (!isOpen(row, col)) return false;
        return unionBackwater.connected(virtualTop, index);
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return union.connected(virtualTop, virtualBottom);
    }

    private String getSites() {
        String str = "";
        for (int i = 0; i < n * n; i++) {
            if (i != 0 && i % n == 0) str += "\n";
            if (isOpen[i]) str += "O";
            else str += "X";
        }
        return str;
    }


    public static void main(String[] args) {
        StdOut.println("---test constructor---");
        Percolation p = new Percolation(5);
        StdOut.println(p.getSites());
        StdOut.println("this site is percolates: " + p.percolates());
        StdOut.println();


        StdOut.println("---test open---");
        p.open(1, 2);
        StdOut.println(p.getSites());
        StdOut.println();

        StdOut.println("---test numberOfOpenSites---");
        StdOut.println(p.getSites());
        StdOut.println("number of open sites: " + p.numberOfOpenSites());
        StdOut.println();

        StdOut.println("---test isOpen---");
        StdOut.println(p.getSites());
        StdOut.println("(1,2) is open: " + p.isOpen(1, 2));
        StdOut.println();

        StdOut.println("---test isFull---");
        p.open(1, 2);
        p.open(2, 2);
        p.open(2, 3);
        p.open(2, 5);

        StdOut.println(p.getSites());
        StdOut.println("(1,2) is full: " + p.isFull(1, 2));
        StdOut.println("(2,2) is full: " + p.isFull(2, 2));
        StdOut.println("(2,3) is full: " + p.isFull(2, 3));
        StdOut.println("(2,4) is full: " + p.isFull(2, 4));
        StdOut.println("(2,5) is full: " + p.isFull(2, 5));

        StdOut.println();

        StdOut.println("---test percolates---");
        p.open(3, 3);
        p.open(4, 3);
        StdOut.println(p.getSites());
        StdOut.println("this site is percolates: " + p.percolates());
        p.open(5, 3);
        StdOut.println(p.getSites());
        StdOut.println("this site is percolates: " + p.percolates());
        StdOut.println();

    }
}