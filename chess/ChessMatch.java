package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.chesspieces.Bishop;
import chess.chesspieces.Horse;
import chess.chesspieces.King;
import chess.chesspieces.Pawn;
import chess.chesspieces.Rook;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieceAdversary = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourChessPosition) {
        Position p = sourChessPosition.toPosition();
        validateSourcePosition(p);
        return board.piece(p).possibleMoves();
    }

    private void placeNewPiece(char collumn, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(collumn, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeNewPiece('A', 1, new Rook(board, Color.WHITE));
        placeNewPiece('B', 1, new Horse(board, Color.WHITE));
        placeNewPiece('C', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('E', 1, new King(board, Color.WHITE));
        placeNewPiece('F', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('G', 1, new Horse(board, Color.WHITE));
        placeNewPiece('H', 1, new Rook(board, Color.WHITE));
        placeNewPiece('A', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('B', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('C', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('D', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('E', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('F', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('G', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('H', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('A', 8, new Rook(board, Color.BLACK));
        placeNewPiece('B', 8, new Horse(board, Color.BLACK));
        placeNewPiece('C', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('E', 8, new King(board, Color.BLACK));
        placeNewPiece('F', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('G', 8, new Horse(board, Color.BLACK));
        placeNewPiece('H', 8, new Rook(board, Color.BLACK));
        placeNewPiece('A', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('B', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('C', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('D', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('E', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('F', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('G', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('H', 7, new Pawn(board, Color.BLACK));
    }

    public ChessPiece performChessMove(ChessPosition sourceChessPosition, ChessPosition targetChessPosition) {
        Position source = sourceChessPosition.toPosition();
        Position target = targetChessPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessExceptiion("You cant't put yourself in check");
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if(testCheck(opponent(currentPlayer))){
            checkMate = true;
        } else {
            nextTurn();
        }
        return (ChessPiece) capturedPiece;
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsPiece(position)) {
            throw new ChessExceptiion("There is no piece on source position!");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessExceptiion("It's not your turn!!");
        }
        if (!board.piece(position).isTherePossibleMove()) {
            throw new ChessExceptiion("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessExceptiion("The chosen piece can't move to target position");
        }
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece)board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieceAdversary.add(capturedPiece);
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece)board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieceAdversary.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
                .collect(Collectors.toList());

        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + "King on the board");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream()
                .filter(x -> ((ChessPiece) x).getColor() == opponent(color))
                .collect(Collectors.toList());

        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();

            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
                .collect(Collectors.toList());
        
        for (Piece p : list){
            boolean [][] mat = p.possibleMoves();
            for(int i=0; i < board.getRows(); i++){
                for(int j=0; j < board.getColumns(); j++){
                    if (mat[i][j]) {
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);

                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }
}
