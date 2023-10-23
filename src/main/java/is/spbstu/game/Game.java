package is.spbstu.game;

import is.spbstu.board.Color;
import is.spbstu.board.CrossBoard;
import is.spbstu.board.Player;

import java.util.List;
import java.util.Map;

public interface Game {

    void initGame();
    List<Player> getPlayers();
    int rollDice();

    CrossBoard getCrossBoard();
    Map<Color, Player> getPlayerByColor();
}
