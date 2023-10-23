package is.spbstu.game;

public class GameCreator {

    public static Game createGame(){
        return new ConsoleGame();
    }
}
