package edu.cmu.cs.cs214.rec02;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Integrated test class for both LinkedIntQueue and ArrayIntQueue
 * Contains specification tests and structural tests
 */
public class IntQueueTest {

    private IntQueue mQueue;
    private List<Integer> testList;

    @Before
    public void setUp() {
        // LinkedIntQueue тестлэхэд:
        mQueue = new LinkedIntQueue();
        // ArrayIntQueue тестлэхэд (дараах мөрийг uncomment хийх):
        // mQueue = new ArrayIntQueue();

        testList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
    }

    // ==================== SPECIFICATION TESTS ====================
    // Тодорхойлолтын тестүүд - IntQueue интерфейсийн дагуу

    @Test
    public void testIsEmpty() {
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        mQueue.enqueue(10);
        assertFalse(mQueue.isEmpty());
    }

    @Test
    public void testPeekEmptyQueue() {
        assertNull(mQueue.peek());
    }

    @Test
    public void testPeekNonEmptyQueue() {
        mQueue.enqueue(5);
        assertEquals(Integer.valueOf(5), mQueue.peek());
        assertEquals(1, mQueue.size());
        // Peek нь дарааллыг өөрчлөхгүй гэдгийг шалгах
        assertEquals(Integer.valueOf(5), mQueue.peek());
        assertEquals(1, mQueue.size());
    }

    @Test
    public void testEnqueue() {
        for (int i = 0; i < testList.size(); i++) {
            mQueue.enqueue(testList.get(i));
            assertEquals(testList.get(0), mQueue.peek());
            assertEquals(i + 1, mQueue.size());
        }
    }

    @Test
    public void testDequeue() {
        // Эхлээд хоосон дарааллаас dequeue хийх
        assertNull(mQueue.dequeue());
        
        // Элементүүд нэмэх
        for (Integer value : testList) {
            mQueue.enqueue(value);
        }
        
        // Бүх элементүүдийг зөв дарааллаар гаргаж авах
        for (int i = 0; i < testList.size(); i++) {
            assertEquals(testList.get(i), mQueue.dequeue());
            assertEquals(testList.size() - i - 1, mQueue.size());
        }
        
        // Дараалал хоосон болсныг шалгах
        assertTrue(mQueue.isEmpty());
        assertNull(mQueue.dequeue());
    }

    @Test
    public void testClear() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.enqueue(3);
        
        mQueue.clear();
        
