package is.spbstu.board;

public enum MovePegType {
    BLOCKED("The target field already contains 2 pegs"),
    WITH_EAT("The peg %s of color %s was eaten"),
    NORMAL("Regular move"),
    NO_SOURCE("The peg doesn't belong to pegs of chosen source"),
    LAST_LINE_LIMIT_EXCESS("To go through the last line you shouldn't excess it's size by your movement");

    private final String message;

    MovePegType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
