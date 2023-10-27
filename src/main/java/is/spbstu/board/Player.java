package is.spbstu.board;

import is.spbstu.game.MoveResult;

import java.util.*;

public class Player {

    public static final int NUMBER_OF_PEGS = 4;
    private final Color color;
    private final TreeSet<Peg> basePegs = new TreeSet<>();
    private final List<Peg> activePegs = new ArrayList<>();
    private final List<Peg> homePegs = new ArrayList<>();

    public Player(Color color, int numberOfPegs){
        for (int i=0; i< numberOfPegs; i++){
            basePegs.add(new Peg(color, i));
        }
        this.color = color;
    }

    public Peg getPegFromBase(){
        if(basePegs.isEmpty()){
            throw new IllegalStateException("Check presence basePegs before use of this method");
        }else{
            return basePegs.first();
        }
    }

    public List<Peg> getActivePegs(){
        return this.activePegs;
    }

    public Set<Peg> getBasePegs(){
        return this.basePegs;
    }

    public Color getColor(){
        return this.color;
    }

    public void movePegForward(MoveResult moveResult) {
        Peg peg = moveResult.originalPeg();

        if (MovePegType.HOME.equals(moveResult.movePegType())) {
            activePegs.remove(peg);
            homePegs.add(peg);
        }else if (Set.of(MovePegType.TO_ACTIVE,
                MovePegType.TO_ACTIVE_WITH_EAT).contains(moveResult.movePegType())){
            basePegs.pollFirst();
            activePegs.add(peg);
        }
    }

    public void movePegToBase(Peg peg) {
        activePegs.remove(peg);
        basePegs.add(peg);
    }

    public boolean isWinner(int numberOfPegs) {
        return homePegs.size()==numberOfPegs;
    }
}
