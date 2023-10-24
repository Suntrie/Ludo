package is.spbstu.board;

public record Field(int number, boolean isLastLineField) {

    public Field(int number){
        this(number, false);
    }

}
