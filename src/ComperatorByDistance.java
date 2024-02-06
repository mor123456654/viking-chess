import java.util.Comparator;

public class ComperatorByDistance  implements Comparator<ConcretePiece> {
    
    @Override
    public int compare(ConcretePiece p1, ConcretePiece p2) {
        int size1 = p1.getTotalSteps();
        int size2 = p2.getTotalSteps();
        return Integer.compare(size1, size2);
    }
}
