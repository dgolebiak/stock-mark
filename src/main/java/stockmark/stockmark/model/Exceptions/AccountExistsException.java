package stockmark.stockmark.model.Exceptions;

public class AccountExistsException extends Exception {
    public AccountExistsException(){
        super("Account with email already exists!");
    }
    public AccountExistsException(String massage){
        super(massage);
    }
}
