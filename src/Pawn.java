public class Pawn extends ConcretePiece {

    private int playerName;

    Pawn(ConcretePlayer owner, int playerName) {
        super("♙" ,owner,playerName);
        this.playerName = playerName;
    }

    public int getPlayerName(){
        return playerName;
    }


}
