import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;



public class BurrowsWheeler {
    private final static int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        int len = s.length();
        CircularSuffixArray array = new CircularSuffixArray(s);
        int first = 0;
        while (array.index(first) != 0)
            first++;

        BinaryStdOut.write(first);

        for (int i = 0; i < len; i++)
            BinaryStdOut.write(s.charAt((array.index(i) + len - 1) % len));

        BinaryStdOut.close();
    }
    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int N = s.length();
        int[] next = new int[N];
        int[] count = new int[R + 1];

        for(int i = 0; i < N; i++)
            count[s.charAt(i) + 1]++;

        for(int r = 0; r < R; r++)
            count[r + 1] += count[r];

        for(int i = 0; i < N; i++)
            next[count[s.charAt(i)]++] = i;

        for(int i = next[first], c = 0; c < N; i = next[i], c++) {
            BinaryStdOut.write(s.charAt(i));
        }

        BinaryStdOut.close();
    }
    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
