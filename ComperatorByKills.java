import java.util.Comparator;

public class ComperatorByKills  implements Comparator<ConcretePiece> {

    @Override
    public int compare(ConcretePiece p1, ConcretePiece p2) {
        int size1 = p1.getKills();
        int size2 = p2.getKills();
        return Integer.compare(size1, size2);
    }
}
