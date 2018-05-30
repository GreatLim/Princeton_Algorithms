import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import javafx.beans.binding.ObjectExpression;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private Item[] q;

    public RandomizedQueue()                 // construct an empty randomized queue
    {
        q = (Item[]) new Object[1];

    }

    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return N == 0;
    }

    public int size()                        // return the number of items on the randomized queue
    {
        return N;
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++)
            temp[i] = q[i];
        q = temp;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null) throw new IllegalArgumentException();
        if (N == q.length) resize(2 * q.length);
        q[N++] = item;
    }

    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(0, N);
        Item item = q[index];
        q[index] = q[--N];
        q[N] = null;
        if (N > 0 && N == q.length / 4) resize(q.length / 2);
        return item;
    }

    public Item sample()                     // return a random item (but do not remove it)
    {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(0, N);
        return q[index];
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] temp = (Item[]) new Object[N];

        private RandomizedQueueIterator() {
            for(int j = 0; j < N; j++) temp[j] = q[j];
            if(N > 0) StdRandom.shuffle(temp);
        }

        @Override
        public boolean hasNext() {
            return i < N;
        }

        @Override
        public Item next() {
            Item item = temp[i];
            if (item == null) throw new NoSuchElementException();
            i++;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public String toString() {
        String s = "[ ";
        Iterator<Item> iterator = iterator();
        while (iterator.hasNext()) {
            s += iterator.next();
            s += " ";
        }
        s += "]";
        return s;
    }

    public static void main(String[] args)   // unit testing (optional)
    {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
        StdOut.println(q);
        StdOut.println("size: " + q.size());
        StdOut.println("is empty:" + q.isEmpty());

        q.enqueue(1);
        StdOut.println(q);
        StdOut.println("size: " + q.size());
        StdOut.println("is empty:" + q.isEmpty());


        q.dequeue();
        StdOut.println(q);
        StdOut.println("size: " + q.size());
        StdOut.println("is empty:" + q.isEmpty());


        q.enqueue(1);
        StdOut.println(q);
        StdOut.println("size: " + q.size());
        StdOut.println("is empty:" + q.isEmpty());

        q.dequeue();
        StdOut.println(q);
        StdOut.println("size: " + q.size());
        StdOut.println("is empty:" + q.isEmpty());




        q.enqueue(1);
        StdOut.println(q);
        StdOut.println("size: " + q.size());
        StdOut.println("is empty:" + q.isEmpty());

        q.enqueue(3);
        StdOut.println(q);
        StdOut.println("size: " + q.size());
        StdOut.println("is empty:" + q.isEmpty());



        q.enqueue(2);
        StdOut.println(q);
        StdOut.println("size: " + q.size());
        StdOut.println("is empty:" + q.isEmpty());

        q.dequeue();
        StdOut.println(q);
        StdOut.println("size: " + q.size());
        StdOut.println("is empty:" + q.isEmpty());



        Iterator<Integer> iterator1 = q.iterator();
        while (iterator1.hasNext()) {
            StdOut.println(iterator1.next());
        }

        Iterator<Integer> iterator2 = q.iterator();
        while (iterator2.hasNext()) {
            StdOut.println(iterator2.next());
        }


    }
}
