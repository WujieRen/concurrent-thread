package website.renwujie.core.first.fourteenth_callable_future;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * <p>
 * </p>
 *
 * @author renwujie
 * @since 2018-10-16 16:50
 */
public class FutureTest {
    public static void main(String[] args){
        try(Scanner in = new Scanner(System.in)) {
            System.out.println("Enter base directory (e.g. /opt/jdk1.8.0/src):");
            String directory = in.nextLine();
            System.out.println("Enter keyword (e.g. volatile):");
            String keyWord = in.nextLine();

            MatchCounter counter = new MatchCounter(new File(directory), keyWord);
            FutureTask<Integer> task = new FutureTask<>(counter);
            Thread t = new Thread(task);
            t.start();
            try{
                System.out.println(task.get() + " match files.");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

class MatchCounter implements Callable<Integer> {

    private File directory;
    private String keyword;

    public MatchCounter(File directory, String keyword) {
        this.directory = directory;
        this.keyword = keyword;
    }

    @Override
    public Integer call() throws Exception {
        int count = 0;
        try {
            File[] files = directory.listFiles();
            List<Future<Integer>> results = new ArrayList<>();

            for(File file : files) {
                if(file.isDirectory()) {
                    MatchCounter counter = new MatchCounter(file, keyword);
                    FutureTask<Integer> task = new FutureTask<>(counter);
                    results.add(task);
                    Thread t = new Thread(task);
                    t.start();
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