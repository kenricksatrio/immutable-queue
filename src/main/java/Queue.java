public interface Queue<T> {

    /**
     * Inserts the specified item to the tail of the queue.
     *
     * @return The queue after the specified item is added to the queue.
     * @throws NullPointerException if the specified item is null
     */
    Queue<T> enQueue(T item);

    /**
     * Removes the item at the beginning of the queue and returns the queue.
     */
    Queue<T> deQueue();

    /**
     * Retrieves the head of the queue.
     *
     * @return the head of the queue, or null if the queue is empty.
     */
    T head();

    /**
     * Checks if queue has item or not.
     *
     * @return {@code true} if the queue has no item.
     */
    boolean isEmpty();
}
