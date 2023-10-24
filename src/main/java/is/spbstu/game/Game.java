package is.spbstu.game;

import is.spbstu.board.Color;
import is.spbstu.board.CrossBoard;
import is.spbstu.board.Peg;
import is.spbstu.board.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Game {

    void initGame(int handLongSideLength, int handShortSideWidth, int numberOfPegs, int numberOfPlayers);
    List<Player> getPlayers();
    int rollDice();

    CrossBoard getCrossBoard();
    Map<Color, Player> getPlayerByColorMap();

    boolean checkForWin(Player currentPlayer, int numberOfPegs);

    void checkAndMoveEatenOpponentPegToBase(Optional<Peg> opponentPegOpt);
}
