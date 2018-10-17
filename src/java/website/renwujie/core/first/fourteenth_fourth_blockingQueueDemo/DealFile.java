package website.renwujie.core.first.fourteenth_fourth_blockingQueueDemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>
 *     >reference: http://forrest420.iteye.com/blog/1141073
 * </p>
 *
 * @author renwujie
 * @since 2018-10-16 18:14
 */
public class DealFile implements Runnable {
    private FileOutputStream out;
    private ConcurrentLinkedQueue<String> queue;
    private static final ThreadLocal<String> str = ThreadLocal.withInitial(() -> "1 : 2 : 3 : 4 : 5 : 6 : 7 : 8 : 9 : 0");
    public DealFile() {
    }

    public DealFile(FileOutputStream out, ConcurrentLinkedQueue<String> queue) {
        this.out = out;
        this.queue = queue;
    }

    public void run() {
        while (true) {//循环监听
            if (!queue.isEmpty()) {
                try {
                    out.write(queue.poll().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Creater implements Runnable {
    private ConcurrentLinkedQueue<String> queue;
    private String contents;

    public Creater() {
    }

    public Creater(ConcurrentLinkedQueue<String> queue, String contents) {
        this.queue = queue;
        this.contents = contents;
    }

    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        queue.add(contents);
    }
}


class Test{
    public static void main(String[] args) throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream(new File("W:"+ File.separator +"test.txt"),true);
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

        for(int j=0;j<100000;j++){
            new Thread(new Creater(queue,j+"--")).start();//多线程往队列中写入数据
        }
        new Thread(new DealFile(out,queue)).start();//监听线程，不断从queue中读数据写入到文件中
    }
}