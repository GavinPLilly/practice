import java.util.Scanner;

import java.lang.Math;

public class Chess {
	Piece[][] board;
	Scanner s;
	int turn;
	int white;
	int black;

	public Chess(boolean darkMode) {
		board = new Piece[8][8]; //[column][row]
		if(darkMode) {
			turn = 0;
			white = 0;
			black = 1;
		}
		else{
			turn = 1;
			white = 1;
			black = 0;
		}
		// setUpBoard();
		setUpFakeBoard();

		s = new Scanner(System.in);
	}

	private void setUpBoard() {
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

	private void setUpFakeBoard() {
		board[0][0] = new King(white);
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
		String move;
		int[] startingSquare = {-1, -1};
		int[] endingSquare = {-1, -1};
		boolean valid;
		try {
			String tmp;
			if(turn == white) {
				tmp = "White";
			}
			else{
				tmp = "Black";
			}
			System.out.println(tmp + " to move");
			System.out.println("Type your move: [starting square] [ending square]");
			move = s.nextLine();
			startingSquare = getSquare(move.substring(0, 2));
			endingSquare = getSquare(move.substring(3, 5));
			valid = isValidMove(startingSquare, endingSquare, true);
		}
		catch (Exception ex) {
			valid = false;
		}
		while(!valid) {
			System.out.println("Invalid Move");
			move = s.nextLine();
			startingSquare = getSquare(move.substring(0, 2));
			endingSquare = getSquare(move.substring(3, 5));
			valid = isValidMove(startingSquare, endingSquare, true);
		}
		board[endingSquare[0]][endingSquare[1]] = board[startingSquare[0]][startingSquare[1]];
		board[startingSquare[0]][startingSquare[1]] = null;
		Piece p = board[endingSquare[0]][endingSquare[1]];
		if(p instanceof Pawn) {
			Pawn tmp_p = (Pawn) p;
			tmp_p.setMoved();
		}
		else if(p instanceof King) {
			King tmp_p = (King) p;
			tmp_p.setMoved();
		}
		System.out.println(isInCheck());
		changeTurn();
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

	private boolean isValidMove(int[] start, int[] end, boolean checkColor) {
		Piece p1 = board[start[0]][start[1]];
		if(start[0] < 0 || start[0] > 7 || start[1] < 0 || start[1] > 7) { //if start square is out of bounds
			return false;
		}
		if(p1 == null) { //if there's no piece on the starting square
			return false;
		}
		if(checkColor) {
			if(p1.color() != turn) { //if the color of the turn doesn't match the color of the piece
				return false;
			}
		}
		if(end[0] < 0 || end[0] > 7 || end[1] < 0 || end[1] > 7) { //if end square is out of bounds
			return false;
		}
		if(start[0] == end[0] && start[1] == end[1]) { //if the start and end squares are the same
			return false;
		}
		Piece p2 = board[end[0]][end[1]];
		if(p2 != null && p2.color() == p1.color()) { //if the piece is trying to move to a square which has a piece of the same color
			return false;
		}
		if(p1 instanceof Pawn) {
			if(isValidPawnMove((Pawn)p1, start, end) == false) {
				return false;
			}
		}
		else if(p1 instanceof King) {
			if(isValidKingMove((King)p1, start, end) == false) {
				return false;
			}
		}
		else if(p1 instanceof Queen) {
			if(isValidQueenMove(start, end) == false) {
				return false;
			}
		}
		else if(p1 instanceof Bishop) {
			if(isValidBishopMove(start, end) == false) {
				return false;
			}
		}
		else if(p1 instanceof Knight) {
			if(isValidKnightMove(start, end) == false) {
				return false;
			}
		}
		else if(p1 instanceof Rook) {
			if(isValidRookMove(start, end) == false) {
				return false;
			}
		}
		return true;
	}

	private boolean isInCheck() {
		int[] tmpStart = new int[2];
		int[] tmpEnd = new int[2];
		int[] kingPos = findKing();
		System.out.println("king at " + kingPos[0] + ", " + kingPos[1]);
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] != null && board[i][j].color() != turn) {
					System.out.println("found non-null square");
					tmpStart[0] = i;
					tmpStart[1] = j;
					tmpEnd[0] = kingPos[0];
					tmpEnd[1] = kingPos[1];
					if(isValidMove(tmpStart, tmpEnd, false)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private int[] findKing() {
		Piece tmp;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				tmp = board[i][j];
				if(tmp != null) {
					if(tmp.color() == turn) {
						int[] out = {i, j};
						return out;
					}
				}
			}
		}
		return null;
	}

	private boolean isValidPawnMove(Pawn p, int[] start, int[] end) {
		int forwardDir;
		if(turn == white) {
			forwardDir = 1;
		}
		else {
			forwardDir = -1;
		}
		if(end[1] - start[1] == (2 * forwardDir)  && !p.hasMoved() && end[0] == start[0] && board[start[0]][start[1] + forwardDir] == null && board[start[0]][start[1] + (forwardDir * 2)] == null) {
			//if pawn moves two rows forward
			//and it hasn't moved yet
			//and it stays in the same column
			//and there are no pieces on the two squares ahead
			return true;
		}
		if(end[1] - start[1] == (forwardDir)) {
			//it the pawn moves one row forward
			if(end[0] == start[0] && board[start[0]][start[1] + forwardDir] == null) {
				//and it stays in the same column
				//and theres no piece on the ending square
				return true;
			}
			if(Math.abs(end[0] - start[0]) == 1 && board[end[0]][end[1]] != null) {
				//and it moves one column over (diagonally with the previous check one row forward)
				//and there is a piece there
				return true;
			}
		}
		return false;
	}

	private boolean isValidKingMove(King k, int[] start, int[] end) {
		if(Math.abs(start[0] - end[0]) <= 1 && Math.abs(start[1] - end[1]) <= 1) {
			return true;
		}
		return false;
	}

	private boolean isValidQueenMove(int[] start, int[] end) {
		return isValidBishopMove(start, end) || isValidRookMove(start, end);

	}

	private boolean isValidBishopMove(int[] start, int[] end) {
		if(Math.abs(start[0] - end[0]) == Math.abs(start[1] - end[1])) {
			//if it moves the same amount horizontal as vertical (if it moves diagonally)
			int i = 1;
			if(end[1] - start[1] > 0) { //if the bishop is moving up the board (from white perspective)
				if(end[0] - start[0] > 0){ //if the bishop is moving up and right
					while(i < Math.abs(start[0] - end[0])) { //loop through the diagonal squares
						if(board[start[0] + i][start[1] + i] != null) {
							return false;
						}
						i++;
					}
					return true;
				}
				else { //up and left
					while(i < Math.abs(start[0] - end[0])) { //loop through the diagonal squares
						if(board[start[0] - i][start[1] + i] != null) {
							return false;
						}
						i++;
					}
					return true;
				}
			}
			else{ //bishop if moving down the board (from white perspective)
				if(end[0] - start[0] > 0) { //if the bishop is moving down and right
					while(i < Math.abs(start[0] - end[0])) { //loop through the diagonal squares
						if(board[start[0] + i][start[1] - i] != null) {
							return false;
						}
						i++;
					}
					return true;
				}
				else {
					while(i < Math.abs(start[0] - end[0])) { //loop through the diagonal squares
						if(board[start[0] - i][start[1] - i] != null) {
							return false;
						}
						i++;
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean isValidKnightMove(int[] start, int[]end) {
		if(Math.abs(start[0] - end[0]) == 2 && Math.abs(start[1] - end[1]) == 1) {
			return true;
		}
		if(Math.abs(start[0] - end[0]) == 1 && Math.abs(start[1] - end[1]) == 2) {
			return true;
		}
		return false;
	}
	
	private boolean isValidRookMove(int[] start, int[] end) {
		int i = 1;
		if(start[0] == end[0]) { //if it stays in the same column
			if(end[1] > start[1]) { //if it moves right
				while(i < Math.abs(start[1] - end[1])) {
					if(board[start[0]][start[1] + i] != null) {
						return false;
					}
					i++;
				}
			}
			else{ //if it moves left
				while(i < Math.abs(start[1] - end[1])) {
					if(board[start[0]][start[1] - i] != null) {
						return false;
					}
					i++;
				}
			}
		}
		else if(start[1] == end[1]) { //if it stays in the same row
			if(end[0] > start[0]) { //if it moves up
				while(i < Math.abs(start[1] - end[1])) {
					if(board[start[start[0] + i]][start[0]] != null) {
						return false;
					}
					i++;
				}
			}
			else{ //if it moves down
				while(i < Math.abs(start[1] - end[1])) { //if it moves down
					if(board[start[start[0] - i]][start[0]] != null) {
						return false;
					}
					i++;
				}
			}
		}
		else {
			return false;
		}
		return true;
	}
	
	private void changeTurn() {
		if(turn == white) {
			turn = black;
		}
		else {
			turn = white;
		}
	}
	public static void main(String[] args) {
		Chess c = new Chess(true);
		while(true) {
			c.displayBoard();
			c.makeMove();
		}
	}

}
