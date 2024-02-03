import java.util.ArrayList;
import java.util.List;

public class ConcretePiece implements Piece {
    private String type;
    private Player owner;
    private int id;
    List<Position> position = new ArrayList<Position>();

    ConcretePiece(){
    }

    ConcretePiece(String type,Player owner,int id){
        this.type = type;
        this.owner = owner;
        this.id=id;
    }

    ConcretePiece(String type,Player owner,Position pos){
        this.type = type;
        this.owner = owner;
        this.position.add(pos);
    }

    public void addPosition(Position position){
        this.position.add(position);
    }
    
    public List<Position> GetPosition(){
        return this.position;
    }

    @java.lang.Override
    public Player getOwner() {
        return owner;
    }

    @java.lang.Override
    public String getType() {
        return type;
    }

    public void printMoves(String s){
        System.out.print(s+": ");
        System.out.print("[");
        for(int i=0;i<position.size();i++){
            System.out.print("(" + position.get(i).getCol() + ", " + position.get(i).getRow() + ")");
            if (i < position.size() -1 ) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public int getId() {
        return id;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setType(String type) {
        this.type = type;
    }
    public List<Position> getPositions() {
        return this.position;
    }

    public void removeLastPosition() {
        if (!position.isEmpty()) {
            position.remove(position.size() - 1);
        }
    }

}
