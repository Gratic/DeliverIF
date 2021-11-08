package pdtsp;

import java.util.Objects;

/**
 * Utility class to access two elements that can be of different type.
 * Only the first element must be comparable to compare two pair.
 *
 * @param <T>
 * @param <K>
 */
public class Pair<T extends Comparable<T>, K> implements Comparable<Pair<T, K>> {
    private T x;
    private K y;

    public Pair() {
        this.x = null;
        this.y = null;
    }

    public Pair(T x, K y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public K getY() {
        return y;
    }

    public void setY(K y) {
        this.y = y;
    }


    @Override
    public int compareTo(Pair<T, K> o) {
        return this.getX().compareTo(o.getX());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(x, pair.x) && Objects.equals(y, pair.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}