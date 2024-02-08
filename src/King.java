import java.util.ArrayList;

public class King extends ConcretePiece {

    ArrayList<Position> moves = new ArrayList<>();

    King(ConcretePlayer owner, int playerName, int kills, int totalSteps) {
        super("♚", owner, playerName, kills, totalSteps);
    }

    public boolean isWon(Position a) {
        return a.isCorner();

    }

}