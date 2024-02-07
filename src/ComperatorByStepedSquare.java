import java.util.Comparator;

public class ComperatorByStepedSquare implements Comparator<Position> {

    @Override
    public int compare(Position o1, Position o2) {
        int size1 = o1.getDifPiece().size();
        int size2 = o2.getDifPiece().size();
        return Integer.compare(size1, size2);
    }
}