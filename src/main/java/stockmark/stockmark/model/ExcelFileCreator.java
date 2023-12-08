package stockmark.stockmark.model;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
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

    public byte[] CreateExcelFile(ArrayList<Transaction> history){
        try {
            Workbook workbook = WorkbookFactory.create(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
