package is.spbstu;

import is.spbstu.board.Color;
import is.spbstu.game.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLoopTest {

    @Test
    public void playMultipleTimes(){

       for (int i=0; i<10000; i++){
           GameLoop.main(new String[]{"-autoplay"});
       }
    }

    @Test
    public void playTraceably(){
        GameLoop.main(new String[]{"-autoplay", "-handLongSideLength=4",
                "-handShortSideWidth=3", "-numberOfPegs=1", "-numberOfPlayers=1"});
    }

    @Test
    public void playTraceablyWith2PegsOfSameColor(){
        GameLoop.main(new String[]{"-autoplay", "-handLongSideLength=4",
                "-handShortSideWidth=3", "-numberOfPegs=2", "-numberOfPlayers=1"});
    }

    @Test
    public void playWithMultiplePegsOfTheSameColor(){
        GameLoop.main(new String[]{"-autoplay", "-handLongSideLength=4",
                "-handShortSideWidth=3", "-numberOfPegs=2", "-numberOfPlayers=1"});
        Game game = GameLoop.getGame();
        game.getCrossBoard().getPegsByField()
                .forEach((field, pegs) -> assertEquals(0, pegs.size()));
        game.getCrossBoard().getLastLineFieldsByColor()
                .forEach((color, pegsByField) ->
                        pegsByField.forEach(
                                (field, pegs) -> assertEquals(0, pegs.size())
                        )
                );
        assertEquals(0, game.getCrossBoard().getWentThroughZero().size());
        assertEquals(2, game.getCrossBoard().getHomeTrianglesByColor(Color.GREEN).size());
    }
}
