package is.spbstu.game;

import is.spbstu.board.*;

import java.util.*;

public class ConsoleGame implements Game {

    private static final Random random = new Random();
    private final CrossBoard crossBoard;
    private final List<Player> players = new ArrayList<>();
    private final Map<Color, Player> playerByColor = new HashMap<>();

    public static final int NUMBER_OF_PLAYERS = 4;

    public ConsoleGame(int handLongSideLength, int handShortSideWidth, int numberOfPegs, int numberOfPlayers) {
        crossBoard = new CrossBoard(handLongSideLength, handShortSideWidth);
        for (Color color : Color.values()) {
            Player player = new Player(color, numberOfPegs);
            players.add(player);
            playerByColor.put(color, player);
            if (players.size() == numberOfPlayers) {
                break;
            }
        }
    }

    @Override
    public boolean canPlayerAddPeg(int playerIdx, int diceValue) {
        Player player = getPlayer(playerIdx);
        boolean isSourceFieldBlocked = crossBoard.isSourceFieldBlocked(player);
        return diceValue == 6 && !player.getBasePegs().isEmpty() && !isSourceFieldBlocked;
    }

    @Override
    public MoveResult makeMove(int playerIdx, int diceValue, boolean addNewPeg, int activePegNumber) {
        Player player = getPlayer(playerIdx);
        MoveResult moveResult = addNewPeg ? crossBoard.movePegFromBase(player.getPegFromBase()) :
                crossBoard.moveActivePeg(player.getColor(), activePegNumber, diceValue);
        if (moveResult.movePegType().getMoveSucceeded()) {
            player.movePegForward(moveResult);
        }
        if (Set.of(MovePegType.NORMAL_WITH_EAT,
                MovePegType.TO_ACTIVE_WITH_EAT).contains(moveResult.movePegType())) {
            Peg eatenPeg = moveResult.eatenPeg().get();
            Player opponentPlayer = playerByColor.get(eatenPeg.color());
            opponentPlayer.movePegToBase(eatenPeg);
        }
        return moveResult;
    }

    @Override
    public boolean checkForWin(int playerIdx, int numberOfPegs) {
        Player currentPlayer = getPlayer(playerIdx);
        return currentPlayer.isWinner(numberOfPegs);
    }

    @Override
    public int rollDice() {
        return random.nextInt(6) + 1;
    }


    @Override
    public boolean canPlayerMovePeg(int playerIdx) {
        Player player = getPlayer(playerIdx);
        return !player.getActivePegs().isEmpty();
    }

    @Override
    public String getPlayerName(int playerIdx) {
        return players.get(playerIdx).getColor().name();
    }

    private Player getPlayer(int playerIdx) {
        return players.get(playerIdx);
    }

    @Override
    public List<Integer> getActivePegNumbers(int playerIdx) {
        Player player = getPlayer(playerIdx);
        return player.getActivePegs().stream().map(Peg::number).toList();
    }

}
