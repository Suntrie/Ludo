package is.spbstu.game;

import is.spbstu.board.Field;


public record MoveFieldChange(Field newField, boolean wentThroughZero, boolean wentToActive){
    public MoveFieldChange(Field newField, boolean wentThroughZero){
        this(newField, wentThroughZero, false);
    }
}
