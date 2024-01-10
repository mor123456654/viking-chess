public class ConcretePiece implements Piece {

    private String type;
    private Player owner;


    ConcretePiece(String type,Player owner){
        this.type = type;
        this.owner = owner;

    }

    @java.lang.Override
    public Player getOwner() {
        return owner;
    }

    @java.lang.Override
    public String getType() {
        return type;
    }
}
