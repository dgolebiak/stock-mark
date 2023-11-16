package stockmark.stockmark.model.Exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(){
        super("The account does not exist! ");
    }
    public AccountNotFoundException(String message) {
        super(message);
    }
}
  
