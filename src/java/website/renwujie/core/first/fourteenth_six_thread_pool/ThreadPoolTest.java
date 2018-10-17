package website.renwujie.core.first.fourteenth_six_thread_pool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * <p>
 * </p>
 *
 * @author renwujie
 * @since 2018-10-17 17:29
 */
public class ThreadPoolTest {
    public static void main(String[] args){
        try(Scanner in = new Scanner(System.in)) {
            System.out.println("Enter base directory (e.g. /opt/jdk1.8.0/src):");
            String directory = in.nextLine();
            System.out.println("Enter keyword (e.g. volatile):");
            String keyWord = in.nextLine();

            ExecutorService pool = Executors.newCachedThreadPool();
            //ExecutorService pool = Executors.newFixedThreadPool(100);

            MatchCounter counter = new MatchCounter(new File(directory), keyWord, pool);
            Future<Integer> result = pool.submit(counter);

            try {
                System.out.println(result.get() + " match files.");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            int largePoolSize = ((ThreadPoolExecutor)pool).getLargestPoolSize();
            System.out.println("largest pool size = " + largePoolSize);
            int activeCount = ((ThreadPoolExecutor)pool).getActiveCount();
            System.out.println("active count = " + activeCount);
            int corePoolSize = ((ThreadPoolExecutor)pool).getCorePoolSize();
            System.out.println("core pool size = " + corePoolSize);
            int poolSize = ((ThreadPoolExecutor)pool).getPoolSize();
            System.out.println("pool size = " + poolSize);
            int maximumPoolSize = ((ThreadPoolExecutor)pool).getPoolSize();
            System.out.println("maximum pool size = " + maximumPoolSize);


            pool.shutdown();

            int largePoolSize1 = ((ThreadPoolExecutor)pool).getLargestPoolSize();
            System.out.println("largest pool size = " + largePoolSize1);
            int activeCount1 = ((ThreadPoolExecutor)pool).getActiveCount();
            System.out.println("active count = " + activeCount1);
            int corePoolSize1 = ((ThreadPoolExecutor)pool).getCorePoolSize();
            System.out.println("core pool size = " + corePoolSize1);
            int poolSize1 = ((ThreadPoolExecutor)pool).getPoolSize();
            System.out.println("pool size = " + poolSize1);
            int maximumPoolSize1 = ((ThreadPoolExecutor)pool).getPoolSize();
            System.out.println("maximum pool size = " + maximumPoolSize1);

        }
    }
}

class MatchCounter implements Callable<Integer> {

    private File directory;
    private String keyword;
    private ExecutorService pool;
    private int count;

    public MatchCounter(File directory, String keyword, ExecutorService pool) {
        this.directory = directory;
        this.keyword = keyword;
        this.pool = pool;
    }

    @Override
    public Integer call() throws Exception {
        count = 0;
        try {
            File[] files = directory.listFiles();
            List<Future<Integer>> results = new ArrayList<>();

            for(File file : files) {
                if(file.isDirectory()) {
                    MatchCounter counter = new MatchCounter(file, keyword, pool);
                    Future<Integer> result = pool.submit(counter);
                    results.add(result);
                }
                else {
                    if(search(file)) count++;
                }
            }

            for(Future<Integer> result : results) {
                try{
                    count += result.get();
                }  catch (ExecutionException e) {
                    //e.printStackTrace();
                }
            }

        } catch (InterruptedException ignored) { }
        return count;
    }

    /**
     * Search a file for a given keyword.
     * @param file
     * @return
     */
    private boolean search(File file) {
        try {
            try(Scanner in = new Scanner(file, "UTF-8")) {
                boolean found = false;
                while(!found && in.hasNextLine()) {
                    String line = in.nextLine();
                    if(line.contains(keyword)) found = true;
                }
                return found;
            }
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}
