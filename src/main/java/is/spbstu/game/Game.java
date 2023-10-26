package is.spbstu.game;

import java.util.List;

public interface Game {

    int rollDice();

    boolean canPlayerAddPeg(int playerIdx, int diceValue);

    MoveResult makeMove(int playerIdx, int diceValue, boolean addNewPeg, int activePegNumber);

    boolean checkForWin(int playerIdx, int numberOfPegs);

    boolean canPlayerMovePeg(int playerIdx);

    String getPlayerName(int playerIdx);

    List<Integer> getActivePegNumbers(int playerIdx);
}
