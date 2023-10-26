package is.spbstu.board;


import is.spbstu.game.MoveFieldChange;
import is.spbstu.game.MoveResult;

import java.util.*;

public class CrossBoard {

    public static final int HAND_LONG_SIDE_LENGTH = 6;
    public static final int HAND_SHORT_SIDE_WIDTH = 3;

    private Map<Color, Map<Field, List<Peg>>> lastLineFieldsByColor = new HashMap<>();
    private Map<Color, Field> generatorFieldByColor = new HashMap<>();
    private Map<Peg, Field> fieldByPeg = new HashMap<>();
    private Map<Field, List<Peg>> pegsByField = new LinkedHashMap<>();
    private Set<Peg> wentThroughZero = new HashSet<>();

    public CrossBoard(int handLongSideLength, int handShortSideWidth) {

        boolean longSideOffset = false;
        boolean shortSideOffset = true;
        int fieldNumber = 0;

        Color[] colors = Color.values();

        for (int i = 0; i < colors.length; i++) {
            int columnNumber = 0;
            int rowNumber = 0;

            while (rowNumber <= handLongSideLength - (1 + (longSideOffset ? 1 : 0))) {
                pegsByField.put(new Field(fieldNumber), new ArrayList<>());
                rowNumber++;
                fieldNumber++;
            }

            longSideOffset = !longSideOffset;


            while (columnNumber <= handShortSideWidth - (1 + (shortSideOffset ? 1 : 0))) {
                Field field = new Field(fieldNumber);
                pegsByField.put(field, new ArrayList<>());
                columnNumber++;
                fieldNumber++;
            }

            rowNumber = 0;

            while (rowNumber <= handLongSideLength - (1 + (longSideOffset ? 1 : 0))) {
                Field field = new Field(fieldNumber);
                pegsByField.put(field, new ArrayList<>());
                if (rowNumber == 0) {
                    generatorFieldByColor.put(colors[i], field);
                }

                rowNumber++;
                fieldNumber++;
            }

            longSideOffset = !longSideOffset;

            Map<Field, List<Peg>> lastLineMap = new HashMap<>();
            for (int l = 0; l < handLongSideLength; l++) {
                lastLineMap.put(new Field(l, true), new ArrayList<>());
            }

            lastLineFieldsByColor.put(colors[i], lastLineMap);
        }

    }


    public Field getSourceField(Color color) {
        return this.generatorFieldByColor.get(color);
    }


    public Field getPegPosition(Peg peg) {
        return fieldByPeg.get(peg);
    }

    public Map<Color, Map<Field, List<Peg>>> getLastLineFieldsByColor() {
        return lastLineFieldsByColor;
    }

    public Map<Field, List<Peg>> getPegsByField() {
        return pegsByField;
    }

    public Set<Peg> getWentThroughZero() {
        return wentThroughZero;
    }

    public Field getFieldByPeg(Peg peg) {
        return fieldByPeg.get(peg);
    }

    public boolean isSourceFieldBlocked(Player player) {
        Field sourceField = getSourceField(player.getColor());
        return pegsByField.get(sourceField).size() == 2;
    }

    public MoveResult movePegFromBase(Peg peg) {
        Field sourceField = generatorFieldByColor.get(peg.color());
        return movePeg(sourceField, false, true, peg, Optional.empty());
    }


    public MoveResult movePeg(Field destinationField,
                              boolean pegWentThroughZero,
                              boolean wentToActive,
                              Peg peg,
                              Optional<Field> sourceFieldOpt) {

        Map<Field, List<Peg>> lastLinePegsByField = lastLineFieldsByColor.get(peg.color());

        List<Peg> destinationFieldPegs = getPegsByField(destinationField, lastLinePegsByField);
        List<Peg> sourceFieldPegs = sourceFieldOpt.isPresent() ? getPegsByField(sourceFieldOpt.get(), lastLinePegsByField) :
                new ArrayList<>();

        if (pegWentThroughZero) {
            wentThroughZero.add(peg);
        }

        MoveFieldChange moveFieldChange = new MoveFieldChange(destinationField, pegWentThroughZero, wentToActive);
        MoveResult moveResult = prognoseMoveResult(moveFieldChange, peg);

        switch (moveResult.movePegType()) {
            case WITH_EAT -> {
                Peg opponentPeg = moveResult.eatenPeg().get();
                fieldByPeg.remove(opponentPeg);
                fieldByPeg.put(peg, destinationField);
                sourceFieldPegs.remove(peg);
                destinationFieldPegs.clear();
                destinationFieldPegs.add(peg);
                return moveResult;
            }
            case NORMAL, TO_ACTIVE -> {
                // last line only relevant option
                fieldByPeg.put(peg, destinationField);
                sourceFieldPegs.remove(peg);
                destinationFieldPegs.add(peg);
                return moveResult;
            }
            case HOME -> {
                fieldByPeg.remove(peg);
                sourceFieldPegs.remove(peg);
                wentThroughZero.remove(peg);
                return moveResult;
            }
            default -> {
                return moveResult;
            }
        }
    }

