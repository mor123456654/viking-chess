public class Position {

    private int row;
    private int col;

    Position(int col,int row){
        this.row= row;
        this.col= col;
    }

    public void setPosition(int col, int row){
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
    public boolean isNearWall() {
        return row - 1 == -1 || col - 1 == -1 || row + 1 == 11 || col + 1 == 11;
    }
    public boolean isNearCorner() {
        return (row == 0 && col - 1 == 0) ||(row-1==0&&col==0)||(row==10&&col+1==0)||(row+1==10&&col==10);
    }
    public void isEating(){

    }


}
