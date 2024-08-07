package chess;

import boardgame.Position;

public class ChessPosition {
    private char collumn;
    private int row;

    public ChessPosition(char collumn, int row) {
        if(collumn < 'A' || collumn> 'H' || row < 1 || row > 8){
            throw new ChessExceptiion("Error! Position invalid");
        }
        this.collumn = collumn;
        this.row = row;
    }

    public char getCollumn() {
        return collumn;
    }

    public int getRow() {
        return row;
    }

    protected Position toPosition() {
        return new Position(8 - row, collumn - 'A');
    }

    protected static ChessPosition fromPosition (Position position){
        return new ChessPosition((char)('A' + position.getColumn()), 8 - position.getRow());
    }

    @Override
    public String toString() {
        return "" + collumn + row;
    }

    
}
