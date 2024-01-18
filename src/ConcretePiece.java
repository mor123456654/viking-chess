import java.util.ArrayList;
import java.util.List;

public class ConcretePiece implements Piece {
    private String type;
    private Player owner;
    List<Position> position = new ArrayList<Position>();

    ConcretePiece(String type,Player owner){
        this.type = type;
        this.owner = owner;
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
