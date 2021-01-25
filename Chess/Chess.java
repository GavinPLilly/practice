import java.util.Scanner;

import javax.security.auth.kerberos.KerberosCredMessage;

import java.lang.Math;

public class Chess {
	Piece[][] board;
	Scanner s;
	int turn;
	int white;
	int black;
	boolean gameover;

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
		gameover = false;
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
		board[7][7] = new King(black);
		board[7][2] = new Pawn(black);

		board[0][0] = new King(white);
		board[0][6] = new Pawn(white);
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
		String tmp;
		if(turn == white) {
			tmp = "White";
		}
		else{
			tmp = "Black";
		}
		String tmp2;
		if(isInCheck()) {
			if(isCheckMate()) {
				if(turn == white) {
					tmp = "Black";
				}
				else{
					tmp = "White";
				}
				System.out.println("Checkmate. " + tmp + " wins.");
				gameover = true;
				return;
			}	
			tmp2 = ". You in check.";
		}
		else {
			tmp2 = "";
		}
		System.out.print(tmp + " to move");
		System.out.println(tmp2);
		System.out.println("Type your move: [starting square] [ending square]");
		move = s.nextLine();
		startingSquare = getSquare(move.substring(0, 2));
		endingSquare = getSquare(move.substring(3, 5));
		while(!isValidMoveWithCheck(startingSquare, endingSquare)) {
			System.out.println("Invalid Move");
			move = s.nextLine();
			startingSquare = getSquare(move.substring(0, 2));
			endingSquare = getSquare(move.substring(3, 5));
		}
		Piece p = board[endingSquare[0]][endingSquare[1]];
		if(p instanceof Pawn) {
			Pawn tmp_p = (Pawn) p;
			tmp_p.setMoved();
		}
		else if(p instanceof King) {
			King tmp_p = (King) p;
			tmp_p.setMoved();
		}
		board[endingSquare[0]][endingSquare[1]] = board[startingSquare[0]][startingSquare[1]];
		board[startingSquare[0]][startingSquare[1]] = null;

