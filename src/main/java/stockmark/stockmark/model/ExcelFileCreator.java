package stockmark.stockmark.model;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import stockmark.stockmark.model.Types.Transaction;

public class ExcelFileCreator {
    public static String createExcelString(ArrayList<Transaction> history) {
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

    public static String createExcelFile(ArrayList<Transaction> history){
        try {
            Workbook workbook = WorkbookFactory.create(true);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            
            Sheet sheet = workbook.createSheet();
            int rowNum = 0;

            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Action");
            row.createCell(1).setCellValue("Ticker");
            row.createCell(2).setCellValue("Amount");
            row.createCell(3).setCellValue("Price");
            row.createCell(4).setCellValue("Time");

            for (Transaction transaction : history){
                row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(transaction.action());
                row.createCell(1).setCellValue(transaction.ticker());
                row.createCell(2).setCellValue(transaction.amount());
                row.createCell(3).setCellValue(transaction.unitPrice());
                row.createCell(4).setCellValue(transaction.time());
            }
            workbook.write(byteArray);
            return Base64.getEncoder().encodeToString(byteArray.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
