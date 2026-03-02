package edu.cmu.cs.cs214.rec02;

import java.util.Arrays;

/**
 * A resizable-array implementation of the {@link IntQueue} interface. The head of
 * the queue starts out at the head of the array, allowing the queue to grow and
 * shrink in constant time.
 *
 * @author Alex Lockwood
 * @author Ye Lu
 */
public class ArrayIntQueue implements IntQueue {

    /**
     * An array holding this queue's data
     */
    private int[] elementData;

    /**
     * Index of the next dequeue-able value
     */
    private int head;

    /**
     * Current size of queue
     */
    private int size;

    /**
     * The initial size for new instances of ArrayQueue
     */
    private static final int INITIAL_SIZE = 10;

    /**
     * Constructs an empty queue with an initial capacity of ten.
     */
    public ArrayIntQueue() {
        elementData = new int[INITIAL_SIZE];
        head = 0;
        size = 0;
    }

    /** {@inheritDoc} */
    public void clear() {
        Arrays.fill(elementData, 0);
        size = 0;
        head = 0;
    }

    /** {@inheritDoc} */
    public Integer dequeue() {
        if (isEmpty()) {
            return null;
        }
        Integer value = elementData[head];
        head = (head + 1) % elementData.length;
        size--;
        return value;
    }

    /** {@inheritDoc} */
    public boolean enqueue(Integer value) {
        if (value == null) {  // null утга шалгах (нэмэлт сайжруулалт)
            return false;
        }
        ensureCapacity();
        int tail = (head + size) % elementData.length;
        elementData[tail] = value;
        size++;
        return true;
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        // АЛДАА №1: size >= 0 байсан - ЭНЭ БУРУУ
        // ЗАСВАР: size == 0 байх ёстой
        return size == 0;
    }

    /** {@inheritDoc} */
    public Integer peek() {
        // АЛДАА №2: хоосон үед null буцаахгүй байсан
        // ЗАСВАР: хоосон үед null буцаах
        if (isEmpty()) {
            return null;
        }
        return elementData[head];
    }

    /** {@inheritDoc} */
    public int size() {
        return size;
    }

    /**
     * Increases the capacity of this <tt>ArrayIntQueue</tt> instance, if
     * necessary, to ensure that it can hold at least size + 1 elements.
     */
    private void ensureCapacity() {
        if (size == elementData.length) {
            int oldCapacity = elementData.length;
            int newCapacity = 2 * oldCapacity + 1;
            int[] newData = new int[newCapacity];
            
            // Эхний хэсгийг хуулах: head-ээс array-ийн төгсгөл хүртэл
            for (int i = head; i < oldCapacity; i++) {
                newData[i - head] = elementData[i];
            }
            
            // АЛДАА №3: Хоёр дахь хэсгийг хуулах алгоритм буруу байсан
            // ЗАСВАР: Зөв индексээр хуулах
            for (int i = 0; i < head; i++) {
                newData[oldCapacity - head + i] = elementData[i];
            }
            
            elementData = newData;
            head = 0;
        }
    }
}
