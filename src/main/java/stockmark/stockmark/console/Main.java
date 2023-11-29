package stockmark.stockmark.console;

import java.util.Arrays;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Share;
import stockmark.stockmark.model.Exceptions.*;

public class Main {
    public static void main(String[] args) {
        //System.out.println("STONKSSS!");
        AccountManager.Initialize();
        //Market.Initialize();

        try {
            var id = AccountManager.loginAccount("hk.32@outlook.com", "123");
            var acc = AccountManager.getFromUUID(id);
            //acc.sellAsset("AMZN", 2);
            System.out.println(acc.getAssets());
        } catch (Exception e) {
            System.out.println(e);
        }

        System.exit(0);
    }
}
