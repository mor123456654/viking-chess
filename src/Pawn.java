public class Pawn extends ConcretePiece {

    private String playerName;

    Pawn(ConcretePlayer owner, String playerName) {
        super("♙" ,owner);
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return playerName;
    }


}
