import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private final static int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        int n = s.length();
        int[] a = new int[R];

        for(int r = 0; r < R; r++)
            a[r] = r;

        for(int i = 0; i < n; i++) {
            char c = s.charAt(i);
            BinaryStdOut.write(a[c], 8);
            for(int j = 0, count = 0; count < a[c]; j++) {
                if (a[j] < a[c]) {
                    a[j]++;
                    count++;
                }
            }
                a[c] = 0;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int[] a = new int[R];

        for(int r = 0; r < R; r++)
            a[r] = r;

        while(!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readChar();
            BinaryStdOut.write(a[i], 8);
            int temp = a[i];
            for (int j = i; j > 0; j--)
                a[j] = a[j - 1];
            a[0] = temp;
        }
        BinaryStdOut.close();
    }
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
