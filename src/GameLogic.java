import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic {
    ConcretePlayer won = null;
    private int boardSize = 11;
    private ConcretePiece[][] board = new ConcretePiece[boardSize][boardSize];

    private ConcretePlayer firstPlayer = new ConcretePlayer(1, 0, 0, 0);
    private ConcretePlayer secondPlayer = new ConcretePlayer(2, 0, 0, 0);
    private ConcretePiece[] firstPiece = new ConcretePiece[13];
    private ConcretePiece[] secondPiece = new ConcretePiece[24];
    List<Position> moves = new ArrayList<Position>();
    List<Position> eatmoves = new ArrayList<Position>();
    List<ConcretePiece> prevPiece = new ArrayList<ConcretePiece>();
    List<ConcretePiece> eatPiece = new ArrayList<ConcretePiece>();

    GameLogic() {
        createBoard(board);
    }

    @Override
    public boolean move(Position a, Position b) {
        if (isValid(a, b)) {
            ConcretePiece p = (ConcretePiece) getPieceAtPosition(a);
            if (p.GetPosition().isEmpty()) {
                if (p.getOwner().isPlayerOne()) {
                    firstPiece[p.getId() - 1].addPosition(a);
                } else {
                    secondPiece[p.getId() - 1].addPosition(a);
                }
            }
            if (p.getOwner().isPlayerOne()) {
                firstPiece[p.getId() - 1].addPosition(b);
            } else {
                secondPiece[p.getId() - 1].addPosition(b);
            }

            // update steps for player and piece
            int steps = Math.abs(a.getRow() - b.getRow());

            if (steps == 0) {
                steps = Math.abs(a.getCol() - b.getCol());
            }

            if (p.getOwner().isPlayerOne()) {
                firstPlayer.addTotalSteps(steps);
            } else {
                secondPlayer.addTotalSteps(steps);
            }

            p.addTotalSteps(steps);

            prevPiece.add((ConcretePiece) getPieceAtPosition(a));
            moves.add(b);
            b.addPiece((ConcretePiece) getPieceAtPosition(b));
            board[b.getCol()][b.getRow()] = board[a.getCol()][a.getRow()];
            board[a.getCol()][a.getRow()] = null;
            if (isGameFinished()) {
                printStatistic();
                // reset each player kills and steps
                firstPlayer.updateKillsOnWin();
                firstPlayer.updateTotalStepsOnWin();
                secondPlayer.updateKillsOnWin();
                secondPlayer.updateTotalStepsOnWin();
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
        if (getLast() != null) {
            Piece currentPiece = getPieceAtPosition(getLast());
            if (getLast().isCorner() && currentPiece.getType().equals("♚")) {
                firstPlayer.addWin();
                won = firstPlayer;
                return true;
            }
            if (checkIfKingSurrounded()) {
                secondPlayer.addWin();
                won = secondPlayer;
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
        firstPlayer.updateKillsOnWin();
        secondPlayer.updateKillsOnWin();

    }

    @Override
    public void undoLastMove() {
        if (!moves.isEmpty()) {
            Position lastMove = moves.remove(moves.size() - 1);
            ConcretePiece movedPiece = (ConcretePiece) getPieceAtPosition(lastMove);
            Position currentPosition = movedPiece.GetPosition().get(movedPiece.GetPosition().size() - 1);
            movedPiece.GetPosition().remove(movedPiece.GetPosition().size() - 1);
            Position originalPosition = movedPiece.GetPosition().get(movedPiece.GetPosition().size() - 1);
            if (originalPosition != null) {
                // Clear the moved position
                board[lastMove.getCol()][lastMove.getRow()] = null;
                // Restore the piece to its original position
                board[originalPosition.getCol()][originalPosition.getRow()] = movedPiece;

                while (!eatPiece.isEmpty() && !eatmoves.isEmpty()) {
                    Position eatPosition = eatmoves.get(eatmoves.size() - 1);
                    ConcretePiece eatenPiece = eatPiece.get(eatPiece.size() - 1);

                    if (isAdjacent(eatPosition, currentPosition)) {
                        // Restore eaten piece
                        board[eatPosition.getCol()][eatPosition.getRow()] = eatenPiece;
                        eatPiece.remove(eatPiece.size() - 1);
                        eatmoves.remove(eatmoves.size() - 1);
                        movedPiece.removeKill();
                    } else {
                        break;
                    }
                }

                if (!prevPiece.isEmpty()) {
                    prevPiece.remove(prevPiece.size() - 1);
                }
                // Update player statistics if necessary
                if (movedPiece.getOwner().isPlayerOne()) {
                    firstPlayer.subtractTotalSteps(movedPiece.getTotalSteps());
                } else {
                    secondPlayer.subtractTotalSteps(movedPiece.getTotalSteps());
                }
            }
        }
    }

    // Helper method to check if two positions are adjacent
    private boolean isAdjacent(Position pos1, Position pos2) {
        return (Math.abs(pos1.getCol() - pos2.getCol()) == 1 && Math.abs(pos1.getRow() - pos2.getRow()) == 0)
                || ((Math.abs(pos1.getCol() - pos2.getCol()) == 0 && Math.abs(pos1.getRow() - pos2.getRow()) == 1));
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    public Position getLast() {
        if (moves.size() > 0)
            return moves.get(moves.size() - 1);
        return null;
    }

    // a function to check id king surrounded as the term to win for the attack
    // player
    public boolean checkIfKingSurrounded() {
        int redAround = 0;
        Position kingPosition = isKingNear();

        if (kingPosition.getCol() != -1 && kingPosition.getRow() != -1) {
            int kCol = kingPosition.getCol();
            int kRow = kingPosition.getRow();

            redAround += isSame(kCol + 1, kRow, secondPlayer);
            redAround += isSame(kCol - 1, kRow, secondPlayer);
            redAround += isSame(kCol, kRow + 1, secondPlayer);
            redAround += isSame(kCol, kRow - 1, secondPlayer);
            return (kingPosition.isNearWall() && redAround == 3) || (redAround == 4);
        }

        return false;
    }

    private int isSame(int Col, int Row, Player owner) {
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
        if (getPieceAtPosition(pos) != null && !getPieceAtPosition(pos).getType().equals("♚")) {
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
                    if (board[pos.getCol()][pos.getRow()].getOwner().isPlayerOne()) {
                        firstPlayer.setKills();
                    } else {
                        secondPlayer.setKills();
                    }

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
                    if (board[pos.getCol()][pos.getRow()].getOwner().isPlayerOne()) {
                        firstPlayer.setKills();
                    } else {
                        secondPlayer.setKills();
                    }

                }

                if (position.get(position.size() - 1).isNearWall()) {
                    // Check if the enemy piece is on the other side of the wall
                    if ((10 == kCol && 9 == pCol) || (0 == kCol && 1 == pCol) || (10 == kRow && 9 == pRow)
                            || (0 == kRow && 1 == pRow)) {
                        board[kCol][kRow] = null;
                        if (eaten.GetPosition() != null && eaten.GetPosition().equals(new Position(kCol, kRow)))
                            eaten.addPosition(new Position(kCol, kRow));
                        eatPiece.add(eaten);
                        eatmoves.add(new Position(kCol, kRow));
                        // add kills
                        board[pos.getCol()][pos.getRow()].addKills();
                        if (board[pos.getCol()][pos.getRow()].getOwner().isPlayerOne()) {
                            firstPlayer.setKills();
                        } else {
                            secondPlayer.setKills();
                        }
                    }
                }

                if (position.get(position.size() - 1).isNearCorner()) {
                    if ((9 == kCol && 8 == pCol) || (1 == kCol && 2 == pCol) || (9 == kRow && 8 == pRow)
                            || (1 == kRow && 2 == pRow)) {
                        board[kCol][kRow] = null;
                        if (eaten.GetPosition() != null && eaten.GetPosition().equals(new Position(kCol, kRow)))
                            eaten.addPosition(new Position(kCol, kRow));
                        eatPiece.add(eaten);
                        eatmoves.add(new Position(kCol, kRow));
                        // add kills
                        board[pos.getCol()][pos.getRow()].addKills();
                        if (board[pos.getCol()][pos.getRow()].getOwner().isPlayerOne()) {
                            firstPlayer.setKills();
                        } else {
                            secondPlayer.setKills();
                        }
                    }
                }

                position.remove(position.size() - 1);
            }
        }
    }

    private boolean isSameB(int Col, int Row, Player owner) {
        if (Col >= 0 && Col < boardSize && Row >= 0 && Row < boardSize &&
                board[Col][Row] != null && board[Col][Row].getOwner().equals(owner)
                && !board[Col][Row].getType().equals("♚")) {
            return true;
        }
        return false;
    }

    public List<Position> isEnemyNear(Piece me) {
        List<Position> enemy = new ArrayList<>();
        if (getLast() != null) {
            int pCol = getLast().getCol();
            int pRow = getLast().getRow();

            if (pCol + 1 < boardSize && board[pCol + 1][pRow] != null
                    && getPieceAtPosition(new Position(pCol + 1, pRow)) != null) {
                if (!me.getOwner().equals(board[pCol + 1][pRow].getOwner())
                        && !board[pCol + 1][pRow].getType().equals("♚")) {
                    enemy.add(new Position(pCol + 1, pRow));
                }
            }
            if (pCol - 1 >= 0 && board[pCol - 1][pRow] != null
                    && getPieceAtPosition(new Position(pCol - 1, pRow)) != null) {
                if (!me.getOwner().equals(board[pCol - 1][pRow].getOwner())
                        && !board[pCol - 1][pRow].getType().equals("♚")) {
                    enemy.add(new Position(pCol - 1, pRow));
                }
            }
            if (pRow + 1 < boardSize && board[pCol][pRow + 1] != null
                    && getPieceAtPosition(new Position(pCol, pRow + 1)) != null) {
                if (!me.getOwner().equals(board[pCol][pRow + 1].getOwner())
                        && !board[pCol][pRow + 1].getType().equals("♚")) {
                    enemy.add(new Position(pCol, pRow + 1));
                }
            }
            if (pRow - 1 >= 0 && board[pCol][pRow - 1] != null
                    && getPieceAtPosition(new Position(pCol, pRow - 1)) != null) {
                if (!me.getOwner().equals(board[pCol][pRow - 1].getOwner())
                        && !board[pCol][pRow - 1].getType().equals("♚")) {
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
        if (currentPiece == null) {
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
        if (((endCol == 0 && endRow == 0) || (endCol == 10 && endRow == 0) || (endCol == 0 && endRow == 10)
                || (endCol == 10 && endRow == 10)) && getPieceAtPosition(startingPosition).getType().equals("♙"))
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
        for (int i = 0; i < 24; i++) {
            secondPiece[i] = new Pawn(secondPlayer, (i + 1), 0, 0);
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
        for (int i = 0; i < 13; i++) {
            if (i != 6)
                firstPiece[i] = new Pawn(firstPlayer, (i + 1), 0, 0);
            else
                firstPiece[i] = new King(firstPlayer, (i + 1), 0, 0);
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
        sortArrays(firstPiece);
        sortArrays(secondPiece);
        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.print("\n");
    }

    // get prints for each compartor according to the player
    public void getNames(String s, int comp, ConcretePiece[] piecesArr) {
        for (int i = 0; i < piecesArr.length; i++) {
            if (piecesArr[i].position.size() > 0) {
                if (piecesArr[i].getOwner().isPlayerOne()) {
                    if (piecesArr[i].getId() != 7) {
                        s = "D" + piecesArr[i].getId();
                    } else {
                        s = "K" + piecesArr[i].getId();
                    }
                } else {
                    s = "A" + piecesArr[i].getId();
                }

                if (comp == 1) {
                    piecesArr[i].printMoves(s);
                }

                if (comp == 2) {
                    if (piecesArr[i].getKills() > 0) {
                        piecesArr[i].printKills(s);
                    }
                }

                if (comp == 3) {
                    if (piecesArr[i].getTotalSteps() > 0) {
                        piecesArr[i].printTotalSteps(s);
                    }
                }
            }
        }
    }

    public void sortBySecComp(ConcretePiece[] arr, ComperatorByKills comparator) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (comparator.compare(arr[j], arr[j + 1]) < 0) {
                    ConcretePiece temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public void sortByThirdComp(ConcretePiece[] arr, ComperatorByDistance comparator) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (comparator.compare(arr[j], arr[j + 1]) < 0) {
                    ConcretePiece temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public void sortByForthComp(List<Position> arr, ComperatorByStepedSquare comparator) {
        for (int i = 0; i < arr.size(); i++) {
            for (int j = 0; j < arr.size() - i - 1; j++) {
                if (comparator.compare(arr.get(j), arr.get(j + 1)) < 0) {
                    Position temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);
                }
                if (comparator.compare(arr.get(j), arr.get(j + 1)) == 0) {
                    if (arr.get(j).getRow() == arr.get(j + 1).getRow()
                            && arr.get(j).getCol() < arr.get(j + 1).getCol()) {
                        Position temp = arr.get(j);
                        arr.set(j, arr.get(j + 1));
                        arr.set(j + 1, temp);
                    }
                    if (arr.get(j).getRow() < arr.get(j + 1).getRow()) {
                        Position temp = arr.get(j);
                        arr.set(j, arr.get(j + 1));
                        arr.set(j + 1, temp);
                    }
                }
            }
        }
    }

    public void printStatistic() {
        ComperatorBySteps comparator1 = new ComperatorBySteps();
        ComperatorByKills comparator2 = new ComperatorByKills();
        ComperatorByDistance comparator3 = new ComperatorByDistance();
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

        ConcretePiece[] sharedPieces = new ConcretePiece[firstPiece.length + secondPiece.length];
        for (int i = 0; i < firstPiece.length; i++) {
            sharedPieces[i] = firstPiece[i];
        }

        for (int i = 0; i < secondPiece.length; i++) {
            sharedPieces[firstPiece.length + i] = secondPiece[i];
        }

        String s = "";
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

        s = "";
        if (firstPlayer.getKills() == secondPlayer.getKills()) {
            sortBySecComp(firstPiece, comparator2);
            sortBySecComp(secondPiece, comparator2);
            if (won.isPlayerOne()) {
                // Printing firstPiece array
                getNames(s, 2, firstPiece);
                // Printing secondPiece array
                getNames(s, 2, secondPiece);
            } else {
                // Printing secondPiece array
                getNames(s, 2, secondPiece);
                // Printing firstPiece array
                getNames(s, 2, firstPiece);
            }
        } else {
            sortArrays(sharedPieces);
            sortBySecComp(sharedPieces, comparator2);
            getNames(s, 2, sharedPieces);
        }

        printStars();

        s = "";

        if (firstPlayer.getTotalSteps() == secondPlayer.getTotalSteps()) {
            sortByThirdComp(firstPiece, comparator3);
            sortByThirdComp(secondPiece, comparator3);
            if (won.isPlayerOne()) {
                // Printing firstPiece array
                getNames(s, 3, firstPiece);
                // Printing secondPiece array
                getNames(s, 3, secondPiece);
            } else {
                // Printing secondPiece array
                getNames(s, 3, secondPiece);
                // Printing firstPiece array
                getNames(s, 3, firstPiece);
            }
        } else {
            sortArrays(sharedPieces);
            sortByThirdComp(sharedPieces, comparator3);
            getNames(s, 3, sharedPieces);
        }

        printStars();

        List<Position> pos = new ArrayList<>(moves);

        for (int i = 0; i < moves.size(); i++) {
            boolean check = false;
            for (int j = 0; j < pos.size(); j++) {
                if (pos.get(j).isEqual(moves.get(i))) {
                    pos.get(j).addCounter();
                    check = true;
                    break;
                }
            }
            if (!check) {
                pos.add(moves.get(i));
            }
        }
        sortByForthComp(pos, comparator4);
        for (int i = 0; i < pos.size(); i++) {
            if (pos.get(i).getCounter() > 1)
                pos.get(i).printMoves();
        }
    }

    public void sortArrays(ConcretePiece[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getId() > arr[j + 1].getId()) {
                    ConcretePiece temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

}
