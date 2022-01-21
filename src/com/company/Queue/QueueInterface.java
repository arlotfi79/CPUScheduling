package com.company.Queue;

public interface QueueInterface<E> {
    /** Returns the number of elements in the queue. */
    int size();

    /** Tests whether the queue is empty. */
    boolean isEmpty();

    /** Inserts an element at the rear of the queue. */
    void enqueue(E element);

    /** Returns, but does not remove, the first element of the queue (null if empty). */
    E first();

    /** Removes and returns the first element of the queue (null if empty). */
    E dequeue();
}
