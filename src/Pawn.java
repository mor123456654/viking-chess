public class Pawn extends ConcretePiece {

    private int playerName;

    Pawn(ConcretePlayer owner, int playerName, int kills) {
        super("♙" ,owner,playerName, kills);
        this.playerName = playerName;
    }

    public int getPlayerName(){
        return playerName;
    }


}