        assertTrue(mQueue.isEmpty());
        assertEquals(0, mQueue.size());
        assertNull(mQueue.peek());
        assertNull(mQueue.dequeue());
    }

    @Test
    public void testContent() throws IOException {
        InputStream in = new FileInputStream("src/test/resources/data.txt");
        try (Scanner scanner = new Scanner(in)) {
            scanner.useDelimiter("\\s*fish\\s*");

            List<Integer> correctResult = new ArrayList<>();
            while (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                correctResult.add(input);
                mQueue.enqueue(input);
            }

            for (Integer result : correctResult) {
                assertEquals(result, mQueue.dequeue());
            }
            
            assertTrue(mQueue.isEmpty());
        }
    }

    // ==================== STRUCTURAL TESTS ====================
    // Бүтцийн тестүүд - ArrayIntQueue-н дотоод хэрэгжилтийг тестлэх

    @Test
    public void testArrayQueueInitialState() {
        // Энэ тест зөвхөн ArrayIntQueue-д зориулагдсан
        assumeArrayQueue();
        
        assertTrue(mQueue.isEmpty());
        assertEquals(0, mQueue.size());
        assertNull(mQueue.peek());
        assertNull(mQueue.dequeue());
    }

    @Test
    public void testArrayQueueEnqueueNullValue() {
        assumeArrayQueue();
        
        // ArrayIntQueue-д null утга нэмэх боломжгүй
        assertFalse(mQueue.enqueue(null));
        assertTrue(mQueue.isEmpty());
        assertEquals(0, mQueue.size());
    }

    @Test
    public void testArrayQueueWrapAround() {
        assumeArrayQueue();
        
        // Дарааллыг дүүргэх (10 элемент)
        for (int i = 0; i < 10; i++) {
            assertTrue(mQueue.enqueue(i));
        }
        
        // 5 элементийг гаргаж авах - head 5 болно
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        
        // Шинэ элементүүд нэмэх - wrap-around үүснэ
        for (int i = 10; i < 15; i++) {
            assertTrue(mQueue.enqueue(i));
        }
        
        // Зөв дарааллаар гаргаж авах
        for (int i = 5; i < 10; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        for (int i = 10; i < 15; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testArrayQueueResize() {
        assumeArrayQueue();
        
        // Анхны багтаамжаас хэтрүүлэх (10 -> resize)
        for (int i = 0; i < 15; i++) {
            assertTrue(mQueue.enqueue(i));
        }
        
        assertEquals(15, mQueue.size());
        
        // Бүх элементүүд зөв эсэхийг шалгах
        for (int i = 0; i < 15; i++) {
            assertEquals(Integer.valueOf(i), mQueue.peek());
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
            assertEquals(14 - i, mQueue.size());
        }
    }

    @Test
    public void testArrayQueueResizeWithWrapAround() {
        assumeArrayQueue();
        
        // Эхлээд дүүргэх
        for (int i = 0; i < 10; i++) {
            mQueue.enqueue(i);
        }
        
        // Хагасыг нь гаргаж авах - head = 5 болно
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        
        // Одоо tail = 0 байна (wrap-around)
        // Илүү олон элемент нэмж resize хийх (10 -> 21)
        for (int i = 10; i < 20; i++) {
            mQueue.enqueue(i);
        }
        
        // Нийт 5 + 10 = 15 элемент байна
        assertEquals(15, mQueue.size());
        
        // Зөв дарааллаар гаргаж авах
        for (int i = 5; i < 10; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        for (int i = 10; i < 20; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testArrayQueueEnqueueDequeueMix() {
        assumeArrayQueue();
        
        // Холимог үйлдлүүд
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        assertEquals(Integer.valueOf(1), mQueue.dequeue());
        mQueue.enqueue(3);
        mQueue.enqueue(4);
        assertEquals(Integer.valueOf(2), mQueue.dequeue());
        assertEquals(Integer.valueOf(3), mQueue.peek());
        assertEquals(Integer.valueOf(3), mQueue.dequeue());
        mQueue.enqueue(5);
        assertEquals(Integer.valueOf(4), mQueue.dequeue());
        assertEquals(Integer.valueOf(5), mQueue.dequeue());
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testArrayQueueClearAfterOperations() {
        assumeArrayQueue();
        
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.enqueue(3);
        
        mQueue.clear();
        
        assertTrue(mQueue.isEmpty());
        assertEquals(0, mQueue.size());
        assertNull(mQueue.peek());
        assertNull(mQueue.dequeue());
        
        // Clear хийсний дараа дахин ашиглах
        mQueue.enqueue(10);
        assertEquals(Integer.valueOf(10), mQueue.peek());
        assertEquals(1, mQueue.size());
        assertEquals(Integer.valueOf(10), mQueue.dequeue());
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testArrayQueuePeekAfterDequeue() {
        assumeArrayQueue();
        
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        
        assertEquals(Integer.valueOf(1), mQueue.dequeue());
        assertEquals(Integer.valueOf(2), mQueue.peek());
        assertEquals(1, mQueue.size());
    }

    @Test
    public void testArrayQueueLargeNumberOfElements() {
        assumeArrayQueue();
        
        for (int i = 0; i < 100; i++) {
            mQueue.enqueue(i);
        }
        
        assertEquals(100, mQueue.size());
        
        for (int i = 0; i < 100; i++) {
            assertEquals(Integer.valueOf(i), mQueue.peek());
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
            assertEquals(99 - i, mQueue.size());
        }
        
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testArrayQueueMultipleResizes() {
        assumeArrayQueue();
        
        // Олон удаа resize хийх
        for (int i = 0; i < 1000; i++) {
            mQueue.enqueue(i);
        }
        
        for (int i = 0; i < 500; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        
        for (int i = 1000; i < 1500; i++) {
            mQueue.enqueue(i);
        }
        
        for (int i = 500; i < 1000; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        for (int i = 1000; i < 1500; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testArrayQueueBoundaryConditions() {
        assumeArrayQueue();
        
        // Яг багтаамжийн хэмжээгээр нэмэх
        for (int i = 0; i < 10; i++) {
            mQueue.enqueue(i);
        }
        assertEquals(10, mQueue.size());
        
        // Бүгдийг гаргаж авах
        for (int i = 0; i < 10; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
        
        // Дахин ашиглах
        mQueue.enqueue(100);
        assertEquals(Integer.valueOf(100), mQueue.peek());
        assertEquals(1, mQueue.size());
    }

    // ==================== HELPER METHODS ====================

    /**
     * Туслах метод: ArrayIntQueue эсэхийг шалгах
     * Хэрэв LinkedIntQueue бол тестийг skip хийх
     */
    private void assumeArrayQueue() {
        org.junit.Assume.assumeTrue(mQueue instanceof ArrayIntQueue);
    }

    // ==================== COMBINED TESTS ====================
    // Хоёр төрлийн queue-д ажиллах тестүүд

    @Test
    public void testFifoOrder() {
        // FIFO (First-In-First-Out) дарааллыг шалгах
        int[] values = {10, 20, 30, 40, 50};
        
        for (int value : values) {
            mQueue.enqueue(value);
        }
        
        for (int value : values) {
            assertEquals(Integer.valueOf(value), mQueue.dequeue());
        }
    }

    @Test
    public void testSizeConsistency() {
        assertEquals(0, mQueue.size());
        
        mQueue.enqueue(1);
        assertEquals(1, mQueue.size());
        
        mQueue.enqueue(2);
        assertEquals(2, mQueue.size());
        
        mQueue.dequeue();
        assertEquals(1, mQueue.size());
        
        mQueue.dequeue();
        assertEquals(0, mQueue.size());
    }

    @Test
    public void testEnqueueDequeueSequence() {
        // Нэмж гаргах үйлдлүүдийн дараалал
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        assertEquals(Integer.valueOf(1), mQueue.dequeue());
        mQueue.enqueue(3);
        assertEquals(Integer.valueOf(2), mQueue.dequeue());
        assertEquals(Integer.valueOf(3), mQueue.dequeue());
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testEmptyAfterClear() {
        mQueue.enqueue(1);
        mQueue.enqueue(2);
        mQueue.clear();
        assertTrue(mQueue.isEmpty());
        assertEquals(0, mQueue.size());
    }

    @Test
    public void testPeekWithoutRemoving() {
        mQueue.enqueue(42);
        assertEquals(Integer.valueOf(42), mQueue.peek());
        assertEquals(1, mQueue.size()); // Peek нь size-г өөрчлөхгүй
        assertEquals(Integer.valueOf(42), mQueue.peek()); // Дахин peek хийхэд мөн адил
    }

    @Test
    public void testDequeueFromEmptyReturnsNull() {
        assertNull(mQueue.dequeue());
        mQueue.enqueue(1);
        mQueue.dequeue();
        assertNull(mQueue.dequeue());
    }
}
