package is.spbstu;

import is.spbstu.board.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void setMultiplePegsOfDifferentColorOnSameFieldWithEat(){
        CrossBoard crossBoard = new CrossBoard(3 ,3);
        Peg bluePeg = new Peg(Color.BLUE, 1);
        crossBoard.moveBasePeg(bluePeg, MovePegType.NORMAL);
        assertEquals(new Field(25, false), crossBoard.getPegPosition(bluePeg));

        Peg redPeg = new Peg(Color.RED, 1);
        crossBoard.moveBasePeg(redPeg, MovePegType.NORMAL);
        crossBoard.moveActivePeg(redPeg.color(), redPeg.number(),
                7, MovePegType.WITH_EAT);
        assertEquals(new Field(25, false), crossBoard.getPegPosition(redPeg));

        assertEquals(1, crossBoard.getPegsByField().get(new Field(25, false)).size());
    }

    @Test
    public void makeCircle(){

        CrossBoard crossBoard = new CrossBoard(3 ,3);
        Peg peg = new Peg(Color.BLUE, 1);
        crossBoard.moveBasePeg(peg, MovePegType.NORMAL);

        assertEquals(new Field(25, false), crossBoard.getPegPosition(peg));
        crossBoard.moveActivePeg(peg.color(), peg.number(),
                5, MovePegType.NORMAL);

        assertEquals(new Field(2, false), crossBoard.getPegPosition(peg));
        assertTrue(crossBoard.getWentThroughZero().contains(peg));
    }

    @Test
    public void goToLastLine(){

        CrossBoard crossBoard = new CrossBoard(3 ,3);
        Peg peg = new Peg(Color.BLUE, 1);
        crossBoard.moveBasePeg(peg, MovePegType.NORMAL);

        assertEquals(new Field(25, false), crossBoard.getPegPosition(peg));
        crossBoard.moveActivePeg(peg.color(), peg.number(),
                27, MovePegType.NORMAL);

        assertEquals(new Field(0, true), crossBoard.getPegPosition(peg));
        assertTrue(crossBoard.getWentThroughZero().contains(peg));
    }

    @Test
    public void goToLastLineWithExcessWithinLimit(){

        CrossBoard crossBoard = new CrossBoard(3 ,3);
        Peg peg = new Peg(Color.BLUE, 1);
        crossBoard.moveBasePeg(peg, MovePegType.NORMAL);

        assertEquals(new Field(25, false), crossBoard.getPegPosition(peg));
        crossBoard.moveActivePeg(peg.color(), peg.number(),
                29, MovePegType.NORMAL);

        assertEquals(new Field(2, true), crossBoard.getPegPosition(peg));
        assertTrue(crossBoard.getWentThroughZero().contains(peg));
    }


    @Test
    public void goToLastLineWithGoToHome(){

        CrossBoard crossBoard = new CrossBoard(3 ,3);
        Peg peg = new Peg(Color.BLUE, 1);
        crossBoard.moveBasePeg(peg, MovePegType.NORMAL);

        assertEquals(new Field(25, false), crossBoard.getPegPosition(peg));
        crossBoard.moveActivePeg(peg.color(), peg.number(),
                30, MovePegType.NORMAL);

        assertNull(crossBoard.getPegPosition(peg));
        assertTrue(crossBoard.getHomeTrianglesByColor(Color.BLUE).contains(peg));
    }


    @Test
    public void goToLastLineWithGoToHomeIn2Steps(){

        CrossBoard crossBoard = new CrossBoard(3 ,3);
        Peg peg = new Peg(Color.BLUE, 1);
        crossBoard.moveBasePeg(peg, MovePegType.NORMAL);

        assertEquals(new Field(25, false), crossBoard.getPegPosition(peg));
        crossBoard.moveActivePeg(peg.color(), peg.number(),
                29, MovePegType.NORMAL);
        crossBoard.moveActivePeg(peg.color(), peg.number(),
                1, MovePegType.NORMAL);
        assertNull(crossBoard.getPegPosition(peg));
        assertTrue(crossBoard.getHomeTrianglesByColor(Color.BLUE).contains(peg));
    }

    @Test
    public void goToLastLineWithExcessOutsideLimit(){

        CrossBoard crossBoard = new CrossBoard(3 ,3);
        Peg peg = new Peg(Color.BLUE, 1);
        crossBoard.moveBasePeg(peg, MovePegType.NORMAL);

        assertEquals(new Field(25, false), crossBoard.getPegPosition(peg));
        crossBoard.moveActivePeg(peg.color(), peg.number(),
                29, MovePegType.NORMAL);
        assertThrows(IllegalStateException.class, ()-> crossBoard.moveActivePeg(peg.color(), peg.number(),
                2, MovePegType.NORMAL));
    }

    @Test
    public void setMultiplePegsOfSameColor2Block(){

        Player bluePlayer = new Player(Color.BLUE, 3);
        CrossBoard crossBoard = new CrossBoard(3 ,3);

        Peg firstPeg = new Peg(Color.BLUE, 0);
        crossBoard.moveBasePeg(firstPeg, MovePegType.NORMAL);
        bluePlayer.fromBaseToActivePegs();
        crossBoard.moveActivePeg(firstPeg.color(), firstPeg.number(),
                3, MovePegType.NORMAL);

        Peg secondPeg = new Peg(Color.BLUE, 1);
        crossBoard.moveBasePeg(secondPeg, MovePegType.NORMAL);
        bluePlayer.fromBaseToActivePegs();
        crossBoard.moveActivePeg(secondPeg.color(), secondPeg.number(),
                3, MovePegType.NORMAL);

        assertEquals(crossBoard.getFieldByPeg(firstPeg), crossBoard.getFieldByPeg(secondPeg));

        Peg thirdPeg = new Peg(Color.BLUE, 2);
        crossBoard.moveBasePeg(thirdPeg, MovePegType.NORMAL);
        bluePlayer.fromBaseToActivePegs();
        assertEquals(MovePegType.BLOCKED,
                crossBoard.isPossibleMoveActivePeg(bluePlayer,  2, 3));

        Player redPlayer = new Player(Color.RED, 3);
        Peg redPeg = new Peg(Color.RED, 2);
        crossBoard.moveBasePeg(redPeg, MovePegType.NORMAL);
        redPlayer.fromBaseToActivePegs();
        assertEquals(MovePegType.BLOCKED,
                crossBoard.isPossibleMoveActivePeg(redPlayer, 2, 10));

    }

    @Test
    public void setMultiplePegsOfSameColor2BlockOnSourceField(){

        Player bluePlayer = new Player(Color.BLUE, 3);
        CrossBoard crossBoard = new CrossBoard(3 ,3);

        Peg firstPeg = new Peg(Color.BLUE, 2);
        crossBoard.moveBasePeg(firstPeg, MovePegType.NORMAL);
        bluePlayer.fromBaseToActivePegs();

        Peg secondPeg = new Peg(Color.BLUE, 1);
        crossBoard.moveBasePeg(secondPeg, MovePegType.NORMAL);
        bluePlayer.fromBaseToActivePegs();

        assertEquals(MovePegType.BLOCKED,
                crossBoard.isPossibleMoveBasePeg(bluePlayer));

        Player redPlayer = new Player(Color.RED, 3);
        Peg redPeg = new Peg(Color.RED, 2);
        crossBoard.moveBasePeg(redPeg, MovePegType.NORMAL);
        redPlayer.fromBaseToActivePegs();
        assertEquals(MovePegType.BLOCKED,
                crossBoard.isPossibleMoveActivePeg(redPlayer,  2, 7));

        assertEquals(MovePegType.NORMAL,
                crossBoard.isPossibleMoveActivePeg(bluePlayer,  2, 2));
        crossBoard.moveActivePeg(firstPeg.color(), firstPeg.number(),
                2, MovePegType.NORMAL);

        assertEquals(MovePegType.NORMAL,
                crossBoard.isPossibleMoveBasePeg(bluePlayer));
        Peg thirdPeg = new Peg(Color.BLUE, 0);
        crossBoard.moveBasePeg(thirdPeg, MovePegType.NORMAL);
        bluePlayer.fromBaseToActivePegs();

        assertEquals(crossBoard.getFieldByPeg(thirdPeg), crossBoard.getFieldByPeg(secondPeg));
        assertNotEquals(crossBoard.getFieldByPeg(firstPeg), crossBoard.getFieldByPeg(thirdPeg));
    }
}
