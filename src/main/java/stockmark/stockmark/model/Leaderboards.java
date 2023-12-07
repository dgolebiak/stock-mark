package stockmark.stockmark.model;

import java.util.Collection;
import java.util.PriorityQueue;

import stockmark.stockmark.model.Types.ChangeOverTime;

public class Leaderboards {
    public static Account[] getBestPerformers(int max) {
        Collection<Account> accs = AccountManager.getAccounts();
        if (max < accs.size())
            max = accs.size();

        PriorityQueue<Account> maxHeap = new PriorityQueue<Account>(
                (Account a, Account b) -> -Integer.compare(percentChange(a), percentChange(b)));

        Account[] list = new Account[max];

        for (Account acc : accs) {
            maxHeap.add(acc);
        }

        for (int i = 0; i < max; i++) {
            list[i] = maxHeap.remove();
        }

        return list;
    }

    private static int percentChange(Account acc) {
        ChangeOverTime change = acc.calcValueChangeOverall();
        double diff = change.current() - change.old();
        return (int) (diff / change.old() * 100);
    }
}
