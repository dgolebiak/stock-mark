package stockmark.stockmark.model;

import java.util.List;

import stockmark.stockmark.model.Types.AccountMetric;
import stockmark.stockmark.model.Types.ChangeOverTime;

public class Leaderboards {
    public static AccountMetric[] getBestPerformers() {
        List<Account> accs = AccountManager.getAccounts();

        AccountMetric[] list = new AccountMetric[accs.size()];

        for (int i = 0; i < list.length; i++) {
            double pcChange = -1000000;
            Account acc = null;

            for (Account inner : accs) {
                var userPcChange = percentChange(inner);
                if (userPcChange > pcChange) {
                    acc = inner;
                    pcChange = userPcChange;
                }
            }

            list[i] = new AccountMetric(acc.getName(), percentChange(acc));
            accs.remove(acc);
        }

        return list;
    }

    private static int percentChange(Account acc) {
        ChangeOverTime change = acc.calcValueChangeOverall();
        double diff = change.current() - change.old();
        return (int) (diff / change.old() * 100);
    }
}
