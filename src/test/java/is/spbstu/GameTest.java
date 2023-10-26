/*
package is.spbstu;

import is.spbstu.board.*;
import is.spbstu.game.ConsoleGame;
import is.spbstu.game.Game;
import is.spbstu.game.GameCreator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void initGame() {
        Game game = GameCreator.createGame(3, 3,
                1, 1);

        CrossBoard board = game.getCrossBoard();
        assertEquals(28, board.getPegsByField().size());
        assertEquals(4, board.getLastLineFieldsByColor().get(Color.BLUE).size());
    }

    @Test
    public void moveWithEat(){

        ConsoleGame consoleGame = new ConsoleGame(3,3,3,2);

        Player greenPlayer = consoleGame.getPlayerByColorMap().get(Color.GREEN);
        CrossBoard crossBoard = consoleGame.getCrossBoard();

        Peg greenPeg = new Peg(Color.GREEN, 2);
        crossBoard.moveBasePeg(greenPeg, MovePegType.NORMAL);
        greenPlayer.fromBaseToActivePegs();

        crossBoard.moveActivePeg(Color.GREEN, 2,3, MovePegType.NORMAL);

        Field greenPegField = crossBoard.getFieldByPeg(greenPeg);

        Player yellowPlayer = new Player(Color.YELLOW, 3);
        Peg redPeg = new Peg(Color.YELLOW, 2);
        crossBoard.moveBasePeg(redPeg, MovePegType.NORMAL);
        yellowPlayer.fromBaseToActivePegs();

        assertEquals(MovePegType.WITH_EAT,
                crossBoard.isPossibleMoveActivePeg(yellowPlayer, 2, 24));
        var res = crossBoard.moveActivePeg(redPeg.color(), redPeg.number(),
                24, MovePegType.WITH_EAT);
        consoleGame.checkAndMoveEatenOpponentPegToBase(res.getRight());

        assertEquals(crossBoard.getFieldByPeg(redPeg), greenPegField);
        assertEquals(1, crossBoard.getPegsByField().get(greenPegField).size());
        assertEquals(redPeg, crossBoard.getPegsByField().get(greenPegField).get(0));
        assertEquals(3, greenPlayer.getBasePegs().size());
        assertEquals(0, greenPlayer.getActivePegs().size());
        assertNull(crossBoard.getFieldByPeg(greenPeg));
        assertFalse(crossBoard.getWentThroughZero().contains(greenPeg));
    }

    //TODO
    */
/*@Test
    public void reinstantiateEatenPeg(){

        ConsoleGame consoleGame = new ConsoleGame();
        consoleGame.initGame(3,3,3,2);

        Player greenPlayer = consoleGame.getPlayerByColorMap().get(Color.GREEN);
        CrossBoard crossBoard = consoleGame.getCrossBoard();

        Peg greenPeg = new Peg(Color.GREEN, 2);
        crossBoard.moveBasePeg(greenPeg, MovePegType.NORMAL);
        greenPlayer.fromBaseToActivePegs();

        crossBoard.moveActivePeg(Color.GREEN, 2,3, MovePegType.NORMAL);

        Field greenPegField = crossBoard.getFieldByPeg(greenPeg);

        Player yellowPlayer = new Player(Color.YELLOW, 3);
        Peg redPeg = new Peg(Color.YELLOW, 2);
        crossBoard.moveBasePeg(redPeg, MovePegType.NORMAL);
        yellowPlayer.fromBaseToActivePegs();

        var res = crossBoard.moveActivePeg(redPeg.color(), redPeg.number(),
                24, MovePegType.WITH_EAT);
        consoleGame.checkAndMoveEatenOpponentPegToBase(res.getRight());

        assertEquals(crossBoard.getFieldByPeg(redPeg), greenPegField);
        assertEquals(1, crossBoard.getPegsByField().get(greenPegField).size());
        assertEquals(redPeg, crossBoard.getPegsByField().get(greenPegField).get(0));
        assertEquals(3, greenPlayer.getBasePegs().size());
        assertEquals(0, greenPlayer.getActivePegs().size());
        assertNull(crossBoard.getFieldByPeg(greenPeg));
        assertFalse(crossBoard.getWentThroughZero().contains(greenPeg));
    }*//*

}
*/
