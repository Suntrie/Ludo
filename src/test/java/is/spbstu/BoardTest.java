package is.spbstu;

import is.spbstu.board.*;
import is.spbstu.game.MoveResult;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void setMultiplePegsOfDifferentColorOnSameFieldWithEat() {
        CrossBoard crossBoard = new CrossBoard(3, 3);
        Peg bluePeg = new Peg(Color.BLUE, 1);
        crossBoard.movePegFromBase(bluePeg);
        assertEquals(new Field(26, false), crossBoard.getPegPosition(bluePeg));

        Peg redPeg = new Peg(Color.RED, 1);
        crossBoard.movePegFromBase(redPeg);
        crossBoard.moveActivePeg(redPeg.color(), redPeg.number(),
                7);
        assertEquals(new Field(26, false), crossBoard.getPegPosition(redPeg));

        assertEquals(1, crossBoard.getPegsByField().get(new Field(26, false)).size());
    }

    @Test
    void makeCircle() {

        CrossBoard crossBoard = new CrossBoard(3, 3);
        Peg bluePeg = new Peg(Color.BLUE, 1);
        crossBoard.movePegFromBase(bluePeg);

        assertEquals(new Field(26, false), crossBoard.getPegPosition(bluePeg));
        assertFalse(crossBoard.getWentThroughZero().contains(bluePeg));

        crossBoard.moveActivePeg(bluePeg.color(), bluePeg.number(),
                5);

        assertEquals(new Field(3, false), crossBoard.getPegPosition(bluePeg));
        assertTrue(crossBoard.getWentThroughZero().contains(bluePeg));
    }

    @Test
    void goToLastLine() {

        CrossBoard crossBoard = new CrossBoard(3, 3);
        Peg bluePeg = new Peg(Color.BLUE, 1);
        crossBoard.movePegFromBase(bluePeg);

        crossBoard.moveActivePeg(bluePeg.color(), bluePeg.number(), 27);

        assertEquals(new Field(0, true), crossBoard.getPegPosition(bluePeg));
        assertTrue(crossBoard.getWentThroughZero().contains(bluePeg));
    }

    @Test
    void goToLastLineWithExcessWithinLimit() {

        CrossBoard crossBoard = new CrossBoard(3, 3);
        Peg bluePeg = new Peg(Color.BLUE, 1);
        crossBoard.movePegFromBase(bluePeg);

        crossBoard.moveActivePeg(bluePeg.color(), bluePeg.number(), 28);

        assertEquals(new Field(1, true), crossBoard.getPegPosition(bluePeg));
        assertTrue(crossBoard.getWentThroughZero().contains(bluePeg));
    }

    @Test
    void goToLastLineWithGoToHome() {

        //TODO: player win + game win

        CrossBoard crossBoard = new CrossBoard(3, 3);
        Peg bluePeg = new Peg(Color.BLUE, 1);
        crossBoard.movePegFromBase(bluePeg);

        crossBoard.moveActivePeg(bluePeg.color(), bluePeg.number(), 29);

        checkCrossBoardCleanOnWin(crossBoard, bluePeg);
    }

    private static void checkCrossBoardCleanOnWin(CrossBoard crossBoard, Peg bluePeg) {
        assertNull(crossBoard.getPegPosition(bluePeg));
        assertFalse(crossBoard.getWentThroughZero().contains(bluePeg));
        assertFalse(crossBoard.getLastLineFieldsByColor().get(Color.BLUE).values().stream().flatMap(Collection::stream)
                .collect(Collectors.toSet()).contains(bluePeg));
        assertFalse(crossBoard.getPegsByField().values().stream().flatMap(Collection::stream)
                .collect(Collectors.toSet()).contains(bluePeg));
    }

    @Test
    void goToLastLineWithGoToHomeIn3Steps() {

        CrossBoard crossBoard = new CrossBoard(3, 3);
        Peg bluePeg = new Peg(Color.BLUE, 1);
        crossBoard.movePegFromBase(bluePeg);

        crossBoard.moveActivePeg(bluePeg.color(), bluePeg.number(), 24);
        crossBoard.moveActivePeg(bluePeg.color(), bluePeg.number(), 3);
        crossBoard.moveActivePeg(bluePeg.color(), bluePeg.number(), 2);

        checkCrossBoardCleanOnWin(crossBoard, bluePeg);
    }

    @Test
    void goToLastLineWithExcessOutsideLimit() {

        CrossBoard crossBoard = new CrossBoard(3, 3);
        Peg bluePeg = new Peg(Color.BLUE, 1);
        crossBoard.movePegFromBase(bluePeg);

        crossBoard.moveActivePeg(bluePeg.color(), bluePeg.number(),
                28);
        MoveResult moveResult = crossBoard.moveActivePeg(bluePeg.color(), bluePeg.number(),
                2);
        assertEquals(MovePegType.LAST_LINE_LIMIT_EXCESS, moveResult.movePegType());
    }

    @Test
    void setMultiplePegsOfSameColor2Block(){

        Player bluePlayer = new Player(Color.BLUE, 3);
        CrossBoard crossBoard = new CrossBoard(3 ,3);

        Peg firstPeg = new Peg(Color.BLUE, 0);
        MoveResult moveResult = crossBoard.movePegFromBase(firstPeg);
        bluePlayer.movePegForward(moveResult);

        crossBoard.moveActivePeg(firstPeg.color(), firstPeg.number(),
                3);

        Peg secondPeg = new Peg(Color.BLUE, 1);
        moveResult = crossBoard.movePegFromBase(secondPeg);
        bluePlayer.movePegForward(moveResult);
        crossBoard.moveActivePeg(secondPeg.color(), secondPeg.number(),
                3);

        assertEquals(crossBoard.getFieldByPeg(firstPeg), crossBoard.getFieldByPeg(secondPeg));

        Peg thirdPeg = new Peg(Color.BLUE, 2);
        moveResult = crossBoard.movePegFromBase(thirdPeg);
        bluePlayer.movePegForward(moveResult);
        moveResult = crossBoard.moveActivePeg(thirdPeg.color(), thirdPeg.number(),
                3);
        assertEquals(MovePegType.BLOCKED,
                moveResult.movePegType());

        Player redPlayer = new Player(Color.RED, 3);
        Peg redPeg = new Peg(Color.RED, 2);
        moveResult = crossBoard.movePegFromBase(redPeg);
        redPlayer.movePegForward(moveResult);
        moveResult = crossBoard.moveActivePeg(redPeg.color(), redPeg.number(),
                10);
        assertEquals(MovePegType.BLOCKED, moveResult.movePegType());

    }


    @Test
    void setMultiplePegsOfSameColor2BlockOnSourceField(){

        Player bluePlayer = new Player(Color.BLUE, 3);
        CrossBoard crossBoard = new CrossBoard(3 ,3);

        Peg firstPeg = new Peg(Color.BLUE, 0);
        MoveResult moveResult = crossBoard.movePegFromBase(firstPeg);
        bluePlayer.movePegForward(moveResult);


        Peg secondPeg = new Peg(Color.BLUE, 1);
        moveResult = crossBoard.movePegFromBase(secondPeg);
        bluePlayer.movePegForward(moveResult);

        assertEquals(crossBoard.getFieldByPeg(firstPeg), crossBoard.getFieldByPeg(secondPeg));

        Peg thirdPeg = new Peg(Color.BLUE, 2);
        moveResult = crossBoard.movePegFromBase(thirdPeg);

        assertEquals(MovePegType.BLOCKED,
                moveResult.movePegType());

        crossBoard.moveActivePeg(secondPeg.color(), secondPeg.number(),
                3);

        moveResult = crossBoard.movePegFromBase(thirdPeg);
        bluePlayer.movePegForward(moveResult);

        assertEquals(MovePegType.TO_ACTIVE,
                moveResult.movePegType());

        assertEquals(crossBoard.getFieldByPeg(thirdPeg), crossBoard.getFieldByPeg(firstPeg));
        assertNotEquals(crossBoard.getFieldByPeg(firstPeg), crossBoard.getFieldByPeg(secondPeg));

    }
}
