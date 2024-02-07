public class Pawn extends ConcretePiece {

    private int playerName;

    Pawn(ConcretePlayer owner, int playerName, int kills, int totalSteps) {
        super("♙", owner, playerName, kills, totalSteps);
        this.playerName = playerName;
    }

    public int getPlayerName() {
        return playerName;
    }

}