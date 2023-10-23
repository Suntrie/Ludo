package is.spbstu.game;

import is.spbstu.board.CrossBoard;
import is.spbstu.board.Color;
import is.spbstu.board.Player;

import java.util.*;

public class ConsoleGame implements Game{

    private static final Random random = new Random();

    private CrossBoard crossBoard;
    private final List<Player> players = new ArrayList<>();
    private Map<Color, Player> playerByColor = new HashMap<>(); //TODO: LinkedTreeMap

    @Override
    public void initGame() {
        crossBoard = new CrossBoard();
        for (Color color: Color.values()){
            Player player = new Player(color);
            players.add(player);
            playerByColor.put(color, player);
        }
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    public Map<Color, Player> getPlayerByColor(){
        return playerByColor;
    }

    @Override
    public int rollDice() {
        return random.nextInt(6)+1;
    }

    @Override
    public CrossBoard getCrossBoard() {
        return this.crossBoard;
    }


}
