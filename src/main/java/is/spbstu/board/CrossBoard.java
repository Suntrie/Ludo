package is.spbstu.board;


import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class CrossBoard {

    public static final int HAND_LONG_SIDE_LENGTH = 6;
    public static final int HAND_SHORT_SIDE_WIDTH = 3;

    private Map<Color, Map<Field, List<Peg>>> lastLineFieldsByColor = new HashMap<>();
    private Map<Color, Field> sourceFields = new HashMap<>();
    private Map<Color, Field> targetFields = new HashMap<>();
    private Map<Peg, Field> fieldByPeg = new HashMap<>();
    private Map<Field, List<Peg>> pegsByField = new LinkedHashMap<>();
    private Set<Peg> wentThroughZero = new HashSet<>();
    private Map<Color, List<Peg>> homeTrianglesByColor = new HashMap<>();

    private int handLongSideLength;
    private int handShortSideWidth;

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

            Field lastField = null;
            Field previousField = null;

            while (columnNumber <= handShortSideWidth - (1 + (shortSideOffset ? 1 : 0))) {
                Field field = new Field(fieldNumber);
                pegsByField.put(field, new ArrayList<>());
                columnNumber++;
                fieldNumber++;
                previousField = lastField;
                lastField = field;
            }

            Field sourceField = lastField;
            Field targetField = previousField;

            sourceFields.put(colors[i], sourceField);
            targetFields.put(colors[i], targetField);

            rowNumber = 0;

            while (rowNumber <= handLongSideLength - (1 + (longSideOffset ? 1 : 0))) {
                pegsByField.put(new Field(fieldNumber), new ArrayList<>());
                rowNumber++;
                fieldNumber++;
            }

            longSideOffset = !longSideOffset;

            Map<Field, List<Peg>> lastLineMap = new HashMap<>();
            for (int l = 0; l < handLongSideLength + 1; l++) {
                lastLineMap.put(new Field(l, true), new ArrayList<>());
            }

            lastLineFieldsByColor.put(colors[i], lastLineMap);
            homeTrianglesByColor.put(colors[i], new ArrayList<>());
        }

        this.handLongSideLength = handLongSideLength;
        this.handShortSideWidth = handShortSideWidth;
    }

    public Pair<Peg, Optional<Peg>> moveBasePeg(Peg peg, MovePegType movePegType) {
        Color color = peg.color();
        Field sourceField = sourceFields.get(color);

        return movePeg(Pair.of(Optional.of(sourceField), false), peg, movePegType, Optional.empty());
    }

    public MovePegType isPossibleMoveActivePeg(Player currentPlayer, int pegNumber, int diceNumber) {
        Peg targetPeg = new Peg(currentPlayer.getColor(), pegNumber);

        if (!currentPlayer.getActivePegs().contains(targetPeg)) {
            return MovePegType.NO_SOURCE;
        }

        Pair<Optional<Field>, Boolean> targetFieldWithZeroMark = getOffsetFieldForPeg(diceNumber, targetPeg);
        return targetFieldWithZeroMark.getLeft().isPresent() ? isPossibleMovePeg(targetFieldWithZeroMark.getLeft().get(), targetPeg)
                : MovePegType.LAST_LINE_LIMIT_EXCESS;
    }

    public MovePegType isPossibleMovePeg(Field targetField, Peg peg) {

        int lastLineSize = lastLineFieldsByColor.get(peg.color()).size();
        if (targetField.isLastLineField() && targetField.number()> lastLineSize -1){
            return MovePegType.LAST_LINE_LIMIT_EXCESS;
        }

        List<Peg> fieldPegs = targetField.isLastLineField()?
                lastLineFieldsByColor.get(peg.color()).get(targetField): pegsByField.get(targetField);

        if (!fieldPegs.isEmpty()) {
            if (fieldPegs.size() == 2) {
                return MovePegType.BLOCKED;
            }
            if (fieldPegs.stream().anyMatch(previousPeg -> !peg.color().equals(previousPeg.color()))) {
                return MovePegType.WITH_EAT;
            }
        }

        return MovePegType.NORMAL;
    }

    public Pair<Peg, Optional<Peg>> movePeg(Pair<Optional<Field>, Boolean> destinationFieldWithZeroMark, Peg peg, MovePegType movePegType,
                                            Optional<Field> sourceFieldOpt) {

        Field destinationField = destinationFieldWithZeroMark.getLeft().get();
        Map<Field, List<Peg>> lastLinePegsByField = lastLineFieldsByColor.get(peg.color());

        List<Peg> destinationFieldPegs = destinationField.isLastLineField()?
                lastLinePegsByField.get(destinationField): pegsByField.get(destinationField);

        List<Peg> sourceFieldPegs = sourceFieldOpt.isPresent()?  sourceFieldOpt.get().isLastLineField()?
                lastLinePegsByField.get(sourceFieldOpt.get()): pegsByField.get(sourceFieldOpt.get()):
                new ArrayList<>();

        if (destinationFieldWithZeroMark.getRight()){
            wentThroughZero.add(peg);
        }

        switch (movePegType) {
            case WITH_EAT -> {
                Peg opponentPeg = destinationFieldPegs.get(0);
                fieldByPeg.remove(opponentPeg);
                fieldByPeg.put(peg, destinationField);
                sourceFieldPegs.remove(peg);
                destinationFieldPegs.clear();
                destinationFieldPegs.add(peg);
                return Pair.of(peg, Optional.of(opponentPeg));
            }
            case NORMAL -> {
                // last line only relevant option
                fieldByPeg.put(peg, destinationField);
                sourceFieldPegs.remove(peg);
                destinationFieldPegs.add(peg);

                if (destinationField.isLastLineField() &&
                        destinationField.number() ==lastLinePegsByField.keySet().size()-1){
                    homeTrianglesByColor.get(peg.color()).add(peg);
                    fieldByPeg.remove(peg);
                    destinationFieldPegs.remove(peg);
                    wentThroughZero.remove(peg);
                }
                return Pair.of(peg, Optional.empty());
            }
            default -> throw new IllegalStateException("Before calling this method you should " +
                    "check possibility of the move");
        }
    }

    public Pair<Peg, Optional<Peg>> moveActivePeg(Color color, int pegNumber, int diceNumber, MovePegType movePegType) {

        Peg targetPeg = new Peg(color, pegNumber);
        Pair<Optional<Field>, Boolean> targetFieldWithZeroMark = getOffsetFieldForPeg(diceNumber, targetPeg);

        if (targetFieldWithZeroMark.getLeft().isEmpty()) {
            throw new IllegalStateException("Before moving the peg, check for possibility of the move should be done");
        }

        return movePeg(targetFieldWithZeroMark, targetPeg, movePegType, Optional.ofNullable(fieldByPeg.get(targetPeg)));
    }

    private Pair<Optional<Field>, Boolean> getOffsetFieldForPeg(int diceNumber, Peg targetPeg) {

        Field sourceField = sourceFields.get(targetPeg.color());
        Field previousField = fieldByPeg.get(targetPeg);
        boolean wentThroughZeroFlg = wentThroughZero.contains(targetPeg);

        int targetFieldNumber = previousField.number() + diceNumber;

        if ((!wentThroughZeroFlg) && (targetFieldNumber > pegsByField.keySet().size() - 1)) {
            targetFieldNumber = targetFieldNumber % pegsByField.keySet().size();
            wentThroughZeroFlg = true;
        }

        if ((wentThroughZeroFlg && !previousField.isLastLineField())
                && (targetFieldNumber >= sourceField.number() - 1)) {
            int offsetOnLastRoad = targetFieldNumber - (sourceField.number() - 1);
            return Pair.of(Optional.of(new Field(offsetOnLastRoad, true)), true);
        }

        if (previousField.isLastLineField()) {
            if (targetFieldNumber > lastLineFieldsByColor.get(targetPeg.color()).size() - 1) {
                return Pair.of(Optional.empty(), false);
            } else {
                return Pair.of(Optional.of(new Field(targetFieldNumber, true)), false);
            }
        }

        return Pair.of(Optional.of(new Field(targetFieldNumber, false)),wentThroughZeroFlg);
    }

    public Field getSourceField(Color color) {
        return this.sourceFields.get(color);
    }

    public MovePegType isPossibleMoveBasePeg(Player currentPlayer) {

        Field targetField = getSourceField(currentPlayer.getColor());

        if (currentPlayer.getBasePegs().isEmpty()) {
            return MovePegType.NO_SOURCE;
        }

        Peg peg = currentPlayer.getAvailableBasePeg();
        return isPossibleMovePeg(targetField, peg);
    }

    public List<Peg> getHomeTrianglesByColor(Color color){
        return homeTrianglesByColor.get(color);
    }

    public Field getPegPosition(Peg peg){
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
}
