/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node<Item> head;
    private Node<Item> tail;

    private static class Node<T> {
        private T key;
        private Node<T> prev;
        private Node<T> next;

        Node(T key) {
            this.key = key;
        }
    }


    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the node to the front
    public void addFirst(Item key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> node = new Node<>(key);
        if (size == 0) {
            head = node;
            tail = node;
        }
        else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    // add the node to the back
    public void addLast(Item key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> node = new Node<>(key);
        if (size == 0) {
            head = node;
            tail = node;
        }
        else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    // remove and return the node from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item result;
        if (size == 1) {
            result = head.key;
            head = null;
            tail = null;
        }
        else {
            result = head.key;
            head.next.prev = null;
            head = head.next;
        }
        size--;
        return result;
    }

    // remove and return the node from the back
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item result;
        if (size == 1) {
            result = tail.key;
            head = null;
            tail = null;
        }
        else {
            result = tail.key;
            tail.prev.next = null;
            tail = tail.prev;
        }
        size--;
        return result;
    }


    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;


        DequeIterator(Node<Item> head) {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item result = current.key;
            current = current.next;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator(head);
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        StdOut.println(deque.size);
        deque.addFirst("first");
        StdOut.println(deque.size);
        deque.addLast("last");
        StdOut.println(deque.size);
        StdOut.println(deque.isEmpty());
        StdOut.println(">>1");
        print(deque);
        deque.removeFirst();
        StdOut.println(">>2");
        print(deque);
        deque.removeLast();
        print(deque);
    }

    private static <T> void print(Deque<T> deque) {
        for (T item : deque) {
            StdOut.println(item);
        }
    }
}
