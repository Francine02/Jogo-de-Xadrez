package chess;

import boardgame.BoardException;

public class ChessExceptiion extends BoardException{
    private static final long serialVersionUID = 1L;

    public ChessExceptiion(String msg) {
        super(msg);
    }
}
