package is.spbstu;

import is.spbstu.game.Game;
import is.spbstu.game.GameCreator;
import is.spbstu.game.MoveResult;
import org.kohsuke.args4j.CmdLineException;

import java.util.*;
import java.util.stream.Collectors;

public class GameLoop {

    private static final Random random = new Random();
    public static final Scanner scanner = new Scanner(System.in);
    private static Game game;

    public static void main(String[] args) throws CmdLineException {

        CommandLineArgument arguments = new CommandLineArgument(args);

        game = GameCreator.createGame(arguments.getHandLongSideLength(),
                arguments.getHandShortSideWidth(),
                arguments.getNumberOfPegs(),
                arguments.getNumberOfPlayers());

        int currentPlayerIdx = 0;

        while (!game.checkForWin(currentPlayerIdx, arguments.getNumberOfPegs())) {

            String currentPlayer = game.getPlayerName(currentPlayerIdx);
            int diceValue = game.rollDice();

            System.out.println("-----------------------------------------");
            System.out.printf("Move of %s%n",currentPlayer);
            System.out.printf("Dice value %s%n", diceValue);

            boolean addNewPeg = false;

            if (game.canPlayerAddPeg(currentPlayerIdx, diceValue)){
               addNewPeg = askPlayerAddPeg(currentPlayer, arguments.getAutoplay());
            }

            int activePeg = -1;

            if ((!addNewPeg)&&(game.canPlayerMovePeg(currentPlayerIdx))){
                activePeg = askPlayerPegNumber(currentPlayer,game.getActivePegNumbers(currentPlayerIdx), diceValue, arguments.getAutoplay());
            }

            if (!addNewPeg && activePeg==-1){
                System.out.println("Sorry, the move is impossible");
                currentPlayerIdx=getNextPlayerIdx(currentPlayerIdx, arguments);
                continue;
            }

            MoveResult moveResult = game.makeMove(currentPlayerIdx, diceValue, addNewPeg, activePeg);

            System.out.printf(moveResult.toString());

            currentPlayerIdx = diceValue==6 ? currentPlayerIdx : getNextPlayerIdx(currentPlayerIdx, arguments);
        }

        System.out.printf("Winner: %s%n", game.getPlayerName(currentPlayerIdx));

    }

    private static int getNextPlayerIdx(int currentPlayerIdx, CommandLineArgument arguments) {
        return (currentPlayerIdx + 1) % arguments.getNumberOfPlayers();
    }

    private static int askPlayerPegNumber(String player, List<Integer> availablePegNumbers, int diceNumber, boolean autoplay) {

        if (autoplay) {
            return availablePegNumbers.get(random.nextInt(availablePegNumbers.size()));
        }

        String availablePegNumbersString = availablePegNumbers
                .stream().sorted(Comparator.naturalOrder()).map(String::valueOf).collect(Collectors.joining());

        System.out.printf("Player %s, please, input number of active peg to move for %s positions%n", player, diceNumber);
        System.out.printf("Available numbers: %s%n", availablePegNumbersString);

        while (true) {

            int answer = scanner.nextInt();

            if (availablePegNumbers.contains(answer)) {
                return answer;
            }

            System.out.println("Incorrect number, please, try one more time");
        }
    }

    private static boolean askPlayerAddPeg(String player, boolean autoplay) {

        if (autoplay) {
            return random.nextBoolean();
        }

        while (true) {
            System.out.printf("Player %s, do you want to move one more peg from base to the board? Yes / no%n", player);

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
