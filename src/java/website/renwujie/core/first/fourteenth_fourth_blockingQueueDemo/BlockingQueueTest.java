package website.renwujie.core.first.fourteenth_fourth_blockingQueueDemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <p>
 * </p>
 *
 * @author renwujie
 * @since 2018-10-16 11:37
 */
public class BlockingQueueTest {
    private static final int FILE_QUEUE_SIZE = 10;
    private static final int SEARCH_THREADS = 100;
    private static final File DUMMY = new File("");
    private static BlockingQueue<File> queue = new ArrayBlockingQueue<File>(FILE_QUEUE_SIZE);


    public static void main(String[] args){
        try(Scanner in = new Scanner(System.in)) {
            System.out.println("Enter base directory (e.g. /opt/jdk1.8.0/src):");
            String directory = in.nextLine();
            System.out.println("Enter keyword (e.g. volatile):");
            String keyWord = in.nextLine();

            Runnable enumetator = () -> {
                try {
                    enumerate(new File(directory));
                    queue.put(DUMMY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            new Thread(enumetator).start();

            for(int i = 1; i <= SEARCH_THREADS; i++) {
                Runnable searcher = () -> {
                    try{
                        boolean done = false;
                        while(!done) {
                            File file = queue.take();
                            if(file == DUMMY) {
                                queue.put(file);
                                done = true;
                            }
                            else search(file, keyWord);
                        }
                    } catch (InterruptedException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                };
                new Thread(searcher).start();
            }
        }
    }


    /**
     * Recursively enumerates all files in a given directory and its subdirectories.
     * @param directory
     * @throws InterruptedException
     */
    public static void enumerate(File directory) throws InterruptedException {
        File[] files = directory.listFiles();
        if(files != null) {
            for(File file : files) {
                if(file.isDirectory()) enumerate(file);
                else queue.put(file);
            }
        }
    }


    public static void search(File file, String keyword) throws FileNotFoundException {
        try (Scanner in = new Scanner(file, "UTF-8")) {
            int lineNumber = 0;
            while(in.hasNextLine()) {
                lineNumber++;
                String line = in.nextLine();
                if(line.contains(keyword))
                    System.out.printf("%s:%d:%s%n", file.getPath(), lineNumber, line);
            }
        }
    }

}
