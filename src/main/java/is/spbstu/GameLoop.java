package is.spbstu;

import is.spbstu.board.*;
import is.spbstu.game.ConsoleGame;
import is.spbstu.game.Game;
import is.spbstu.game.GameCreator;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static is.spbstu.board.MovePegType.*;

public class GameLoop {

    private static final Random random = new Random();
    public final static Scanner scanner = new Scanner(System.in);

    private static Game game;

    public static void main(String[] args) {
        CommandLineArgument values = new CommandLineArgument(args);

        boolean autoPlay = values.getAutoplay();
        int handLongSideLength = values.getHandLongSideLength() == null ? CrossBoard.HAND_LONG_SIDE_LENGTH:
                values.getHandLongSideLength();
        int handShortSideWidth = values.getHandShortSideWidth() == null? CrossBoard.HAND_SHORT_SIDE_WIDTH:
                values.getHandShortSideWidth();
        int numberOfPegs = values.getNumberOfPegs() == null? Player.NUMBER_OF_PEGS: values.getNumberOfPegs();
        int numberOfPlayers = values.getNumberOfPlayers() == null? ConsoleGame.NUMBER_OF_PLAYERS : values.getNumberOfPlayers();

        game = GameCreator.createGame();
        game.initGame(handLongSideLength, handShortSideWidth,
                numberOfPegs, numberOfPlayers);

        CrossBoard board = game.getCrossBoard();
        List<Player> players = game.getPlayers();

        int i = 0;
        Player currentPlayer = players.get(i);

        while (!game.checkForWin(currentPlayer, numberOfPegs)) {
            System.out.println("-----------------------------------------");
            //System.out.println(board.toString());

            currentPlayer = players.get(i);
            System.out.println(String.format("Move of %s", currentPlayer.getColor()));

            int diceNumber = game.rollDice();
            System.out.println(String.format("Dice value %s", diceNumber));

            boolean magicDiceNumber = diceNumber == 6;

            MovePegType movePegType = null;
            boolean addNewPeg = false;

            if (magicDiceNumber) {

                addNewPeg = currentPlayer.getActivePegs().isEmpty() ||  !currentPlayer.getBasePegs().isEmpty() && askPlayerAddPeg(currentPlayer, autoPlay);

                if (addNewPeg) {

                    movePegType = board.isPossibleMoveBasePeg(currentPlayer);

                    if (Set.of(NO_SOURCE, BLOCKED).contains(movePegType)) {
                        System.out.println(movePegType.getMessage());
                        continue;
                    }
                }
            }
            Pair<Peg, Optional<Peg>> moveResult = null;

            if (addNewPeg) {
                Peg newActivePeg = currentPlayer.getAvailableBasePeg();
                currentPlayer.fromBaseToActivePegs();
                moveResult = board.moveBasePeg(newActivePeg, movePegType);
                game.checkAndMoveEatenOpponentPegToBase(moveResult.getRight());

            } else {
                if (currentPlayer.getActivePegs().isEmpty()){
                    System.out.println(String.format("Player %s couldn't move (no active pegs)",
                            currentPlayer.getColor()));
                    i= (i + 1) % players.size();
                    continue;
                };

                int pegNumber = askPlayerPegNumber(currentPlayer, diceNumber, autoPlay);
                movePegType = board.isPossibleMoveActivePeg(currentPlayer, pegNumber, diceNumber);
                if (Set.of(NO_SOURCE, BLOCKED, LAST_LINE_LIMIT_EXCESS).contains(movePegType)) {
                    System.out.println(movePegType.getMessage());
                    continue;
                }

                moveResult = board.moveActivePeg(currentPlayer.getColor(), pegNumber, diceNumber, movePegType);
                game.checkAndMoveEatenOpponentPegToBase(moveResult.getRight());
            }

            System.out.println(moveResult.getRight().isPresent()? String.format(movePegType.getMessage(),
                    moveResult.getRight().get().color(), moveResult.getRight().get().number() ): movePegType.getMessage());

            Peg affectedPeg = moveResult.getLeft();

            if (board.getPegPosition(affectedPeg)==null){
                System.out.println(String.format("Move result: peg %s reached the home", affectedPeg));
                currentPlayer.moveFromActivePegsToHome(affectedPeg);
            }else{
                System.out.println(String.format("Move result: peg %s on field %s", affectedPeg,
                        board.getPegPosition(affectedPeg)));
            }

            i = magicDiceNumber ? i : (i + 1) % players.size();
        }

        System.out.println(String.format("Winner: %s", currentPlayer.getColor()));

    }

    private static int askPlayerPegNumber(Player player, int diceNumber, boolean autoplay) {

        List<Integer> availablePegNumbers = player.getActivePegs().stream().map(Peg::number).collect(Collectors.toList());

        if (autoplay){
            return availablePegNumbers.get(random.nextInt(availablePegNumbers.size()));
        }

        String availablePegNumbersString = availablePegNumbers
                .stream().map(String::valueOf).collect(Collectors.joining());

        System.out.println(String.format("Player %s, please, input number of active peg to move for %s positions", player.getColor(), diceNumber));
        System.out.println(String.format("Available numbers: %s", availablePegNumbersString));

        while (true) {

            Integer answer = scanner.nextInt();

            if (availablePegNumbers.contains(answer)) {
                return answer;
            }

            System.out.println("Incorrect number, please, try one more time");
        }
    }

    private static boolean askPlayerAddPeg(Player player, boolean autoplay) {

        if (autoplay){
            return random.nextBoolean();
        }

        while (true) {
            System.out.println(String.format("Player %s, do you want to move one more peg from base to the board? Yes / no", player.getColor()));

            String answer = scanner.next().toLowerCase();

            if (Set.of("yes", "no").contains(answer)) {
                return "yes".equals(answer);
            }
        }
    }

    public static Game getGame() {
        return game;
    }
}
