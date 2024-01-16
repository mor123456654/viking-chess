public class Position {

    private int row;
    private int col;

    Position(int row,int col){
        this.row= row;
        this.col= col;
    }
    
    public void setPosition(int row, int col){
            this.row=row;
            this.col=col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isCorner() {
        if (row == 0 && col == 0 || row == 10 && col == 10 || row == 10 && col == 0||row == 0 && col == 10){
            return true;
        }

        return false;
    }

    public void isEating(){

    }

}
