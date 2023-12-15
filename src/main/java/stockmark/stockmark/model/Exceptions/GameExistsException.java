package stockmark.stockmark.model.Exceptions;

public class GameExistsException extends Exception {
    public GameExistsException(){
        super("Game with that name already exists!");
    }
    public GameExistsException(String massage){
        super(massage);
    }
}
