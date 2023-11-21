package stockmark.stockmark.model.Exceptions;

public class NotEnoughAssetsException extends Exception {
    public NotEnoughAssetsException(){
        super("Not enough assets in account!");
    }
    public NotEnoughAssetsException(String message) {
        super(message);
    }
}