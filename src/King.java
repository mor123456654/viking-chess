public class King extends ConcretePiece {

    King(ConcretePlayer owner) {
        super("King" ,owner);
    }

    public boolean isWon(Position a) {
        return a.isCorner();

    }

}
