package is.spbstu;

import is.spbstu.board.*;
import is.spbstu.game.Game;
import is.spbstu.game.GameCreator;

import java.util.*;
import java.util.stream.Collectors;

import static is.spbstu.board.MovePegType.*;

public class GameLoop {

    public final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Game game = GameCreator.createGame();
        game.initGame();

        CrossBoard board = game.getCrossBoard();
        List<Player> players = game.getPlayers();

        int i = 0;

        while (true) {
            System.out.println("-----------------------------------------");

            Player currentPlayer = players.get(i);
            System.out.println(String.format("Move of %s", currentPlayer.getColor()));

            int diceNumber = game.rollDice();
            boolean magicDiceNumber = diceNumber == 6;

            boolean makeNewActivePeg = false;
            MovePegType movePegType = null;

            if (magicDiceNumber) {

                movePegType = board.isPossibleMoveBasePeg(currentPlayer);

                if (Set.of(NO_SOURCE, BLOCKED).contains(movePegType)) {
                    continue;
                }

                makeNewActivePeg = Set.of(NORMAL, WITH_EAT).contains(movePegType) &&
                        (currentPlayer.getActivePegs().isEmpty() || askPlayerAddPeg(currentPlayer));
            }

            if (makeNewActivePeg) {
                Peg newActivePeg = currentPlayer.getAvailableBasePeg();
                currentPlayer.fromBaseToActivePegs();
                Optional<Peg> resultPegOpt = board.moveBasePeg(newActivePeg, movePegType);
                checkAndReturnEatenOpponentPegToBase(resultPegOpt, currentPlayer, game);
            } else {
                int pegNumber = askPlayerPegNumber(currentPlayer, diceNumber);
                movePegType = board.isPossibleMoveActivePeg(currentPlayer.getColor(), pegNumber, diceNumber);
                if (Set.of(NO_SOURCE, BLOCKED).contains(movePegType)) {
                    continue;
                }

                Optional<Peg> resultPegOpt = board.moveActivePeg(currentPlayer.getColor(), pegNumber, diceNumber, movePegType);
                checkAndReturnEatenOpponentPegToBase(resultPegOpt, currentPlayer, game);
            }

            i = magicDiceNumber ? i : (i + 1) % players.size();
        }

    }

    private static int askPlayerPegNumber(Player player, int diceNumber) {

        Set<Integer> availablePegNumbers = player.getActivePegs().stream().map(Peg::number).collect(Collectors.toSet());
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

    private static void checkAndReturnEatenOpponentPegToBase(Optional<Peg> resultPegOpt, Player currentPlayer, Game game) {
        if (resultPegOpt.isPresent()) {
            Peg oponnentPeg = resultPegOpt.get();
            Color oponnentColor = oponnentPeg.color();
            Player oponnentPlayer = game.getPlayerByColor().get(oponnentColor);
            oponnentPlayer.fromActiveToBasePegs(oponnentPeg);
        }
    }

    private static boolean askPlayerAddPeg(Player player) {

        while (true) {
            System.out.println(String.format("Player %s, do you want to move one more peg from base to the board? Yes / no", player.getColor()));

            String answer = scanner.next().toLowerCase();

            if (Set.of("yes", "no").contains(answer)) {
                return "yes".equals(answer);
            }
        }
    }
}
