package is.spbstu;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;

public class CommandLineArgument {

    @Option(name="-autoplay")
    private boolean autoplay;
    @Option(name = "-handLongSideLength")
    private Integer handLongSideLength;
    @Option(name = "-handShortSideWidth")
    private Integer handShortSideWidth;
    @Option(name = "-numberOfPegs")
    private Integer numberOfPegs;
    @Option(name = "-numberOfPlayers")
    private Integer numberOfPlayers;

    public CommandLineArgument(String[] args) {

        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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