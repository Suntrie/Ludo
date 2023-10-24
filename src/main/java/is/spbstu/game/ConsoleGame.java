package is.spbstu.game;

import is.spbstu.board.CrossBoard;
import is.spbstu.board.Color;
import is.spbstu.board.Peg;
import is.spbstu.board.Player;

import java.util.*;

public class ConsoleGame implements Game{

    private static final Random random = new Random();
    private CrossBoard crossBoard;
    private final List<Player> players = new ArrayList<>();
    private Map<Color, Player> playerByColor = new HashMap<>();

    public static final int NUMBER_OF_PLAYERS = 4;

    @Override
    public void initGame(int handLongSideLength, int handShortSideWidth, int numberOfPegs, int numberOfPlayers) {
        crossBoard = new CrossBoard(handLongSideLength, handShortSideWidth);
        for (Color color: Color.values()){
            Player player = new Player(color, numberOfPegs);
            players.add(player);
            playerByColor.put(color, player);
            if (players.size() == numberOfPlayers){
                break;
            }
        }
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    public Map<Color, Player> getPlayerByColorMap(){
        return playerByColor;
    }

    @Override
    public boolean checkForWin(Player currentPlayer, int numberOfPegs) {
        List<Peg> pegs = crossBoard.getHomeTrianglesByColor(currentPlayer.getColor());
        return pegs.size() == numberOfPegs;
    }

    @Override
    public int rollDice() {
        return random.nextInt(6)+1;
    }

    @Override
    public CrossBoard getCrossBoard() {
        return this.crossBoard;
    }

    @Override
    public void checkAndMoveEatenOpponentPegToBase(Optional<Peg> opponentPegOpt) {
        if (opponentPegOpt.isPresent()) {
            Peg oponnentPeg = opponentPegOpt.get();
            Color oponnentColor = oponnentPeg.color();
            Player oponnentPlayer = getPlayerByColorMap().get(oponnentColor);
            oponnentPlayer.fromActiveToBasePegs(oponnentPeg);
        }
    }


}
