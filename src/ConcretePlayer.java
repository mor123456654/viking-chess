public class ConcretePlayer implements Player{

    int wins;
    int player;

    ConcretePlayer(int player, int wins){
        this.player = player;
        this.wins = wins;
    }

    @Override
    public boolean isPlayerOne() {
        if ( player == 1 ) { 
            return true;
        }

        return false;

    }

    public void addWin() {
        this.wins ++;
    }

    @Override
    public int getWins() {
        return wins;
    }

    public void setWins(int winsNumber) {
        this.wins = winsNumber;
    }

}
