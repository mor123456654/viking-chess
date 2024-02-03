public class ConcretePlayer implements Player{

    int wins;
    int player;
    int kills;

    ConcretePlayer(int player, int wins, int kills){
        this.player = player;
        this.wins = wins;
        this.kills = kills;
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

    public int getKills() {
        return kills;
    }

    public void addKills(){
        this.kills ++;
    }
}
