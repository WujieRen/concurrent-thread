package website.renwujie.core.first.fourteenth_third_sync;

import website.renwujie.core.first.fourteenth_third_sync.fourthdemo.Bank4;

/**
 * <p>
 * </p>
 *
 * @author renwujie
 * @since 2018-10-15 15:57
 */
public class UnsyncBankTest {
    //public static final int NACCOUNTS = 100 ;
    public static final int NACCOUNTS = 10;
    public static final double INITIAL_BALANCE = 1000 ;
    public static final double MAX_AMOUNT = 1000 ;
    public static final int DELAY = 10;

    public static void main(String[] args){
        //Bank1 bank = new Bank1(NACCOUNTS, INITIAL_BALANCE);
        //Bank2 bank = new Bank2(NACCOUNTS, INITIAL_BALANCE);
        //Bank3 bank = new Bank3(NACCOUNTS, INITIAL_BALANCE);
        Bank4 bank = new Bank4(NACCOUNTS, INITIAL_BALANCE);
        for(int i = 0; i < NACCOUNTS; i++) {
            int fromAccount = i;
            Runnable r = () -> {
                try{
                    int toAccount = (int) (bank.size() * Math.random());
                    //double amount = MAX_AMOUNT * Math.random();
                    double amount = 2 * INITIAL_BALANCE;
                    bank.transfer(fromAccount, toAccount, amount);
                    Thread.sleep((long) (DELAY * Math.random()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }
}
