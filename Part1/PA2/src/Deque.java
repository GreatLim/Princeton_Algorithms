import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private Node sentinel;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public Deque()                           // construct an empty deque
    {
        sentinel = new Node();
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return sentinel.next == sentinel;
    }

    public int size()                        // return the number of items on the deque
    {
        return size;
    }

    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null) throw new IllegalArgumentException();
        Node temp = sentinel.next;
        sentinel.next = new Node();
        sentinel.next.item = item;
        sentinel.next.prev = sentinel;
        sentinel.next.next = temp;
        temp.prev = sentinel.next;
        size++;
    }

    public void addLast(Item item)           // add the item to the end
    {
        if (item == null) throw new IllegalArgumentException();
        Node temp = sentinel.prev;
        temp.next = new Node();
        temp.next.item = item;
        temp.next.prev = temp;
        temp.next.next = sentinel;
        sentinel.prev = temp.next;
        size++;
    }

    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty()) throw new NoSuchElementException();
        Node temp = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return temp.item;
    }

    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty()) throw new NoSuchElementException();
        Node temp = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return temp.item;
    }

    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = sentinel.next;

        public boolean hasNext() {
            return current != sentinel;
        }

        public Item next() {
            Item item = current.item;
            if (item == null) throw new NoSuchElementException();
            current = current.next;
            return item;
        }

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
        Deque<Integer> d = new Deque<Integer>();
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());

        d.addFirst(1);
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());


        d.addFirst(2);
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());


        d.addFirst(3);
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());


        d.addLast(2);
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());


        d.addLast(3);
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());


        d.removeFirst();
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());


        d.removeLast();
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());

        d.removeLast();
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());

        d.removeLast();
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());

        d.removeLast();
        StdOut.println(d);
        StdOut.println("size: " + d.size());
        StdOut.println("is empty:" + d.isEmpty());
    }

}

