package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.chesspieces.King;
import chess.chesspieces.Rook;

public class ChessMatch {
    
    private Board board;
    private int turn;
    private Color currentPlayer;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieceAdversary = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i=0; i < board.getRows(); i++) {
            for ( int j=0; j<board.getColumns(); j++){
                mat[i] [j] = (ChessPiece) board.piece(i, j );
            }
        }
        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourChessPosition){
        Position p = sourChessPosition.toPosition();
        validateSourcePosition(p);
        return board.piece(p).possibleMoves();
    }

    private void placeNewPiece(char collumn, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(collumn, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeNewPiece('B', 6, new Rook(board, Color.WHITE));
        placeNewPiece('E', 1, new King(board, Color.WHITE));

        placeNewPiece('E', 8, new King(board, Color.BLACK));

    }

    public ChessPiece performChessMove(ChessPosition sourceChessPosition, ChessPosition targetChessPosition) {
        Position source = sourceChessPosition.toPosition();
        Position target = targetChessPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);
        nextTurn();
        return (ChessPiece)capturedPiece;
    }

    private void validateSourcePosition(Position position) {
        if(!board.thereIsPiece(position)){
            throw new ChessExceptiion("There is no piece on source position!");
        }
        if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessExceptiion("It's not your turn!!");
        }
        if(!board.piece(position).isTherePossibleMove()){
            throw new ChessExceptiion("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){
            throw new ChessExceptiion("The chosen piece can't move to target position");
        }
    }

    private Piece makeMove(Position source, Position target){
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieceAdversary.add(capturedPiece);
        }

        return capturedPiece;
    }

    private void nextTurn(){
        turn ++;
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
}
