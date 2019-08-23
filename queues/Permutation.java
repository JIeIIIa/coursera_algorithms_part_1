/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);
        int count = 0;
        while (!StdIn.isEmpty()) {
            String line = StdIn.readString();
            randomizedQueue.enqueue(line);
            count++;
        }
        for (String s : randomizedQueue) {
            if (k <= 0) {
                return;
            }
            StdOut.println(s);
            k--;
        }
    }
}
