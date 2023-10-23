package is.spbstu.board;


import java.util.*;

public class CrossBoard {

    public static final int HAND_LONG_SIDE_LENGTH = 6;
    public static final int HAND_SHORT_SIDE_WIDTH = 3;
    private List<Field> crossFields = new ArrayList<>();

    private Map<Color, List<Field>> lastLineFieldsByColor = new HashMap<>();
    private Map<Color, Field> sourceFields = new HashMap<>();
    private Map<Color, Field> targetFields = new HashMap<>();
    private Map<Peg, Field> fieldByPeg = new HashMap<>();
    private Map<Field, List<Peg>> pegsByField = new HashMap<>();

    private Set<Peg> wentThroughZero = new HashSet<>();

    public CrossBoard() {

        boolean longSideOffset = false;
        boolean shortSideOffset = true;
        int fieldNumber = 0;

        Color[] colors = Color.values();

        for (int i = 0; i < colors.length; i++) {
            int columnNumber = 0;
            int rowNumber = 0;

            while (rowNumber <= HAND_LONG_SIDE_LENGTH - (1 + (longSideOffset ? 1 : 0))) {
                crossFields.add(new Field(fieldNumber));
                rowNumber++;
                fieldNumber++;
            }

            longSideOffset = !longSideOffset;

            while (columnNumber <= HAND_SHORT_SIDE_WIDTH - (1 + (shortSideOffset ? 1 : 0))) {
                crossFields.add(new Field(fieldNumber));
                columnNumber++;
                fieldNumber++;
            }


            Field sourceField = crossFields.get(crossFields.size() - 1);
            Field targetField = crossFields.get(crossFields.size() - 2);

            sourceFields.put(colors[i], sourceField);
            targetFields.put(colors[i], targetField);

            rowNumber = 0;

            while (rowNumber <= HAND_LONG_SIDE_LENGTH - (1 + (longSideOffset ? 1 : 0))) {
                crossFields.add(new Field(fieldNumber));
                rowNumber++;
                fieldNumber++;
            }

            longSideOffset = !longSideOffset;

            List<Field> lastLine = new ArrayList<>();
            for (int l=0; l<HAND_LONG_SIDE_LENGTH-1; l++){
                lastLine.add(new Field(l));
            }

            lastLineFieldsByColor.put(colors[i], lastLine);
        }
    }

    public Optional<Peg> moveBasePeg(Peg peg, MovePegType movePegType) {
        Color color = peg.color();
        Field sourceField = sourceFields.get(color);

        return movePeg(sourceField, peg, movePegType);
    }

    public MovePegType isPossibleMoveActivePeg(Color color, int pegNumber, int diceNumber){
        Peg targetPeg = new Peg(color, pegNumber);
        Field targetField = getOffsetFieldForPeg(diceNumber, targetPeg);
        return isPossibleMoveActivePeg(targetField, targetPeg);
    }

    public MovePegType isPossibleMoveActivePeg(Field targetField, Peg peg){

        List<Peg> fieldPegs = pegsByField.getOrDefault(targetField, new ArrayList<>());

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
    
    public Optional<Peg> movePeg(Field targetField, Peg peg, MovePegType movePegType) {

        List<Peg> fieldPegs = pegsByField.getOrDefault(targetField, new ArrayList<>());

        switch (movePegType){
            case WITH_EAT -> {
                Peg opponentPeg = fieldPegs.get(0);
                fieldByPeg.remove(opponentPeg);
                fieldPegs.clear();
                return Optional.of(opponentPeg);
            }
            case NORMAL -> {
                fieldByPeg.put(peg, targetField);

                fieldPegs.add(peg);
                pegsByField.put(targetField, fieldPegs);
                return Optional.empty();
            }
            default -> {
                return Optional.empty();
            }
        }
    }

    public Optional<Peg> moveActivePeg(Color color, int pegNumber, int diceNumber, MovePegType movePegType) {

        Peg targetPeg = new Peg(color, pegNumber);
        Field targetField = getOffsetFieldForPeg(diceNumber, targetPeg);

        return movePeg(targetField, targetPeg, movePegType);
    }

    private Field getOffsetFieldForPeg(int diceNumber, Peg targetPeg) {

        Field sourceField = sourceFields.get(targetPeg.color());

        Field previousField = fieldByPeg.get(targetPeg);


        int targetFieldNumber = previousField.number() + diceNumber;

        if(targetFieldNumber > crossFields.size()-1) {
            targetFieldNumber = targetFieldNumber % crossFields.size();
            wentThroughZero.add(targetPeg);
        }

        if ((wentThroughZero.contains(targetPeg))&&(targetFieldNumber > sourceField.number()-1)) {
            int offsetOnLastRoad = targetFieldNumber - sourceField.number(); //TODO: remove after eat + recognize type
            return new Field(offsetOnLastRoad, true);
        }

        return new Field(targetFieldNumber, false);
    }

    public Field getSourceField(Color color){
        return this.sourceFields.get(color);
    }

    public MovePegType isPossibleMoveBasePeg(Player currentPlayer) {
        
        Field targetField = getSourceField(currentPlayer.getColor());
        
        if (currentPlayer.getBasePegs().isEmpty()){
            return MovePegType.NO_SOURCE;
        }
        
        Peg peg = currentPlayer.getAvailableBasePeg();
        return isPossibleMoveActivePeg(targetField, peg);
    }
}