    private static boolean isHomeReached(Field destinationField, Map<Field, List<Peg>> lastLinePegsByField) {
        return destinationField.isLastLineField() &&
                destinationField.number() == lastLinePegsByField.keySet().size() - 1;
    }

    private List<Peg> getPegsByField(Field field, Map<Field, List<Peg>> lastLinePegsByField) {
        return field.isLastLineField() ?
                lastLinePegsByField.get(field) : pegsByField.get(field);
    }

    public MoveResult prognoseMoveResult(MoveFieldChange moveFieldChange, Peg peg) {

        Field targetField = moveFieldChange.newField();
        int lastLineSize = lastLineFieldsByColor.get(peg.color()).size();
        if (targetField.isLastLineField() && targetField.number() > lastLineSize - 1) {
            return new MoveResult(MovePegType.LAST_LINE_LIMIT_EXCESS, moveFieldChange, peg);
        }

        if (isHomeReached(targetField, lastLineFieldsByColor.get(peg.color()))) {
            return new MoveResult(MovePegType.HOME, moveFieldChange, peg);
        }

        List<Peg> fieldPegs = getPegsByField(targetField, lastLineFieldsByColor.get(peg.color()));

        if (!fieldPegs.isEmpty()) {
            if (fieldPegs.size() == 2) {
                return new MoveResult(MovePegType.BLOCKED, moveFieldChange, peg);
            }

            Optional<Peg> opponentPeg = fieldPegs.stream().filter(previousPeg -> !peg.color().equals(previousPeg.color()))
                    .findFirst();

            if (opponentPeg.isPresent()) {
                return new MoveResult(MovePegType.WITH_EAT, moveFieldChange, peg, opponentPeg);
            }
        }

        return new MoveResult(moveFieldChange.wentToActive()? MovePegType.TO_ACTIVE : MovePegType.NORMAL, moveFieldChange, peg);
    }

    public MoveResult moveActivePeg(Color color, int activePegNumber, int diceValue) {
        Peg peg = new Peg(color, activePegNumber);
        MoveFieldChange moveFieldChange = getDestinationFieldForPeg(peg, diceValue);
        return movePeg(moveFieldChange.newField(), moveFieldChange.wentThroughZero(),false, peg, Optional.of(fieldByPeg.get(peg)));
    }

    private MoveFieldChange getDestinationFieldForPeg(Peg targetPeg, int diceValue) {

        Field sourceField = generatorFieldByColor.get(targetPeg.color());
        Field previousField = fieldByPeg.get(targetPeg);

        boolean wentThroughZeroFlg = wentThroughZero.contains(targetPeg);
        boolean wentThroughZeroFlgNew = false;

        int targetFieldNumber = previousField.number() + diceValue;

        if ((!wentThroughZeroFlg) && (targetFieldNumber > pegsByField.keySet().size() - 1)) {
            targetFieldNumber = targetFieldNumber % pegsByField.keySet().size();
            wentThroughZeroFlgNew = true;
        }

        int shiftFieldIdx = sourceField.number() - 2;

        if (((wentThroughZeroFlg || wentThroughZeroFlgNew) && !previousField.isLastLineField())
                && (targetFieldNumber > shiftFieldIdx)) {
            int offsetOnLastRoad = targetFieldNumber - shiftFieldIdx - 1;
            return new MoveFieldChange(new Field(offsetOnLastRoad, true),
                    wentThroughZeroFlgNew);
        }

        if (previousField.isLastLineField()) {
            return new MoveFieldChange(new Field(targetFieldNumber, true), false);
        }

        return new MoveFieldChange(new Field(targetFieldNumber, false), wentThroughZeroFlgNew);
    }
}
