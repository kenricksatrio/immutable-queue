import java.util.Arrays;
import java.util.Objects;

/**
 * An immutable queue.
 *
 * <p>A <em>thread-safe</em> queue that always make a fresh copy of the underlying
 * array for all mutative operations ({@code enQueue} and {@code deQueue}). Making
 * a fresh copy is useful for thread-safe because there is no modification to the
 * underlying array, thus removing the necessary to lock the array before any
 * operation.</p>
 *
 * <p>All items are permitted, except {@code null}.</p>
 */
public class ImmutableQueue<T> implements Queue<T> {

    private static final Object[] emptyArray = new Object[0];

    private final Object[] array;

    public ImmutableQueue() {
        array = emptyArray;
    }

    private ImmutableQueue(Object[] array) {
        this.array = array;
    }

    @Override
    public Queue<T> enQueue(T item) {
        Objects.requireNonNull(item);

        var length   = this.size();
        var newArray = Arrays.copyOf(array, length + 1);

        newArray[length] = item;

        return new ImmutableQueue<T>(newArray);
    }

    @Override
    public Queue<T> deQueue() {
        if (isEmpty()) {
            return new ImmutableQueue<T>();
        }

        var length   = this.size();
        var newArray = new Object[length - 1];

        // Create a new array starting from the second item of the array.
        System.arraycopy(array, 1, newArray, 0, length - 1);

        return new ImmutableQueue<T>(newArray);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T head() {
        if (isEmpty()) {
            return null;
        }

        return (T) array[0];
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    private int size() {
        return this.array.length;
    }
}
