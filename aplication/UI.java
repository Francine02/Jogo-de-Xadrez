package aplication;

import chess.ChessPiece;

public class UI {
    public static void boardPrint(ChessPiece[][] pieces) {
        for(int i=0; i<pieces.length; i++){
            System.out.print((8 - i) + " ");
            for(int j=0; j<pieces.length; j++){
                piecePrint(pieces[i][j]);
            }
            System.out.println();
        }

        System.out.println("\n   A   B   C   D   E   F   G   H");
    }

    private static void piecePrint(ChessPiece piece) {
        if (piece == null){
            System.out.print(" - ");
        } else{
            System.out.print(piece);
        }
        System.out.print(" ");
    }
}
