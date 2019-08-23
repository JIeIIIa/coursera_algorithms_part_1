import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
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


    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> node = new Node<>(item);
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

    private void shuffle() {

    }

    // remove and return a random item
    public Item dequeue() {
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
            // boolean test = StdRandom.uniform() < 0.5;
            int pos = StdRandom.uniform(size);
            if (pos == 0) {
                result =  head.key;
                head.next.prev = null;
                head = head.next;
            } else
            if (pos == size - 1) {
                result = tail.key;
                tail.prev.next = null;
                tail = tail.prev;
            }
            else {
                Node<Item> node = head;
                while (pos > 0) {
                    node = node.next;
                    pos--;
                }
                result = node.key;
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.prev = null;
                node.next = null;
            }
        }
        size--;
        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item result;
        if (size == 1) {
            result = tail.key;
        }
        else {
            // boolean test = StdRandom.uniform() < 0.5;
            int pos = StdRandom.uniform(size);
            if (pos == 0) {
                result =  head.key;
            } else
            if (pos == size - 1) {
                result = tail.key;
            }
            else {
                Node<Item> node = head;
                while (pos > 0) {
                    node = node.next;
                    pos--;
                }
                result = node.key;
            }
        }
        return result;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Node<Item> head;
        private Node<Item> tail;
        private int current = -1;
        private int[] permutations;
        private Node<Item> node;
        private Object[] arr;

        RandomizedQueueIterator(Node<Item> head, Node<Item> tail) {
            this.head = head;
            this.tail = tail;
            this.permutations = StdRandom.permutation(size);
            this.node = head;
            arr = new Object[size];
            Node<Item> tmp = head;
            for (int i = 0; i < arr.length; i++) {
                arr[i] = tmp;
                tmp = tmp.next;
            }
        }

        @Override
        public boolean hasNext() {

            // return head != null && tail != null;
            return current + 1 < permutations.length;
        }

        @Override
        public Item next() {
            if (current >= permutations.length - 1) {
                throw new NoSuchElementException();
            }
            // Item result;
            // if (head == tail) {
            //     result = head.key;
            //     head = null;
            //     tail = null;
            // }
            // else if (StdRandom.uniform() < 0.5) {
            //     result = head.key;
            //     head = head.next;
            // }
            // else {
            //     result = tail.key;
            //     tail = tail.prev;
            // }
            // return result;
            int steps;
            current++;

            // if (current == 0) {
            //     steps = permutations[0];
            // }
            // else {
            //     steps = permutations[current] - permutations[current - 1];
            // }
            // while (steps != 0) {
            //     if (steps > 0) {
            //         node = node.next;
            //         steps--;
            //     }
            //     else {
            //         node = node.prev;
            //         steps++;
            //     }
            // }
            // return node.key;

            return ((Node<Item>) arr[permutations[current]]).key;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(head, tail);
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        StdOut.println(randomizedQueue.size);
        randomizedQueue.enqueue("first");
        StdOut.println(randomizedQueue.size);
        randomizedQueue.enqueue("last");
        StdOut.println(randomizedQueue.size);
        StdOut.println(randomizedQueue.isEmpty());
        StdOut.println(">>1");
        print(randomizedQueue);
        randomizedQueue.dequeue();
        StdOut.println(">>2");
        print(randomizedQueue);
        randomizedQueue.sample();
        print(randomizedQueue);

    }

    private static <T> void print(RandomizedQueue<T> deque) {
        for (T item : deque) {
            StdOut.println(item);
        }
    }

}
