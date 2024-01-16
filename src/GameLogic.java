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
        return (moves.size() % 2 == 0);
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
        
        int startRow = startingPosition.getRow();
int startCol = startingPosition.getCol();
int endRow = destinationPosition.getRow();
int endCol = destinationPosition.getCol();

int rowStep;
int colStep;

Piece currentPiece = getPieceAtPosition(startingPosition);
// case the current piece isn't exist
if(currentPiece == null){
    return false;
}

if ((isSecondPlayerTurn() && currentPiece.getOwner() != secondPlayer) ||
    (!isSecondPlayerTurn() && currentPiece.getOwner() != firstPlayer)) {
    return false;
}

       // case the position hasn't change
        if (startingPosition.equals(destinationPosition)) {
            return false;
        }

        // case the new position is not in the board limits
        if (endRow < 0 || endRow >= boardSize || endCol < 0 || endCol >= boardSize) {
            return false;
        }

        // case it's not in a straight line
        if (!(startRow == endRow || startCol == endCol)) {
            return false;
        }




if (endRow > startRow) {
    rowStep = 1;
} else if (endRow < startRow) {
    rowStep = -1;
} else {
    rowStep = 0;
}

if (endCol > startCol) {
    colStep = 1;
} else if (endCol < startCol) {
    colStep = -1;
} else {
    colStep = 0;
}

// Check for horizontal or vertical movement
if ((rowStep != 0 && colStep == 0) || (rowStep == 0 && colStep != 0)) {
    for (int i = 1; i <= Math.max(Math.abs(endRow - startRow), Math.abs(endCol - startCol)); i++) {
        int currentRow = startRow + i * rowStep;
        int currentCol = startCol + i * colStep;
        if (board[currentRow][currentCol] != null) {
            return false; // Path is not clear
        }
    }
} else {
    return false; // Invalid movement (not horizontal or vertical)
}
isGameFinished();
destinationPosition.isEating();
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
