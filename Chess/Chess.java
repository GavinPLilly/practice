import java.util.Scanner;

public class Chess {
    Piece[][] board;
    Scanner s;
    int turn;

    public Chess(boolean darkMode) {
        board = new Piece[8][8]; //[column][row]
        setUpBoard(darkMode);
        if(darkMode) {
            turn = 1;
        }
        else{
            turn = 0;
        }

        s = new Scanner(System.in);
    }

    private void setUpBoard(boolean darkMode) {
        int black;
        int white;
        if(darkMode) {
            black = 1;
            white = 0;
        }
        else{
            black = 0;
            white = 1;
        }
        for(int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(white);
            board[i][6] = new Pawn(black);
        }
        board[4][0] = new King(white);
        board[4][7] = new King(black);

        board[3][0] = new Queen(white);
        board[3][7] = new Queen(black);

        board[5][0] = new Bishop(white);
        board[2][0] = new Bishop(white);
        board[5][7] = new Bishop(black);
        board[2][7] = new Bishop(black);

        board[1][0] = new Knight(white);
        board[6][0] = new Knight(white);
        board[1][7] = new Knight(black);
        board[6][7] = new Knight(black);


        board[0][0] = new Rook(white);
        board[7][0] = new Rook(white);
        board[0][7] = new Rook(black);
        board[7][7] = new Rook(black);
    }

    public void displayBoard() {
        System.out.println("");
        System.out.println("————————————————————————————————");
        for(int i = 7; i >= 0; i--) {
            String tmpString = "|";
            for(int j = 0; j < 8; j++) {
                if(board[j][i] == null) {
                    tmpString += "   ";
                }
                else {
                    tmpString += " " + board[j][i] + " ";
                }
                tmpString += "|";
            }
            System.out.println(tmpString);
            System.out.println("————————————————————————————————");
        }
        System.out.println("");
    }

    public void makeMove() {
        System.out.println("Type your move: [starting square] [ending square]");
        String move = s.nextLine();
        int[] startingSquare = getSquare(move.substring(0, 2));
        int[] endingSquare = getSquare(move.substring(3, 5));
    }

    private int[] getSquare(String move) {
        int[] out = new int[2];
        char row = move.charAt(0);
        if(row > 96) {
            out[0] = (int)row - 97;
        }
        else {
            out[0] = (int)row - 65;
        }
        out[1] = move.charAt(1) - 49;
        return out;
    }

    private boolean isValidMove(int[] start, int[] end) {
        if(board[start[0]][start[1]] == null) {
            return False;
        }
    }

    public static void main(String[] args) {
        Chess c = new Chess(true);
        c.makeMove();
        // c.displayBoard();
    }
}
