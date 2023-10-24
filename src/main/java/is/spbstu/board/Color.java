package is.spbstu.board;

public enum Color {
    GREEN ("g"),
    YELLOW ("y"),
    RED("r"),
    BLUE("b");

    private final String name;

    Color(String name) {
        this.name = name;
    }
}
