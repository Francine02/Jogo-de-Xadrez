package aplication;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessExceptiion;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while(!chessMatch.isCheckMate()) {
            try{
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);
                System.out.println("\nSource: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.boardPrint(chessMatch.getPieces(), possibleMoves);
                
    
                System.out.println("\nTarget: ");
                ChessPosition target = UI.readChessPosition(sc);
    
                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

                if (capturedPiece != null) {
                    captured.add(capturedPiece);
                }
                
            } catch (ChessExceptiion c) {
                System.out.println(c.getMessage());
                sc.nextLine();
            } catch (InputMismatchException c) {
                System.out.println(c.getMessage());
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessMatch, captured);
    }
}