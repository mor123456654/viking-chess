import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic{

    private int boardSize = 11;
    private Piece[][] board = new Piece[boardSize][boardSize];

    private ConcretePlayer firstPlayer = new ConcretePlayer(1,0);
    private ConcretePlayer secondPlayer = new ConcretePlayer(2,0);;

    List<Position> moves = new ArrayList<Position>();


    GameLogic() {
        createBoard(board);
    }

    @Override
    public boolean move(Position a, Position b) {
        if(isValid(a,b)) {
            moves.add(b);
            board[b.getRow()][b.getCol()] = board[a.getRow()][a.getCol()];
            board[a.getRow()][a.getCol()] = null;
            
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
        // check of last move was king & corner
        // להוסיף מלך
        if (moves.get(moves.size() - 1).isCorner() || checkIfKingSurrounded()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return (moves.size() % 2 == 1);
    }

    @Override
    public void reset() {
        moves = new ArrayList<Position>();
        board = new Piece[boardSize][boardSize];
        createBoard(board);

    }

    @Override
    public void undoLastMove() {
        Position lastMove =moves.remove(moves.size() - 1);
        int lastMoveRow = lastMove.getRow();
        int lastMoveCol = lastMove.getCol();

        board[lastMoveRow][lastMoveCol] = getPieceAtPosition(lastMove);
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    public boolean checkIfKingSurrounded() {

        // להוסיץ מיקום המלך ולבדוק אם הוא מוקף מלמעלה ומלמטה בשחקנים של היריב

        return false;
        
    }

    public boolean isValid(Position startingPosition, Position destinationPosition) {

        Piece currentPiece = getPieceAtPosition(startingPosition);

        // case the current piece isn't exist
        if(currentPiece == null){
            return false;
       }

       // case the position hasn't change
        if (startingPosition.equals(destinationPosition)) {
            return false;
        }

        // case the new position is not in the board limits
        if (destinationPosition.getRow() < 0 || destinationPosition.getRow() >= boardSize || destinationPosition.getCol() < 0 || destinationPosition.getCol() >= boardSize) {
            return false;
        }

        // case it's not in a straight line
        if (!(startingPosition.getRow() == destinationPosition.getRow() || startingPosition.getCol() == destinationPosition.getCol())) {
            return false;
        }


        // if (startingPosition.getRow() == destinationPosition.getRow() ){
        //     int bigCol=Math.max(startingPosition.getCol(), destinationPosition.getCol());
        //     int smallCol=Math.min(startingPosition.getCol(), destinationPosition.getCol());
    
        //     for (int i=smallCol; i<=bigCol; i++) {
        //         Position check = new Position(destinationPosition.getRow(), i);
        //         Piece current = getPieceAtPosition(check);

        //         if(getPieceAtPosition(check) != null){
        //             return false;
        //        }
        //         // if(board[destinationPosition.getRow()][i]!=null) {
        //         //     return false;
        //         // }
        //     }
        // }

        // else if(startingPosition.getCol() == destinationPosition.getCol() ){
        //     int bigRow=Math.max(startingPosition.getRow(), destinationPosition.getRow());
        //     int smallRow=Math.min(startingPosition.getRow(), destinationPosition.getRow());
        
        //     for (int i=smallRow; i<=bigRow; i++) {
        //         if(board[i][destinationPosition.getCol()].getOwner() != null) {
        //             return false;
        //         }
        //     }
        // }
        
        // else {
            // for (int i=startingPosition.getRow(); i>destinationPosition.getRow(); i--) {
            //     if(board[i][destinationPosition.getCol()] != null) {
            //         return false;
            //     }
            // }
        // }

        int startRow = startingPosition.getRow();
        int startCol = startingPosition.getCol();
        int endRow = destinationPosition.getRow();
        int endCol = destinationPosition.getCol();
        
        // Check for horizontal movement
        if (startRow == endRow) {
            int step = (endCol > startCol) ? 1 : -1;
            for (int i = startCol + step; i != endCol; i += step) {
                if (board[startRow][i] != null) {
                    return false; // Path is not clear
                }
            }
        }
        // Check for vertical movement
        else if (startCol == endCol) {
            int step = (endRow > startRow) ? 1 : -1;
            for (int i = startRow + step; i != endRow; i += step) {
                if (board[i][startCol] != null) {
                    return false; // Path is not clear
                }
            }
        }
        
        return true;
    }

    public void createBoard(Piece[][] board) {

        board[5][9] = new Pawn(secondPlayer);
        board[9][5] = new Pawn(secondPlayer);
        board[1][5] = new Pawn(secondPlayer);
        board[5][1] = new Pawn(secondPlayer);

        for (int i=3; i<=7;i++){
            board[0][i] = new Pawn(secondPlayer);
            board[i][0] = new Pawn(secondPlayer);
            board[10][i] = new Pawn(secondPlayer);
            board[i][10] = new Pawn(secondPlayer);
        }


        for (int i=3; i<=7;i++){
            board[5][i] = new Pawn(firstPlayer);
            board[i][5] = new Pawn(firstPlayer);
        }

        for (int i=4; i<=6;i++){
            board[4][i] = new Pawn(firstPlayer);
            board[i][4] = new Pawn(firstPlayer);
            board[i][6] = new Pawn(firstPlayer);

        }

        board[5][5] = new King(firstPlayer);
    }
}
