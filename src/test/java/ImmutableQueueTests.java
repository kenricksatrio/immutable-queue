import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ImmutableQueueTests {

    @ParameterizedTest
    @MethodSource("provideItemsForEnqueue")
    public void enQueue_AddsNewItemToTheTail(List<Integer> items, int expectedItem) {
        Queue<Integer> queue = new ImmutableQueue<>();
        for (Integer item : items) {
            queue = queue.enQueue(item);
        }

        var actualFirstItem = queue.head();

        assertEquals(expectedItem, actualFirstItem, "enQueue(...) should insert new item to the tail of the queue.");
    }

    /**
     * Provides 2 arguments:
     * 1. Items of the queue.
     * 2. The expected first item of the queue.
     */
    private static Stream<Arguments> provideItemsForEnqueue() {
        return Stream.of(
            Arguments.of(List.of(1), 1),
            Arguments.of(List.of(1, 2), 1),
            Arguments.of(List.of(1, 2, 3), 1),
            Arguments.of(List.of(1, 2, 3, 4), 1)
        );
    }

    @Test
    public void enQueue_CreatesNewQueueInstance() {
        Queue<Integer> originalQueue = new ImmutableQueue<>();
        var updatedQueue             = originalQueue.enQueue(1);

        assertNotSame(originalQueue, updatedQueue, "enQueue(...) should create a new queue instance.");
    }

    @Test
    public void enQueue_NullItem_ThrowsNullPointerException() {
        assertThrows(
            NullPointerException.class,
            () -> {
                var queue = new ImmutableQueue<Integer>();
                queue.enQueue(null);
            },
            "enQueue(...) null item should throw NullPointerException.");
    }

    @ParameterizedTest
    @MethodSource("providesDequeueRemoveTheFirstItemTest")
    public void deQueue_RemovesFirstItem(List<Integer> items, int totalDequeueCall, Integer expectedItem) {
        Queue<Integer> queue = new ImmutableQueue<>();
        for (Integer item : items) {
            queue = queue.enQueue(item);
        }

        for (int i = 0; i < totalDequeueCall; i++) {
            queue = queue.deQueue();
        }

        var actualFirstItem = queue.head();

        assertEquals(expectedItem, actualFirstItem, "deQueue() should remove the first item in the queue.");
    }

    /**
     * Provides 3 arguments:
     * 1. Items of the queue.
     * 2. Total dequeue method call.
     * 3. The expected first item of the queue.
     */
    private static Stream<Arguments> providesDequeueRemoveTheFirstItemTest() {
        return Stream.of(
            Arguments.of(List.of(), 0, null),
            Arguments.of(List.of(1), 1, null),
            Arguments.of(List.of(1, 2), 1, 2),
            Arguments.of(List.of(1, 2), 2, null),
            Arguments.of(List.of(1, 2), 3, null),
            Arguments.of(List.of(1, 2, 3, 4), 1, 2),
            Arguments.of(List.of(1, 2, 3, 4), 2, 3),
            Arguments.of(List.of(1, 2, 3, 4), 3, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("provideItemsForDequeue")
    public void deQueue_CreatesNewQueueInstance(List<Integer> items) {
        Queue<Integer> queue = new ImmutableQueue<Integer>();

        for (Integer item : items) {
            queue = queue.enQueue(1);
        }

        var dequeued = queue.deQueue();

        assertNotSame(queue, dequeued, "dequeue() should create a new queue instance.");
    }

    private static Stream<Arguments> provideItemsForDequeue() {
        return Stream.of(
            Arguments.of(List.of()),
            Arguments.of(List.of(1)),
            Arguments.of(List.of(1, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideConfiguredQueueForHeadTest")
    public void head_ReturnsFirstItem(Supplier<Queue<Integer>> queueSupplier, Integer expectedFirstItem) {
        var queue           = queueSupplier.get();
        var actualFirstItem = queue.head();

        assertEquals(expectedFirstItem, actualFirstItem, "head() should return the first item of the queue.");
    }

    /**
     * Provides 2 arguments:
     * 1. Supplier of the preconfigured queue.
     * 2. The expected first item of the queue.
     */
    private static Stream<Arguments> provideConfiguredQueueForHeadTest() {
        return Stream.of(
            Arguments.of(
                (Supplier<Queue<Integer>>) ImmutableQueue::new,
                null
            ),
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1),
                1
            ),
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1)
                        .deQueue(),
                null
            ),
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1)
                        .enQueue(2)
                        .deQueue(),
                2
            ),
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1)
                        .enQueue(2)
                        .enQueue(3)
                        .deQueue()
                        .enQueue(4)
                        .deQueue(),
                3
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideEmptyQueue")
    public void isEmpty_ReturnsTrue_WhenQueueHasNoItems(Supplier<Queue<Integer>> queueSupplier) {
        Queue<Integer> queue = queueSupplier.get();

        assertTrue(queue.isEmpty(), "isEmpty should return true for an empty queue.");
    }

    /**
     * Provides 1 argument:
     * 1. Supplier of an empty queue.
     */
    private static Stream<Arguments> provideEmptyQueue() {
        return Stream.of(
            Arguments.of(
                (Supplier<Queue<Integer>>) ImmutableQueue::new
            ),
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1)
                        .deQueue()
            ),
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1)
                        .deQueue()
                        .deQueue()
            ),
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1)
                        .deQueue()
                        .enQueue(2)
                        .deQueue()
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonEmptyQueue")
    public void isEmpty_ReturnsFalse_WhenQueueHasItems(Supplier<Queue<Integer>> queueSupplier) {
        Queue<Integer> queue = queueSupplier.get();

        assertFalse(queue.isEmpty(), "isEmpty should return false for non empty queue.");
    }

    /**
     * Provides 1 argument:
     * 1. Supplier of an empty queue.
     */
    private static Stream<Arguments> provideNonEmptyQueue() {
        return Stream.of(
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1)
            ),
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1)
                        .deQueue()
                        .enQueue(2)
            ),
            Arguments.of(
                (Supplier<Queue<Integer>>) () ->
                    (new ImmutableQueue<Integer>())
                        .enQueue(1)
                        .enQueue(2)
                        .deQueue()
                        .deQueue()
                        .enQueue(3)
            )
        );
    }

    /**
     * Test calling multiple methods should still works correctly.
     */
    @Test
    public void immutableQueue_WorksCorrectly() {
        Queue<Integer> originalQueue = new ImmutableQueue<>();

        assertTrue(originalQueue.isEmpty(), "New immutable queue should be empty.");

        var updatedQueue = originalQueue.enQueue(1);

        assertTrue(originalQueue.isEmpty(), "Original queue should be unchanged after mutative method call.");
        assertFalse(updatedQueue.isEmpty(), "enQueue() should insert item to the queue.");
        assertEquals(1, updatedQueue.head(), "head() should return the first item of the queue.");

        updatedQueue = updatedQueue.deQueue();

        assertTrue(updatedQueue.isEmpty(), "deQueue() should remove the first item of the queue.");
        assertNull(updatedQueue.head(), "head() should return null for an empty queue.");

        updatedQueue      = updatedQueue.enQueue(2);
        var updatedQueue1 = updatedQueue.deQueue().enQueue(3);
        var updatedQueue2 = updatedQueue.deQueue().enQueue(4);

        assertEquals(2, updatedQueue.head(), "Original queue should be unchanged after mutative method call.");
        assertEquals(3, updatedQueue1.head(), "enQueue() should insert item to the tail of the queue.");
        assertEquals(4, updatedQueue2.head(), "enQueue() should insert item to the tail of the queue.");
    }
}
