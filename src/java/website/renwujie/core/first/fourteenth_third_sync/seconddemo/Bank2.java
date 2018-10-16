package website.renwujie.core.first.fourteenth_third_sync.seconddemo;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * </p>
 *
 * @author renwujie
 * @since 2018-10-15 16:00
 */
public class Bank2 {
    private final double[] accounts;
    private Lock bankLock = new ReentrantLock();

    public Bank2(int n, double initialBalance) {
        accounts = new double[n];
        Arrays.fill(accounts, initialBalance);
    }

    public void transfer(int from, int to, double amount) {
        bankLock.lock();
        try{
            if(accounts[from] < amount) return;
            System.out.print(Thread.currentThread()) ;
            accounts [from] -= amount ;
            System.out.printf( "%10.2f from %d to %d", amount, from, to ) ;
            accounts [to] += amount ;
            System.out.printf("Total Balance: %10.2f%n", getTotalBalance()) ;
        } finally {
            bankLock.unlock();
        }

    }

    public double getTotalBalance() {
        double sum = 0;
        for(double a : accounts)
            sum += a;
        return sum;
    }

    public int size() {
        return accounts.length;
    }
}
