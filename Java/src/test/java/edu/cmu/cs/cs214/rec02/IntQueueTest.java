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

public class IntQueueTest {

    private IntQueue mQueue;
    private List<Integer> testList;

    @Before
    public void setUp() {
        // LinkedIntQueue тестлэхэд:
        mQueue = new LinkedIntQueue();
        // ArrayIntQueue тестлэхэд:
        // mQueue = new ArrayIntQueue();

        testList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
    }

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
}
