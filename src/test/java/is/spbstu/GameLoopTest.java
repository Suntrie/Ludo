package is.spbstu;

import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

class GameLoopTest {

    @Test
    void playMultipleTimes() throws CmdLineException {

       for (int i=0; i<10000; i++){
           GameLoop.main(new String[]{"-autoplay"});
       }
    }

    @Test
    void playTraceably() throws CmdLineException {
        GameLoop.main(new String[]{"-autoplay", "-handLongSideLength=4",
                "-handShortSideWidth=3", "-numberOfPegs=2", "-numberOfPlayers=1"});
    }

    @Test
    void playTraceablyWith2PegsOfSameColor() throws CmdLineException {
        GameLoop.main(new String[]{"-autoplay", "-handLongSideLength=4",
                "-handShortSideWidth=3", "-numberOfPegs=2", "-numberOfPlayers=1"});
    }

  /*  @Test
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
    }*/
}
