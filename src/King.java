public class King extends ConcretePiece {

    private String playerName;

    King(ConcretePlayer owner, String playerName) {
        super("♚" ,owner);
        this.playerName = playerName;
    }

    public boolean isWon(Position a) {
        return a.isCorner();

    }

    public String getPlayerName(){
        return playerName;
    }

}
