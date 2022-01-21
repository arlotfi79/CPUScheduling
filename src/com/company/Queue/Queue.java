package com.company.Queue;

import java.util.Arrays;
import java.util.Iterator;

public class Queue<E> implements QueueInterface<E> {
    // instance variables
    public static final int CAPACITY = 1000; // default array capacity
    private E[] data;  // generic array used for storage
    private int front = 0;  // index of the front element
    private int size = 0; // current number of elements

    // constructors
    public Queue() {
        this(CAPACITY);
    } // constructs queue with default capacity

    public Queue(int capacity) {   // constructs queue with given capacity
        data = (E[]) new Object[capacity]; // safe cast; compiler may give warning
    }

    // methods
    /** Returns the number of elements in the queue. */
    @Override
    public int size() {
        return size;
    }

    /** Tests whether the queue is empty. */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns, but does not remove, the first element of the queue (null if empty). */
    @Override
    public E first() {
        if (isEmpty())
            return null;
        return data[front];
    }

    /** Inserts an element at the rear of the queue. */
    @Override
    public void enqueue(E e) throws IllegalStateException {
        if (size == data.length)
            throw new IllegalStateException("Queue is full");

        int current = (front + size) % data.length; // use modular arithmetic
        data[current] = e;
        size++;
    }

    /** Removes and returns the first element of the queue (null if empty). */
    @Override
    public E dequeue( ) {
        if (isEmpty())
            return null;

        E answer = data[front];
        data[front] = null; // dereference to help garbage collection
        front = (front + 1) % data.length;
        size--;
        return answer;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}

