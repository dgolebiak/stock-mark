package stockmark.stockmark.model.Exceptions;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(){
        super("Incorrect Password!");
    }
    public IncorrectPasswordException(String message) {
        super(message);
    }
}

