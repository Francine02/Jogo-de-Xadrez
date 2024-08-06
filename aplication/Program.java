package aplication;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessExceptiion;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        while(true) {
            try{
                UI.clearScreen();
                UI.boardPrint(chessMatch.getPieces());
                System.out.println("\nSource: ");
                ChessPosition source = UI.readChessPosition(sc);
    
                System.out.println("\nTarget: ");
                ChessPosition target = UI.readChessPosition(sc);
    
                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

            } catch (ChessExceptiion c) {
                System.out.println(c.getMessage());
                sc.nextLine();
            } catch (InputMismatchException c) {
                System.out.println(c.getMessage());
                sc.nextLine();
            }
        }
    }
}