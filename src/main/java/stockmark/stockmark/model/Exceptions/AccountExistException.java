package stockmark.stockmark.model.Exceptions;

public class AccountExistException extends Exception {
    public AccountExistException(){
        super("Account with email already exists, try to log in! ");
    }
    public AccountExistException(String massage){
        super(massage);
    }
}
