package stockmark.stockmark.model;

import java.util.ArrayList;

import stockmark.stockmark.model.Types.Transaction;

public class ExcelFileCreator {
    public String getString(ArrayList<Transaction> history) {
        StringBuilder excelString = new StringBuilder();

        for (Transaction transaction : history) {
        excelString.append(transaction.action())
            .append("\t").append(transaction.ticker())
            .append("\t").append(transaction.amount())
            .append("\t").append(transaction.unitPrice())
            .append("\t").append(transaction.time())
            .append("\n");
        }
        return excelString.toString();
    }
}
