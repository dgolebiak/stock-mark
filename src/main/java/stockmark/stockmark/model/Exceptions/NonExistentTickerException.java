package stockmark.stockmark.model.Exceptions;

public class NonExistentTickerException extends Exception {
    public NonExistentTickerException() {
        super("The ticker you requested does not exist!");
    }

    public NonExistentTickerException(String message) {
        super(message);
    }
}