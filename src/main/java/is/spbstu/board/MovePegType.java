package is.spbstu.board;

public enum MovePegType {
    BLOCKED("The target field already contains 2 pegs", false),
    NORMAL_WITH_EAT("The peg %s was eaten during ordinal move", true),
    NORMAL("Regular move", true),
    LAST_LINE_LIMIT_EXCESS("To go through the last line you shouldn't excess it's size by your movement", false),
    HOME("Peg %s went to the home", true),
    TO_ACTIVE("Peg went from the base to active state", true),
    TO_ACTIVE_WITH_EAT("The peg %s was eaten during peg creation", true);

    private final String message;
    private final boolean moveSucceeded;

    MovePegType(String message, boolean moveSucceeded) {
        this.message = message;
        this.moveSucceeded = moveSucceeded;
    }

    public String getMessage(){
        return message;
    }
    public boolean getMoveSucceeded(){
        return moveSucceeded;
    }
}
