package stockmark.stockmark.model.Exceptions;

public class PlayerAlreadyInGameException extends Exception {
    public PlayerAlreadyInGameException(){
        super("You are already in this game!");
    }
    public PlayerAlreadyInGameException(String massage){
        super(massage);
    }
}
