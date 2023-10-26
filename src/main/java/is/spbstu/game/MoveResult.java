package is.spbstu.game;

import is.spbstu.board.MovePegType;
import is.spbstu.board.Peg;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record MoveResult(MovePegType movePegType, MoveFieldChange moveFieldChange, Peg originalPeg, Optional<Peg> eatenPeg){

    public MoveResult(@NotNull MovePegType movePegType, MoveFieldChange moveFieldChange, Peg originalPeg){
        this(movePegType, moveFieldChange, originalPeg,  Optional.empty());
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        if (movePegType.getMoveSucceeded()){
            stringBuilder.append(String.format("Peg %s moved to the field %s", originalPeg, moveFieldChange.newField()));
        }else{
            stringBuilder.append(String.format("Peg %s didn't move", originalPeg));
        }

        String message = movePegType.getMessage();

        switch (movePegType) {
            case WITH_EAT -> {
                stringBuilder.append(String.format("\n"+message, eatenPeg));
            }
            case HOME -> {
                stringBuilder.append(String.format("\n"+message, originalPeg));
            }
            default -> {
                stringBuilder.append("\n"+message);
            }
        }

        return stringBuilder.append("\n").toString();
    }
}