		if(board[endingSquare[0]][endingSquare[1]] instanceof Pawn) {
			if(endingSquare[1] == 7 && turn == white) {
				promotePawn(endingSquare);
			}
			if(endingSquare[1] == 0 && turn == black) {
				promotePawn(endingSquare);
			}
		}
		changeTurn();
	}

	private int[] getSquare(String move) {
		int[] out = new int[2];
		try {
			char row = move.charAt(0);
			if(row > 96) {
				out[0] = (int)row - 97;
			}
			else {
				out[0] = (int)row - 65;
			}
			out[1] = move.charAt(1) - 49;
		}
		catch(Exception ex) {
			out[0] = -1;
			out[1] = -1;
		}
		return out;
	}

	private boolean isValidMove(int[] start, int[] end, boolean checkColor) {
		if(start[0] < 0 || start[0] > 7 || start[1] < 0 || start[1] > 7) { //if start square is out of bounds
			return false;
		}
		Piece p1 = board[start[0]][start[1]];
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

	private boolean isValidMoveWithCheck(int[] start, int[] end) {
		if(isValidMove(start, end, true) == false) {
			return false;
		}
		Piece startPiece = board[start[0]][start[1]];
		Piece endPiece = board[end[0]][end[1]];
		board[end[0]][end[1]] = board[start[0]][start[1]];
		board[start[0]][start[1]] = null;	
		if(isInCheck()) {
			board[end[0]][end[1]] = endPiece;
			board[start[0]][start[1]] = startPiece;
			return false;
		}
		board[end[0]][end[1]] = endPiece;
		board[start[0]][start[1]] = startPiece;

		return true;
	}

	private boolean isInCheck() {
		int[] tmpStart = new int[2];
		int[] tmpEnd = new int[2];
		if(findKing() == null) {
			return false;
		}
		int[] kingPos = findKing();
		tmpEnd[0] = kingPos[0];
		tmpEnd[1] = kingPos[1];
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] != null && board[i][j].color() != turn) {
					tmpStart[0] = i;
					tmpStart[1] = j;
					if(isValidMove(tmpStart, tmpEnd, false)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private int[][] findCheckers(int[] kingPos) {
		int[] tmpStart = new int[2];
		int[] tmpEnd = new int[2];
		tmpEnd[0] = kingPos[0];
		tmpEnd[1] = kingPos[1];
		int[][] outArr = new int[2][2];
		int index = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] != null && board[i][j].color() != turn) {
					tmpStart[0] = i;
					tmpStart[1] = j;
					if(isValidMove(tmpStart, tmpEnd, false)) {
						outArr[index][0] = i;
						outArr[index][1] = j;
						index++;
					}
				}
			}
		}
		if(index == 1) {
			outArr[index][0] = -1;
			outArr[index][1] = -1;
		}
		return outArr;
	}

	private boolean isCheckMate() {
		int[] kingPos = findKing();
		int[][] checkers = findCheckers(kingPos);
		if(checkers[1][0] == -1) {
			System.out.println("Only one checker");
			if(canKillChecker(checkers[0])) {
				System.out.println("Can kill attacker");
				return false;
			}
			if(canBlockChecker(kingPos, checkers[0])) {
				System.out.println("Can block checker");
				return false;
			}
		}
		if(canMoveKing(kingPos)) {
			System.out.println("Can move king");
			return false;
		}
		return true;
	}

	private boolean canKillChecker(int[] checkerPos) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] != null) {
					if(board[i][j].color() == turn) {
						int[] start = {i, j};
						if(isValidMoveWithCheck(start, checkerPos)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean canMoveKing(int[] kingPos) {
		int[] end = new int[2];
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				end[0] = i + kingPos[0];
				end[1] = j + kingPos[0];
				if(isValidMoveWithCheck(kingPos, end)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean canBlockChecker(int[] kingPos, int[] checkerPos) {
		Piece p = board[checkerPos[0]][checkerPos[1]];
		if(p instanceof Knight) {
			return false;
		}
		if(p instanceof Pawn) {
			return false;
		}
		if(p instanceof Rook || p instanceof Queen) {
			int[][] squares = findRookBlockingSquares(kingPos, checkerPos);
			int[] tmpPos = new int[2];
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					if(board[i][j] != null && board[i][j].color() == turn) {
						tmpPos[0] = i;
						tmpPos[1] = j;
						for(int k = 0; k < 6; k++) {
							if(isValidMoveWithCheck(tmpPos, squares[k])) {
								return true;
							}
						}
					}
				}
			}
		}
		else if(p instanceof Bishop || p instanceof Queen) {
			int[][] squares = findBishopBlockingSquares(kingPos, checkerPos);
			int[] tmpPos = new int[2];
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					if(board[i][j] != null && board[i][j].color() == turn) {
						tmpPos[0] = i;
						tmpPos[1] = j;
						for(int k = 0; k < 6; k++) {
							if(isValidMoveWithCheck(tmpPos, squares[k])) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private int[][] findRookBlockingSquares(int[] kingPos, int[] checkerPos) {
		int[][] out = new int[6][2];
		int counter;
		if(kingPos[0] - checkerPos[0] != 0 && kingPos[1] - checkerPos[1] != 0) {
			for(int i = 0; i < 6; i++) {
				out[i][0] = -1;
				out[i][1] = -1;
			}
			return out;
		}
		if(kingPos[0] == checkerPos[0]) {
			int difference = Math.abs(kingPos[1] - checkerPos[1]);
			int col = kingPos[0];
			int delta;
			if(checkerPos[1] > kingPos[1]) {
				delta = 1;
			}
			else {
				delta = -1;
			}
			counter = 0;
			for(int i = 1; i < difference; i++) {
				out[counter][0] = col;
				out[counter][1] = kingPos[1] + (i * delta);
				counter++;
			}
		}
		else {
			int difference = Math.abs(kingPos[0] - checkerPos[0]);
			int row = kingPos[1];
			int delta;
			if(checkerPos[0] > kingPos[0]) {
				delta = 1;
			}
			else {
				delta = -1;
			}
			counter = 0;
			for(int i = 1; i < difference; i++) {
				out[counter][0] = kingPos[0] + (i * delta);
				out[counter][1] = row;
				counter++;
			}
		}
		for(int i = counter; i < 6; i++) {
			out[i][0] = -1;
			out[i][1] = -1;
		}
		return out;
	}

	private int[][] findBishopBlockingSquares(int[] kingPos, int[] checkerPos) {
		Piece p = board[checkerPos[0]][checkerPos[1]];
		int[][] out = new int[6][2];
		if(Math.abs(kingPos[0] - checkerPos[0]) != Math.abs(kingPos[1] - checkerPos[1])) {
			for(int i = 0; i < 6; i++) {
				out[i][0] = -1;
				out[i][1] = -1;
			}
			return out;
		}
		int columnDelta;
		int rowDelta;
		int counter;
		if(checkerPos[0] > kingPos[0]) {
			columnDelta = 1;
		}
		else {
			columnDelta = -1;
		}
		if(checkerPos[1] > kingPos[1]) {
			rowDelta = 1;
		}
		else{
			rowDelta = -1;
		}
		int difference = Math.abs(kingPos[0] - checkerPos[0]);
		counter = 0;
		for(int i = 1; i < difference; i++) {
			out[counter][0] = kingPos[0] + (i * columnDelta);
			out[counter][1] = kingPos[1] + (i * rowDelta);
			counter++;
		}
		for(int i = counter; i < 6; i++) {
			out[i][0] = -1;
			out[i][1] = -1;
		}
		return out;
	}

	private int[] findKing() {
		Piece tmp;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				tmp = board[i][j];
				if(tmp != null) {
					if(tmp instanceof King && tmp.color() == turn) {
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

	public boolean isOver() {
		return gameover;
	}

	private void promotePawn(int[] pos) {
		boolean validChoice = false;
		String input;
		
		System.out.println("Pawn is being promoted. Enter a letter");
		System.out.println("q = queen  r = rook  b = bishop  k = knight");
		while(validChoice == false) {
			input = s.nextLine();
			switch(input) {
				case "q":
				case "Q":
					validChoice = true;
					board[pos[0]][pos[1]] = new Queen(turn);
					break;
				case "r":
				case "R":
					validChoice = true;
					board[pos[0]][pos[1]] = new Rook(turn);
					break;
				case "b":
				case "B":
					validChoice = true;
					board[pos[0]][pos[1]] = new Bishop(turn);
					break;
				case "k":
				case "K":
					validChoice = true;
					board[pos[0]][pos[1]] = new Knight(turn);
					break;
			}
		}
		
	}

	private void printDoubleArray(int[][] a) {
		int l1 = a.length;
		int l2 = a[0].length;
		for(int i = 0; i < l1; i++) {
			for(int j = 0; j < l2; j++) {
				System.out.print(a[i][j] + ", ");
			}
			System.out.println("");
		}
	}
	public static void main(String[] args) {
		Chess c = new Chess(true);
		while(c.isOver() == false) {
			c.displayBoard();
			c.makeMove();
		}
	}

}
