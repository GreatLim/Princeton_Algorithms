import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import java.awt.Color;

public class SeamCarver {
    private Picture pic;


    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if(picture == null) throw new IllegalArgumentException();
        pic = new Picture(picture);
    }

    public Picture picture()                          // current picture
    {
        return new Picture(pic);
    }

    public int width()                            // width of current picture
    {
        return pic.width();
    }

    public int height()                           // height of current picture
    {
        return pic.height();
    }
    public double energy(int x, int y)               // energy of pixel at column x and row y
    {
        if(!isValid(x, y)) throw new IllegalArgumentException();
        if(isBorder(x, y)) return 1000;

        int dx2, dy2;
        Color lb, rt;

        lb = pic.get(x - 1, y);
        rt = pic.get(x + 1, y);

        dx2 = getDiff(lb, rt);

        lb = pic.get(x, y - 1);
        rt = pic.get(x, y + 1);

        dy2 = getDiff(lb, rt);


        return Math.sqrt(dx2 + dy2);
    }

    private boolean isBorder(int x, int y) {
        return x == 0 || y == 0 || x == width() - 1 || y == height() - 1;
    }

    private boolean isValid(int x, int y) {
        return x < width() && x >= 0 && y < height() && y >= 0;
    }

    private int getDiff(Color lb, Color rt) {
        int dr = lb.getRed() - rt.getRed();
        int dg = lb.getGreen() - rt.getGreen();
        int db = lb.getBlue() - rt.getBlue();
        return dr * dr + dg * dg + db * db;
    }

    public int[] findVerticalSeam()               // sequence of indices for horizontal seam
    {
        int[] seam = new int[height()];
        if(width() == 1) return seam;
        double[][] energy = new double[width()][height()];
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        int minIndex = 0;

        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height(); j++)
                energy[i][j] = energy(i, j);

        for (int j = 0; j < height(); j++) {
            for(int i = 0; i < width(); i++) {
                if(j == 0) distTo[i][j] = energy[i][j];
                else {
                    int index;
                    if(i == 0) index = compare(i,i + 1, j - 1, distTo);
                    else if(i == width() - 1) index = compare(i, i - 1, j - 1, distTo);
                    else index = compare(i - 1, i , i+ 1, j - 1, distTo);
                    edgeTo[i][j] = index;
                    distTo[i][j] = energy[i][j] + distTo[index][j - 1];
                }
            }
        }

        for(int i = 0; i < width(); i++)
            minIndex = compare(minIndex, i, height() - 1, distTo);

        seam[height() - 1] = minIndex;

        for(int j = height() - 1; j >= 1; j--)
            seam[j - 1] = edgeTo[seam[j]][j];

        return seam;
    }

    private int compare(int i1, int i2, int j, double[][] distTo) {
        return (distTo[i1][j] > distTo[i2][j]) ? i2 : i1;
    }

    private int compare(int i1, int i2, int i3, int j, double[][] distTo) {
        return compare(compare(i1, i2, j, distTo), i3, j, distTo);
    }

    public int[] findHorizontalSeam()                 // sequence of indices for vertical seam
    {
        transpose();
        int[] seam = findVerticalSeam();
        transposeBack();
        return seam;
    }

    private void transpose() {
        Picture temp = new Picture(height(), width());
        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                Color c = pic.get(i, j);
                temp.set(j, i, c);
            }
        }
        pic = temp;
    }

    private void transposeBack() {
        Picture temp = new Picture(height(), width());
        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                Color c = pic.get(i, j);
                temp.set(j, i, c);
            }
        }
        pic = temp;
    }

    public void removeVerticalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        if(width() <= 1) throw new IllegalArgumentException();
        if(!isValidSeam(seam)) throw new IllegalArgumentException();
        Picture temp = new Picture(width() - 1, height());
        int it;
        for(int j = 0; j < height(); j++) {
            it = 0;
            for(int i = 0; i < width(); i++) {
                if(seam[j] == i) continue;
                Color c = pic.get(i, j);
                temp.set(it++, j, c);
            }
        }
        pic = temp;
    }

    private boolean isValidSeam(int[] seam) {
        if(seam == null) return false;
        if(seam.length < pic.height()) return false;
        for(int i = 0; i < seam.length; i++) {
            if(seam[i] >= width()) return false;
            if(i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) return false;
        }
        return true;
    }

    public void removeHorizontalSeam(int[] seam)     // remove vertical seam from current picture
    {
        if(height() <= 1) throw new IllegalArgumentException();
        transpose();
        if(!isValidSeam(seam)) throw new IllegalArgumentException();
        removeVerticalSeam(seam);
        transposeBack();
    }
}