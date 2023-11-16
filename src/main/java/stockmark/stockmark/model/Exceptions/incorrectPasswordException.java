package stockmark.stockmark.model.Exceptions;


public class incorrectPasswordException extends Exception {
    public incorrectPasswordException(){
        super("Incorrect Password, please try another one! ");
    }
    public incorrectPasswordException(String message) {
        super(message);
    }
}

