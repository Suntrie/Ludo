package is.spbstu.board;

import org.jetbrains.annotations.NotNull;


public record Peg(Color color, int number) implements Comparable<Peg> {

    @Override
    public int compareTo(@NotNull Peg o) {
        return this.number - o.number;
    }
}
