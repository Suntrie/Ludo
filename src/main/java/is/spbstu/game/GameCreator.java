package is.spbstu.game;

public class GameCreator {

    private GameCreator(){
    }

    public static Game createGame(int handLongSideLength, int handShortSideWidth, int numberOfPegs, int numberOfPlayers){
        return new ConsoleGame(handLongSideLength,handShortSideWidth, numberOfPegs, numberOfPlayers);
    }
}
