import java.util.Arrays;

public class CircularSuffixArray {
    private CircularSuffix[] circularSuffixes;

    public CircularSuffixArray(String s)    // circular suffix array of s
    {
        if(s == null) throw new java.lang.IllegalArgumentException();
        int len = s.length();
        this.circularSuffixes = new CircularSuffix[len];
        for (int i = 0; i < len; i++)
            circularSuffixes[i] = new CircularSuffix(s, i);

        Arrays.sort(circularSuffixes);
    }
    public int length()                     // length of s
    {
        return circularSuffixes.length;
    }
    public int index(int i)                 // returns index of ith sorted suffix
    {
        if(i < 0 || i >= length()) throw new java.lang.IllegalArgumentException();
        return circularSuffixes[i].index;
    }


    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final String text;
        private final int index;

        private CircularSuffix(String text, int index) {
            this.text = text;
            this.index = index;
        }

        private char charAt(int i) {
            return text.charAt((index + i) % text.length());
        }

        @Override
        public int compareTo(CircularSuffix that) {
            if(this == that) return 0;
            for (int i = 0; i < length(); i ++) {
                if (this.charAt(i) < that.charAt(i)) return -1;
                if (this.charAt(i) > that.charAt(i)) return 1;
            }
            return 0;
        }
    }


    public static void main(String[] args)  // unit testing (required)
    {
        String s = "ABRACADABRA!";
        CircularSuffixArray array = new CircularSuffixArray(s);
        int len = array.length();
        System.out.println("length: " + len);
        for (int i = 0; i < len; i++)
            System.out.print(array.index(i) + " ");
    }
}
