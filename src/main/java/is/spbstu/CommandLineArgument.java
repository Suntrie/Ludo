package is.spbstu;

import is.spbstu.board.CrossBoard;
import is.spbstu.board.Player;
import is.spbstu.game.ConsoleGame;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CommandLineArgument {

    @Option(name="-autoplay")
    private boolean autoplay;
    @Option(name = "-handLongSideLength")
    private Integer handLongSideLength = CrossBoard.HAND_LONG_SIDE_LENGTH;
    @Option(name = "-handShortSideWidth")
    private Integer handShortSideWidth = CrossBoard.HAND_SHORT_SIDE_WIDTH;
    @Option(name = "-numberOfPegs")
    private Integer numberOfPegs = Player.NUMBER_OF_PEGS;
    @Option(name = "-numberOfPlayers")
    private Integer numberOfPlayers = ConsoleGame.NUMBER_OF_PLAYERS;

    public CommandLineArgument(String[] args) throws CmdLineException {

        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public boolean getAutoplay(){
        return autoplay;
    }
    public Integer getHandLongSideLength(){
        return handLongSideLength;
    }
    public Integer getHandShortSideWidth(){
        return handShortSideWidth;
    }
    public Integer getNumberOfPegs(){
        return numberOfPegs;
    }
    public Integer getNumberOfPlayers(){
        return numberOfPlayers;
    }
}