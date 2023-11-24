package stockmark.stockmark.model.Exceptions;

public class BalanceTooLowException extends Exception {
    public BalanceTooLowException(){
        super("The account does not have enough balance!");
    }
    public BalanceTooLowException(String message) {
        super(message);
    }
}
  