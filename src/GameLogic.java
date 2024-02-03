import java.util.ArrayList;
import java.util.Stack;
import java.util.List;
import java.util.Map;

public class GameLogic implements PlayableLogic{
    ConcretePlayer won=null;
    private int boardSize = 11;
    private ConcretePiece[][] board = new ConcretePiece[boardSize][boardSize];

    private ConcretePlayer firstPlayer = new ConcretePlayer(1, 0);
    private ConcretePlayer secondPlayer = new ConcretePlayer(2, 0);;
    private ConcretePiece[] firstPiece=new ConcretePiece[13];
    private ConcretePiece[] secondPiece=new ConcretePiece[24];
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
            if(p.getOwner().isPlayerOne()){
                firstPiece[p.getId()-1].addPosition(b);
            }
            else {
                secondPiece[p.getId() - 1].addPosition(b);
            }
            prevPiece.add((ConcretePiece) getPieceAtPosition(a));
            moves.add(b);
            b.addPiece((ConcretePiece)getPieceAtPosition(b));
            board[b.getCol()][b.getRow()] = board[a.getCol()][a.getRow()];
            board[a.getCol()][a.getRow()] = null;
            if(isGameFinished()) {
                printStatistic();
                reset();
            }
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
                won=firstPlayer;
                return true;
            }
            if (checkIfKingSurrounded()) {
                secondPlayer.addWin();
                won=secondPlayer;
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
//נועה לא לגעת
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
                    // add kills
                    board[pos.getCol()][pos.getRow()].addKills();

                }

                if (isSameB(kCol, kRow + 1, getPieceAtPosition(pos).getOwner()) &&
                        isSameB(kCol, kRow - 1, getPieceAtPosition(pos).getOwner())) {
                    board[kCol][kRow] = null;
                    if (eaten.GetPosition() != null && eaten.GetPosition().equals(new Position(kCol, kRow)))
                        eaten.addPosition(new Position(kCol, kRow));
                    eatPiece.add(eaten);
                    eatmoves.add(new Position(kCol, kRow));
                    // add kills
                    board[pos.getCol()][pos.getRow()].addKills();

                }

                if (position.get(position.size() - 1).isNearWall()) {
                    // Check if the enemy piece is on the other side of the wall
                    if ((10 == kCol && 9 == pCol) || (0 == kCol && 1 == pCol) || (10 == kRow && 9 == pRow) || (0 == kRow && 1 == pRow)) {
                        board[kCol][kRow] = null;
                        if (eaten.GetPosition() != null && eaten.GetPosition().equals(new Position(kCol, kRow)))
                            eaten.addPosition(new Position(kCol, kRow));
                        eatPiece.add(eaten);
                        eatmoves.add(new Position(kCol, kRow));
                        // add kills
                        board[pos.getCol()][pos.getRow()].addKills();

                    }
                }

                if (position.get(position.size() - 1).isNearCorner()){
                    if ((9 == kCol && 8 == pCol) || (1 == kCol && 2 == pCol) || (9 == kRow && 8 == pRow) || (1 == kRow && 2 == pRow)) {
                        board[kCol][kRow] = null;
                        if (eaten.GetPosition() != null && eaten.GetPosition().equals(new Position(kCol, kRow)))
                            eaten.addPosition(new Position(kCol, kRow));
                        eatPiece.add(eaten);
                        eatmoves.add(new Position(kCol, kRow));
                        // add kills
                        board[pos.getCol()][pos.getRow()].addKills();

                    }
                }
                
                position.remove(position.size() - 1);
            }
        }
    }

    private boolean isSameB(int Col, int Row,Player owner) {
        if (Col >= 0 && Col < boardSize && Row >= 0 && Row < boardSize &&
                board[Col][Row] != null && board[Col][Row].getOwner().equals(owner) && !board[Col][Row].getType().equals("♚")) {
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
        for (int i=0;i<24;i++){
            secondPiece[i]=new Pawn(secondPlayer, (i+1), 0);
        }
        // create secondPlayer players
        board[0][3] = secondPiece[6];

        board[0][4] = secondPiece[8];
        board[0][5] = secondPiece[10];
        board[0][6] = secondPiece[14];
        board[0][7] = secondPiece[16];

        board[1][5] = secondPiece[11];

        board[10][3] = secondPiece[7];
        board[10][4] = secondPiece[9];
        board[10][5] = secondPiece[13];
        board[10][6] = secondPiece[15];
        board[10][7] = secondPiece[17];

        board[9][5] = secondPiece[12];


        board[3][0] = secondPiece[0];
        board[4][0] = secondPiece[1];
        board[5][0] = secondPiece[2];
        board[6][0] = secondPiece[3];
        board[7][0] = secondPiece[4];

        board[5][9] = secondPiece[18];


        board[3][10] = secondPiece[19];
        board[4][10] = secondPiece[20];
        board[5][10] = secondPiece[21];
        board[6][10] = secondPiece[22];
        board[7][10] = secondPiece[23];

        board[5][1] = secondPiece[5];
        
        // create firstPlayer players
        for (int i=0;i<13;i++){
            if(i!=6)
                firstPiece[i]=new Pawn(firstPlayer, (i+1), 0);
            else
                firstPiece[i]=new King(firstPlayer, (i+1), 0);
        }

        board[5][3] = firstPiece[0];
        board[4][4] = firstPiece[1];
        board[5][4] = firstPiece[2];
        board[6][4] = firstPiece[3];

        board[3][5] = firstPiece[4];
        board[4][5] = firstPiece[5];
        board[6][5] = firstPiece[7];
        board[7][5] = firstPiece[8];

        board[4][6] = firstPiece[9];
        board[5][6] = firstPiece[10];
        board[6][6] = firstPiece[11];
        board[5][7] = firstPiece[12];

        board[5][5] = firstPiece[6];

    }

    public void printStars() {
        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.print("\n");
    }

    // public void getDefanceNames(String s, int comp) {
    //     for (int i = 0; i < firstPiece.length; i++) {
    //         if (firstPiece[i].position.size() > 0) {
    //             if (firstPiece[i].getId() != 7) {
    //                 s = "D" + firstPiece[i].getId();
    //             } else {
    //                 s = "K" + firstPiece[i].getId();
    //             }
    //             if ( comp == 1 ){
    //                 firstPiece[i].printMoves(s);
    //             }
    //             if ( comp == 2 ){
    //                 if( firstPiece[i].getKills() > 0 ) {
    //                     firstPiece[i].printKills(s);
    //                 }
    //             }
    //         }
    //     }
    // }

    // public void getAttackNames(String s, int comp) {
    //     for (int i = 0; i < secondPiece.length ; i++) {
    //         if (secondPiece[i].position.size() > 0) {
    //             s = "A" + secondPiece[i].getId();
    //             if ( comp == 1 ) {
    //                 secondPiece[i].printMoves(s);
    //             }
    //             if ( comp == 2 ) {
    //                 if( secondPiece[i].getKills() > 0 ) {
    //                     secondPiece[i].printKills(s);
    //                 }
    //             }
    //         }
    //     }
    // }

    public void getNames(String s, int comp,  ConcretePiece[] piecesArr) {
        for (int i = 0; i < piecesArr.length ; i++) {
            if (piecesArr[i].position.size() > 0) {
                if (piecesArr[i].getOwner().isPlayerOne()){
                    s = "A" + piecesArr[i].getId();
                }
                else {
                    if (piecesArr[i].getId() != 7) {
                        s = "D" + piecesArr[i].getId();
                    } else {
                        s = "K" + piecesArr[i].getId();
                    }
                }

                if ( comp == 1 ) {
                    piecesArr[i].printMoves(s);
                }
                if ( comp == 2 ) {
                    if( piecesArr[i].getKills() > 0 ) {
                        piecesArr[i].printKills(s);
                    }
                }
            }
        }
    }


    public void printStatistic() {
        ComperatorBySteps comparator1 = new ComperatorBySteps();
        ComperatorByKills comparator2 = new ComperatorByKills();


        ComperatorByStepedSquare comparator4 = new ComperatorByStepedSquare();
        // Sorting firstPiece array
        for (int i = 0; i < firstPiece.length; i++) {
            for (int j = 0; j < firstPiece.length - i - 1; j++) {
                if (comparator1.compare(firstPiece[j], firstPiece[j + 1]) > 0) {
                    ConcretePiece temp = firstPiece[j];
                    firstPiece[j] = firstPiece[j + 1];
                    firstPiece[j + 1] = temp;
                }
            }
        }

        // Sorting secondPiece array
        for (int i = 0; i < secondPiece.length; i++) {
            for (int j = 0; j < secondPiece.length - i - 1; j++) {
                if (comparator1.compare(secondPiece[j], secondPiece[j + 1]) > 0) {
                    ConcretePiece temp = secondPiece[j];
                    secondPiece[j] = secondPiece[j + 1];
                    secondPiece[j + 1] = temp;
                }
            }
        }

        String s="";
        if (won.isPlayerOne()) {
            // Printing firstPiece array
            getNames(s, 1, firstPiece);
            // Printing secondPiece array
            getNames(s, 1, secondPiece);
        } else {
            // Printing secondPiece array
            getNames(s, 1, secondPiece);
            // Printing firstPiece array
            getNames(s, 1, firstPiece);
        }

        printStars();

        ConcretePiece[] sharedPieces = new ConcretePiece[firstPiece.length + secondPiece.length];
 
        for (int i = 0; i < firstPiece.length; i++) {
            sharedPieces[i] = firstPiece[i];
        }

        for (int i = 0; i < secondPiece.length; i++) {
            sharedPieces[firstPiece.length + i] = secondPiece[i];
        }

        s="";
        for (int i = 0; i < sharedPieces.length; i++) {
            for (int j = 0; j < sharedPieces.length - i - 1; j++) {
                if (comparator2.compare(sharedPieces[j], sharedPieces[j + 1]) > 0) {
                    ConcretePiece temp = sharedPieces[j];
                    sharedPieces[j] = sharedPieces[j + 1];
                    sharedPieces[j + 1] = temp;
                }
            }
        }

        getNames(s, 2, sharedPieces);
        // if (won.isPlayerOne()) {
        //     // Printing firstPiece array
        //     getDefanceNames(s, 2);

        //     // Printing secondPiece array
        //     getAttackNames(s, 2);
        // } else {
        //     // Printing secondPiece array
        //     getAttackNames(s, 2);
        //     // Printing firstPiece array
        //     getDefanceNames(s, 2);
        // }


        printStars();
        //להוסיף את סעיף 3

        printStars();
        //נועה שימי לב זה סעיף 4 תהיי עירנית וחדה על המטרה
       // ..המון הצלחה במילואים
        // Position[] pos= new Position[boardSize*boardSize];
        // for (int i=0;i<boardSize;i++){
        //     for (int j=0;j<boardSize;j++){
        //         for (int m = 0; i < firstPiece.length-i-1; i++) {
        //             for (int n = 0; j < firstPiece.length - j - 1; j++) {
        //                 if (comparator4.compare(board[i][j],(board[i+1][j]) > 0) {
        //                     ConcretePiece temp = firstPiece[j];
        //                     firstPiece[j] = firstPiece[j + 1];
        //                     firstPiece[j + 1] = temp;
        //                 }
        //             }

        //     }
        // }
    }

}

