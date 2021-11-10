package pdtsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A collection of utility function for algorithm working on hamiltonian cycles.
 */
public class HamiltonianCycleUtils {
    /**
     * Part of the local research "2-opt".
     * Here is implemented the only movement possible.
     * The full algorithm must be reimplemented in classes using this method.
     * Delete 2 edges and replace them by 2 new edges.
     * <p>
     * https://fr.wikipedia.org/wiki/2-opt
     * <p>
     * First sublist is all elements from [0,s[ and [t, H.size()[.
     * Second sublist is all elements from [s,t[.
     *
     * @param H The hamiltonian cycle to process.
     * @param s Index of the end (not included) of the first sublist.
     * @param t Index of the end (not included) of the second sublist, can be H.size().
     * @return The modified hamiltonian cycle.
     */
    public static List<Integer> twoOptMove(List<Integer> H, int s, int t) {
        List<Integer> sub1 = new ArrayList<>(H.subList(0, s));
        List<Integer> sub2 = new ArrayList<>(H.subList(s, t));
        List<Integer> sub3 = new ArrayList<>(H.subList(t, H.size()));
        List<Integer> temp = new ArrayList<>();

        Collections.reverse(sub2);

        temp.addAll(sub1);
        temp.addAll(sub2);
        temp.addAll(sub3);

        return temp;
    }

    /**
     * Part of the local research "3-opt".
     * <p>
     * Here is implemented the only movement possible.
     * The full algorithm must be reimplemented in classes using this method.
     * Delete 3 edges and replace them by 3 new edges.
     * There is 7 new configurations possible, and the best move to make is not
     * decided in this method. It must be found beforehand ({@see PaperPDTSP}).
     * <p>
     * There is no wikipedia article on the 3-opt (nor k-opt).
     * https://fr.wikipedia.org/wiki/2-opt
     * <p>
     * First sublist is all elements from [0,s[ and [u, H.size()[.
     * Second sublist is all elements from [s,t[.
     * Third sublist is all elements from [t,u[.
     *
     * @param H          The hamiltonian cycle to process.
     * @param s          Index of the end (not included) of the first sublist.
     * @param t          Index of the end (not included) of the second sublist.
     * @param u          Index of the end (not included) of the third sublist, can be H.size().
     * @param moveNumber the movement wanted to be done.
     * @return The modified hamiltonian cycle.
     */
    public static List<Integer> threeOptMove(List<Integer> H, int s, int t, int u, int moveNumber) {
        List<Integer> sub1 = new ArrayList<>(H.subList(0, s));
        List<Integer> sub2 = new ArrayList<>(H.subList(s, t));
        List<Integer> sub3 = new ArrayList<>(H.subList(t, u));
        List<Integer> sub4 = new ArrayList<>(H.subList(u, H.size()));

        List<Integer> temp = new ArrayList<>(sub1);

        switch (moveNumber) {
            case 0 -> {
                temp.addAll(sub3);
                temp.addAll(sub2);
            }
            case 1 -> {
                Collections.reverse(sub3);
                temp.addAll(sub3);
                temp.addAll(sub2);
            }
            case 2 -> {
                Collections.reverse(sub2);
                temp.addAll(sub3);
                temp.addAll(sub2);
            }
            case 3 -> {
                Collections.reverse(sub2);
                Collections.reverse(sub3);
                temp.addAll(sub2);
                temp.addAll(sub3);
            }
            case 4 -> {
                Collections.reverse(sub3);
                temp.addAll(sub2);
                temp.addAll(sub3);
            }
            case 5 -> {
                Collections.reverse(sub2);
                temp.addAll(sub2);
                temp.addAll(sub3);
            }
            case 6 -> {
                Collections.reverse(sub3);
                Collections.reverse(sub2);
                temp.addAll(sub3);
                temp.addAll(sub2);
            }
        }
        temp.addAll(sub4);

        return temp;
    }
}
