import java.util.ArrayList;
import java.util.List;

public class Position {

    int counter;
    private int row;
    private int col;
    Piece piece = null;
    List<ConcretePiece> difPiece = new ArrayList<ConcretePiece>();

    Position(int col, int row) {
        this.row = row;
        this.col = col;
        counter = 0;
    }

    public void addPiece(ConcretePiece p) {
        if (!difPiece.contains(p))
            difPiece.add(p);
    }

    public List<ConcretePiece> getDifPiece() {
        return this.difPiece;
    }

    public void setPosition(int col, int row) {
        this.row = row;
        this.col = col;
    }

    public void setPiece(Piece piece, int col, int row) {
        this.piece = piece;
        ConcretePiece piece1 = (ConcretePiece) this.piece;
        piece1.addPosition(new Position(col, row));
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isCorner() {
        if (row == 0 && col == 0 || row == 10 && col == 10 || row == 10 && col == 0 || row == 0 && col == 10) {
            return true;
        }
        return false;
    }

    public boolean isNearWall() {
        return row - 1 == -1 || col - 1 == -1 || row + 1 == 11 || col + 1 == 11;
    }

    public boolean isNearCorner() {
        return (row == 10 && col == 9) || (row == 10 && col == 1) || (row == 9 && col == 10) || (row == 0 && col == 1)
                || (row == 0 && col == 9) || (row == 9 && col == 0) || (row == 1 && col == 0)
                || (row == 1 && col == 10);
    }

    public void addCounter() {
        counter++;
    }

    public void removeCounter() {
        counter--;
    }

    public int getCounter() {
        return counter;
    }

    public boolean isEqual(Position pos) {
        return this.row == pos.getRow() && this.col == pos.getCol();
    }

    public void printMoves() {
        System.out.println("(" + col + ", " + row + ")" + counter + " pieces");
    }
}