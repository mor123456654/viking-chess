import java.util.Comparator;

public class ComperatorBySteps implements Comparator<ConcretePiece> {
    @Override
    public int compare(ConcretePiece p1, ConcretePiece p2) {
        int size1 = p1.getPositions().size();
        int size2 = p2.getPositions().size();
        return Integer.compare(size1, size2);
    }
}
