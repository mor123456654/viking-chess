import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic{

    private int boardSize = 11;
    private Piece[][] board = new Piece[boardSize][boardSize];

    private ConcretePlayer firstPlayer = new ConcretePlayer(1,0);
    private ConcretePlayer secondPlayer = new ConcretePlayer(2,0);;

    List<Position> corners = new ArrayList<Position>();

    List<Position> moves = new ArrayList<Position>();


    @Override
    public boolean move(Position a, Position b) {
        if(isValid(a,b)) {
            moves.add(b);
            //לבדוק אם זה הרק השחקן הראשון ואם זה היה המלך ואם כן איך לקרוא לו
            if(b.isCorner() && firstPlayer.isPlayerOne()){
                corners.add(b);
            }
            return true;
        }
        
        return false;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
       return board[position.getRow()][position.getCol()];
    }

    @Override
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    @Override
    public Player getSecondPlayer() {
        return secondPlayer;
    }

    @Override
    public boolean isGameFinished() {
        if (checkIfKingGotAllCorners()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        if (moves.size() % 2 == 1){
            return true;
        }

        return false;
    }

    @Override
    public void reset() {
        board = new Piece[boardSize][boardSize];
        moves = new ArrayList<Position>();
    }

    @Override
    public void undoLastMove() {
        moves.remove(moves.size() - 1);
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    public boolean checkIfKingGotAllCorners() {
        Position upLeftCorner = new Position(0, 0);
        Position upRightCorner = new Position(0, 10);
        Position downLeftCorner = new Position(10, 0);
        Position downRightCorner = new Position(10, 10);

        if (corners.contains(upLeftCorner) && corners.contains(upRightCorner) && corners.contains(downLeftCorner) && corners.contains(downRightCorner) ) {
            return true;
        }

        return false;
    }

    public boolean checkIfKingSurrounded() {

        // להוסיץ מיקום המלך ולבדוק אם הוא מוקף מלמעלה ומלמטה בשחקנים של היריב

        return false;
        
    }

    public boolean isValid(Position a, Position b) {

        if (b == a) {
            return false;
        }

         boolean checkIfCurrentIsTheFirstInRow = a.getRow() == b.getRow() - 1 && a.getRow() == 0;
         boolean checkIfCurrentIsTheFirstInColumn = a.getCol() == b.getCol() - 1 && a.getCol() == 0;

         boolean checkIfCurrentIsTheLastInRow = a.getRow() == b.getRow() + 1 && a.getRow() == 10;
         boolean checkIfCurrentIsTheLastInColumn = a.getCol() == b.getCol() + 1 && a.getCol() == 10;

        //  boolean isInNextRow = a.getRow() == b.getRow() + 1;
        //  boolean isInNextColumn = a.getCol() == b.getCol() + 1;

        //  boolean isInPreviousRow = a.getRow() == b.getRow() - 1;
        //  boolean isInPreviousColumn = a.getCol() == b.getCol() - 1;

        //  boolean isInTheSameRow = a.getRow() == b.getRow();
        //  boolean isInTheSameColumn = a.getRow() == b.getRow();




        // case it is in the first place in the row/column
        // if (checkIfCurrentIsThefirstInRow || checkIfCurrentIsThefirstInColumn) {
        //     return false;
        // }

        // case it is in the last place in the in the row/column
        // else if (checkIfCurrentIsTheLastInRow || checkIfCurrentIsTheLastInColumn) {
        //     return false;
        // }

        // else if (a.getRow() == b.getRow() + 1 ) {
        //     return false;
        // }

       


        return true;
    }
}
