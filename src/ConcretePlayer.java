public class ConcretePlayer implements Player{

    int wins;
    int player;
    int totalKills;
    int totalSteps;

    ConcretePlayer(int player, int wins, int totalKills, int totalSteps){
        this.player = player;
        this.wins = wins;
        this.totalKills = totalKills;
        this.totalSteps = totalSteps;

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

    // to check for second comprator if both players has the same ampunt of kills
    public int getKills() {
        return totalKills;
    }

    public void setKills() {
        totalKills ++;
    }

    public void updateKillsOnWin() {
        totalKills = 0;
    }

    public int getTotalSteps() {
        return totalKills;
    }

    public void setTotalSteps(int steps) {
        totalSteps += steps;
    }

    public void updateTotalStepsOnWin() {
        totalKills = 0;
    }

}
