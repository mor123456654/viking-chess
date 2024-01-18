import java.util.ArrayList;
import java.util.Stack;
import java.util.List;
import java.util.Map;

public class GameLogic implements PlayableLogic{

    private int boardSize = 11;
    private ConcretePiece[][] board = new ConcretePiece[boardSize][boardSize];

    private ConcretePlayer firstPlayer = new ConcretePlayer(1,0);
    private ConcretePlayer secondPlayer = new ConcretePlayer(2,0);;

    List<Position> moves = new ArrayList<Position>();
    List<Position> eatmoves = new ArrayList<Position>();
    List<ConcretePiece> prevPiece = new ArrayList<ConcretePiece>();
    List<ConcretePiece> eatPiece = new ArrayList<ConcretePiece>();
    GameLogic() {
        createBoard(board);
    }

    @Override
    public boolean move(Position a, Position b) {
        if(isValid(a,b)) {
            ConcretePiece p=(ConcretePiece) getPieceAtPosition(a);
            p.addPosition(a);
            prevPiece.add((ConcretePiece) getPieceAtPosition(a));
            moves.add(b);
            board[b.getCol()][b.getRow()] = board[a.getCol()][a.getRow()];
            board[a.getCol()][a.getRow()] = null;
            if(isGameFinished())
                reset();
        isEating(b);
            return true;
        }
        
        return false;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
       return board[position.getCol()][position.getRow()];
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
        if (getLast()!=null) {
            Piece currentPiece = getPieceAtPosition(getLast());
            if (getLast().isCorner() && currentPiece.getType().equals("♚")) {
                firstPlayer.addWin();
                return true;
            }
            if (checkIfKingSurrounded()) {
                secondPlayer.addWin();
                return true;
            }
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
        board = new ConcretePiece[boardSize][boardSize];
        createBoard(board);

    }

    @Override
    public void undoLastMove() {
        if (moves.size() >= 2) {
            Position oldMove = moves.get(moves.size() - 1);
            int oldMoveCol = oldMove.getCol();
            int oldMoveRow = oldMove.getRow();
            ConcretePiece lastMoveP = prevPiece.get(prevPiece.size() - 1);
            int lastMoveCol = lastMoveP.GetPosition().get(lastMoveP.GetPosition().size()-1).getCol();
            int lastMoveRow = lastMoveP.GetPosition().get(lastMoveP.GetPosition().size()-1).getRow();
            while (!eatPiece.isEmpty() && !eatmoves.isEmpty()) {
                ConcretePiece eaten = eatPiece.get(eatPiece.size() - 1);
                Position eatMove = eatmoves.get(eatmoves.size() - 1);
                int eatMoveCol = eatMove.getCol();
                int eatMoveRow = eatMove.getRow();
                if(isAdjacent(eatMoveCol,eatMoveRow,oldMoveCol,oldMoveRow)) {
                    board[eatMoveCol][eatMoveRow] = eaten;

                    eatPiece.remove(eatPiece.size() - 1);
                    eatmoves.remove(eatmoves.size() - 1);
                }
                else break;
            }


            board[lastMoveCol][lastMoveRow] = lastMoveP;
            board[oldMoveCol][oldMoveRow] = null;
            lastMoveP.GetPosition();
            moves.remove(moves.size() - 1);
            lastMoveP.GetPosition().remove(lastMoveP.GetPosition().size()-1);
            prevPiece.remove(prevPiece.size() - 1);
        } else if (moves.size() == 1) {
            reset();
        }
    }
    private boolean isAdjacent(int col1, int row1, int col2, int row2) {
        return (Math.abs(col1 - col2) == 1 && Math.abs(row1 - row2) == 0)||(Math.abs(col1 - col2) == 0 && Math.abs(row1 - row2) == 1);
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    public Position getLast() {
        if(moves.size()>0)
        return moves.get(moves.size() - 1);
        return null;
     }

    public boolean checkIfKingSurrounded() {
        int redAround = 0;
        Position kingPosition = isKingNear();

        if (kingPosition.getCol() != -1 && kingPosition.getRow() != -1) {
            int kCol = kingPosition.getCol();
            int kRow = kingPosition.getRow();

                redAround +=  isSame(kCol + 1, kRow,secondPlayer);
                redAround += isSame(kCol - 1, kRow,secondPlayer);
                redAround += isSame(kCol, kRow + 1,secondPlayer);
                redAround += isSame(kCol, kRow - 1,secondPlayer);
                System.out.print(redAround);
                return (kingPosition.isNearWall() && redAround == 3) || (redAround == 4);
            }


        return false;
    }

    private int isSame(int Col, int Row,Player owner) {
        if (Col >= 0 && Col < boardSize && Row >= 0 && Row < boardSize &&
                board[Col][Row] != null && board[Col][Row].getOwner().equals(owner)) {
            return 1;
        }
        return 0;
    }


    public Position isKingNear() {
        Position king = new Position(-1, -1);
        if (getLast() != null) {
            int pCol = getLast().getCol();
            int pRow = getLast().getRow();


            if (pCol + 1 < boardSize && board[pCol + 1][pRow] != null) {
                if (board[pCol + 1][pRow].getType().equals("♚")) {
                    king.setPosition(pCol + 1, pRow);
                    return king;
                }
            }
            if (pCol - 1 >= 0 && board[pCol - 1][pRow] != null) {
                if (board[pCol - 1][pRow].getType().equals("♚")) {
                    king.setPosition(pCol - 1, pRow);
                    return king;
                }
            }
            if (pRow + 1 < boardSize && board[pCol][pRow + 1] != null) {
                if (board[pCol][pRow + 1].getType().equals("♚")) {
                    king.setPosition(pCol, pRow + 1);
                    return king;
                }
            }
            if (pRow - 1 >= 0 && board[pCol][pRow - 1] != null) {
                if (board[pCol][pRow - 1].getType().equals("♚")) {
                    king.setPosition(pCol, pRow - 1);
                    return king;
                }
            }
        }
        return king;
    }
    public void isEating(Position pos) {
        int pCol = pos.getCol();
        int pRow = pos.getRow();
        List<Position> position = isEnemyNear(getPieceAtPosition(pos));
        Position kingPosition = isKingNear();
        ConcretePiece eating=(ConcretePiece) getPieceAtPosition(pos);
        if (getPieceAtPosition(pos)!=null&&!getPieceAtPosition(pos).getType().equals("♚")) {
            while (!position.isEmpty()) {
                int kCol = position.get(position.size() - 1).getCol();
                int kRow = position.get(position.size() - 1).getRow();
                ConcretePiece eaten = (ConcretePiece) getPieceAtPosition(new Position(kCol, kRow));
                if (isSameB(kCol + 1, kRow, getPieceAtPosition(pos).getOwner()) &&
                        isSameB(kCol - 1, kRow, getPieceAtPosition(pos).getOwner())) {
                    board[kCol][kRow] = null;
                    if (eaten.GetPosition() != null && eaten.GetPosition().equals(new Position(kCol, kRow)))
                        eaten.addPosition(new Position(kCol, kRow));
                    eatPiece.add(eaten);
                    eatmoves.add(new Position(kCol, kRow));
                }
                if (isSameB(kCol, kRow + 1, getPieceAtPosition(pos).getOwner()) &&
                        isSameB(kCol, kRow - 1, getPieceAtPosition(pos).getOwner())) {
                    board[kCol][kRow] = null;
                    if (eaten.GetPosition() != null && eaten.GetPosition().equals(new Position(kCol, kRow)))
                        eaten.addPosition(new Position(kCol, kRow));
                    eatPiece.add(eaten);
                    eatmoves.add(new Position(kCol, kRow));
                }
                if (position.get(position.size() - 1).isNearWall()) {
                    // Check if the enemy piece is on the other side of the wall
                    if ((10 == kCol && 9 == pCol) || (0 == kCol && 1 == pCol) || (10 == kRow && 9 == pRow) || (0 == kRow && 1 == pRow)) {
                        board[kCol][kRow] = null;
                        if (eaten.GetPosition() != null && eaten.GetPosition().equals(new Position(kCol, kRow)))
                            eaten.addPosition(new Position(kCol, kRow));
                        eatPiece.add(eaten);
                        eatmoves.add(new Position(kCol, kRow));
                    }
                }
                position.remove(position.size() - 1);
            }
        }
    }
    private boolean isSameB(int Col, int Row,Player owner) {
        if (Col >= 0 && Col < boardSize && Row >= 0 && Row < boardSize &&
                board[Col][Row] != null && board[Col][Row].getOwner().equals(owner)&& !board[Col][Row].getType().equals("♚")) {
            return true;
        }
        return false;
    }
    public List<Position> isEnemyNear(Piece me) {
        List<Position> enemy = new ArrayList<>();
        if (getLast() != null) {
            int pCol = getLast().getCol();
            int pRow = getLast().getRow();


            if (pCol + 1 < boardSize && board[pCol + 1][pRow] != null && getPieceAtPosition(new Position(pCol + 1, pRow)) != null) {
                if (!me.getOwner().equals(board[pCol + 1][pRow].getOwner()) && !board[pCol + 1][pRow].getType().equals("♚")) {
                    enemy.add(new Position(pCol + 1, pRow));
                }
            }
            if (pCol - 1 >= 0 && board[pCol - 1][pRow] != null && getPieceAtPosition(new Position(pCol - 1, pRow)) != null) {
                if (!me.getOwner().equals(board[pCol - 1][pRow].getOwner()) && !board[pCol - 1][pRow].getType().equals("♚")) {
                    enemy.add(new Position(pCol - 1, pRow));
                }
            }
            if (pRow + 1 < boardSize && board[pCol][pRow + 1] != null && getPieceAtPosition(new Position(pCol, pRow + 1)) != null) {
                if (!me.getOwner().equals(board[pCol][pRow + 1].getOwner()) && !board[pCol][pRow + 1].getType().equals("♚")) {
                    enemy.add(new Position(pCol, pRow + 1));
                }
            }
            if (pRow - 1 >= 0 && board[pCol][pRow - 1] != null && getPieceAtPosition(new Position(pCol, pRow - 1)) != null) {
                if (!me.getOwner().equals(board[pCol][pRow - 1].getOwner()) && !board[pCol][pRow - 1].getType().equals("♚")) {
                    enemy.add(new Position(pCol, pRow - 1));
                }
            }
        }
            return enemy;

    }


    public boolean isValid(Position startingPosition, Position destinationPosition) {
        
        int startCol = startingPosition.getCol();
        int startRow = startingPosition.getRow();
        int endCol = destinationPosition.getCol();
        int endRow = destinationPosition.getRow();

        int ColStep;
        int RowStep;

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
        if (endCol < 0 || endCol >= boardSize || endRow < 0 || endRow >= boardSize) {
            return false;
        }

        // case it's not in a straight line
        if (!(startCol == endCol || startRow == endRow)) {
            return false;
        }

        if (endCol > startCol) {
            ColStep = 1;
        } else if (endCol < startCol) {
            ColStep = -1;
        } else {
            ColStep = 0;
        }

        if (endRow > startRow) {
            RowStep = 1;
        } else if (endRow < startRow) {
            RowStep = -1;
        } else {
            RowStep = 0;
        }
        // check is it corner
        if (((endCol==0&&endRow==0)||(endCol==10&&endRow==0)||(endCol==0&&endRow==10)||(endCol==10&&endRow==10))&&getPieceAtPosition(startingPosition).getType().equals("♙"))
            return false;
        // Check for horizontal or vertical movement
        if ((ColStep != 0 && RowStep == 0) || (ColStep == 0 && RowStep != 0)) {
            for (int i = 1; i <= Math.max(Math.abs(endCol - startCol), Math.abs(endRow - startRow)); i++) {
                int currentCol = startCol + i * ColStep;
                int currentRow = startRow + i * RowStep;
                if (board[currentCol][currentRow] != null) {
                    return false; // Path is not clear
                }
            }
        } else {
            return false; // Invalid movement (not horizontal or vertical)
        }
        return true;
    }



    public void createBoard(Piece[][] board) {
        // create secondPlayer players
        board[0][3] = new Pawn(secondPlayer, "A7");

        board[0][4] = new Pawn( secondPlayer, "A9");
        board[0][5] = new Pawn(secondPlayer, "A11");
        board[0][6] = new Pawn(secondPlayer, "A15");
        board[0][7] = new Pawn(secondPlayer, "A17");

        board[1][5] = new Pawn(secondPlayer, "A12");

        board[10][3] = new Pawn(secondPlayer, "A8");
        board[10][4] = new Pawn(secondPlayer, "A10");
        board[10][5] = new Pawn(secondPlayer, "A14");
        board[10][6] = new Pawn(secondPlayer, "A16");
        board[10][7] = new Pawn(secondPlayer, "A18");

        board[9][5] = new Pawn(secondPlayer, "A13");


        board[3][0] = new Pawn(secondPlayer, "A1");
        board[4][0] = new Pawn(secondPlayer, "A2");
        board[5][0] = new Pawn(secondPlayer, "A3");
        board[6][0] = new Pawn(secondPlayer, "A4");
        board[7][0] = new Pawn(secondPlayer, "A5");

        board[5][9] = new Pawn(secondPlayer, "A19");


        board[3][10] = new Pawn(secondPlayer, "A20");
        board[4][10] = new Pawn(secondPlayer, "A21");
        board[5][10] = new Pawn(secondPlayer, "A22");
        board[6][10] = new Pawn(secondPlayer, "A23");
        board[7][10] = new Pawn(secondPlayer, "A24");

        board[5][1] = new Pawn(secondPlayer, "A6");
        
        // create firstPlayer players
        board[5][3] = new Pawn(firstPlayer, "D1");
        board[4][4] = new Pawn(firstPlayer, "D2");
        board[5][4] = new Pawn(firstPlayer, "D3");
        board[6][4] = new Pawn(firstPlayer, "D4");

        board[3][5] = new Pawn(firstPlayer, "D5");
        board[4][5] = new Pawn(firstPlayer, "D6");
        board[6][5] = new Pawn(firstPlayer, "D8");
        board[7][5] = new Pawn(firstPlayer, "D9");

        board[4][6] = new Pawn(firstPlayer, "D10");
        board[5][6] = new Pawn(firstPlayer, "D11");
        board[6][6] = new Pawn(firstPlayer, "D12");
        board[5][7] = new Pawn(firstPlayer, "D13");

        board[5][5] = new King(firstPlayer, "K7");

    }
}
