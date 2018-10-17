package website.renwujie.core.first.fourteenth_fourth_blockingQueueDemo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * <p>
 * </p>
 *
 * @author renwujie
 * @since 2018-10-17 11:28
 */
public class FileTest {
    private static final int SEARCH_THREADS = 100;
    private static final BlockingQueue<String> queue = new LinkedBlockingDeque<>();
    private static final ThreadLocal<String> str =
            ThreadLocal.withInitial(() -> "1 : 2 : 3 : 4 : 5 : 6 : 7 : 8 : 9 : 0 \n");
    private static int count;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("W://test.txt"));
        put();
        read(bw);
    }

    public static void put() {
        Runnable r = () -> {
            while(true) {
                try {
                    queue.put(str.get());
                    System.out.println(count + "---");
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }

    public static void read(BufferedWriter bw) throws IOException {
        for(int i = 0; i < SEARCH_THREADS; i++) {
            Runnable r = () -> {
                try {
                    while(true) {
                        String value = queue.take();
                        System.out.println(Thread.currentThread().getName() + " : " + value);
                        bw.write(value);
                        bw.flush();
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            };
            new Thread(r).start();
        }
    }

}
