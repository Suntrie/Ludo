package is.spbstu.board;

import is.spbstu.game.MoveResult;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public static final int NUMBER_OF_PEGS = 4;
    private Color color;
    private List<Peg> basePegs = new ArrayList<>();
    private List<Peg> activePegs = new ArrayList<>();
    private List<Peg> homePegs = new ArrayList<>();

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
            return basePegs.get(basePegs.size()-1);
        }
    }

    public List<Peg> getActivePegs(){
        return this.activePegs;
    }

    public List<Peg> getBasePegs(){
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
        }else if (MovePegType.TO_ACTIVE.equals(moveResult.movePegType())){
            basePegs.remove(peg);
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
