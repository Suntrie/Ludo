package is.spbstu.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {

    public static final int NUMBER_OF_PEGS = 4;
    private Color color;

    private List<Peg> basePegs = new ArrayList<>();
    private List<Peg> activePegs = new ArrayList<>();

    public Player(Color color){
        for (int i=0; i< NUMBER_OF_PEGS; i++){
            basePegs.add(new Peg(color, i));
        }
        this.color = color;
    }

    public Peg fromBaseToActivePegs(){
        Peg peg = basePegs.get(basePegs.size()-1);
        basePegs.remove(peg);
        activePegs.add(peg);
        return peg;
    }

    public Peg fromActiveToBasePegs(Peg peg){
        activePegs.remove(peg);
        basePegs.add(peg);
        return peg;
    }

    public Peg getAvailableBasePeg(){
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
}
