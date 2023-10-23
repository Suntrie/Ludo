package is.spbstu.board;

import java.util.Objects;

public record Field(int number, boolean isLastLineField) {

    public Field(int number){
        this(number, false);
    }

}
