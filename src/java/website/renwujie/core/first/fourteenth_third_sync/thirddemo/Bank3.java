package website.renwujie.core.first.fourteenth_third_sync.thirddemo;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * </p>
 *
 * @author renwujie
 * @since 2018-10-15 16:00
 */
public class Bank3 {
    private final double[] accounts;
    private Lock bankLock;
    private Condition sufficientFunds;

    public Bank3(int n, double initialBalance) {
        accounts = new double[n];
        Arrays.fill(accounts, initialBalance);
        bankLock = new ReentrantLock();
        sufficientFunds = bankLock.newCondition();
    }

    public void transfer(int from, int to, double amount) {
        bankLock.lock();
        try{
            while(accounts[from] < amount)
                sufficientFunds.await();
            System.out.print(Thread.currentThread()) ;
            accounts [from] -= amount ;
            System.out.printf( "%10.2f from %d to %d", amount, from, to ) ;
            accounts [to] += amount ;
            System.out.printf(" Total Balance: %10.2f%n", getTotalBalance()) ;
            sufficientFunds.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bankLock.unlock();
        }
    }

    //TODO:我觉得这里没必要同步啊
    public double getTotalBalance() {
        bankLock.lock();
        try{
            double sum = 0;
            for(double a : accounts)
                sum += a;
            return sum;
        } finally {
            bankLock.unlock();
        }

    }

    public int size() {
        return accounts.length;
    }
}
