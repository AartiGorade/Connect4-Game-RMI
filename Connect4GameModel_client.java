/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Client model implementation with UDP mode of connection
 * 
 */

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


public class Connect4GameModel_client implements Observer {
	private char[][] boardMatrix;
	private static final int MAX_COLUMN_NUMBER = 25;
	private static final int MAX_ROW_NUMBER = 9;

	private int lastMoveRow;
	private int lastMoveColumn;
	private char lastMoveChar;

	public Connect4GameModel_client() {
		boardMatrix = new char[MAX_ROW_NUMBER][];
		for (int rows = 0; rows < MAX_ROW_NUMBER; rows++) {
			boardMatrix[rows] = new char[25];
			Arrays.fill(boardMatrix[rows], '1');
			for (int columns = rows; columns < MAX_COLUMN_NUMBER - rows; columns++) {
				boardMatrix[rows][columns] = '0';
			}
		}
		new Connect4UserView_client().displayBoard(boardMatrix, MAX_ROW_NUMBER, MAX_COLUMN_NUMBER);
	}

	public Connect4GameModel_client(Connect4GameModel_client aConnect4Field) {
		this.boardMatrix = new char[MAX_ROW_NUMBER][];
		for (int row = 0; row < MAX_ROW_NUMBER; row++) {
			boardMatrix[row] = new char[MAX_COLUMN_NUMBER];
			for (int column = 0; column < MAX_COLUMN_NUMBER; column++) {
				this.boardMatrix[row][column] = aConnect4Field.getBoardMatrix()[row][column];
			}
		}
	}

	public char[][] getBoardMatrix() {
		return boardMatrix;
	}

	/**
	 * @return max rows in the board
	 */
	public static int getMaxColumns() {
		return MAX_COLUMN_NUMBER;
	}

	/**
	 * @return maximum rows in the board
	 */
	public static int getMaxRows() {
		return MAX_ROW_NUMBER;
	}

	public boolean checkIfPiecedCanBeDroppedIn(int column) {
		if ((column < 0) || (column > MAX_COLUMN_NUMBER)) {
			return false;
		} else {
			for (int rows = MAX_ROW_NUMBER - 1; rows >= 0; rows--) {
				if (boardMatrix[rows][column] == '0') {
					return true;
				}
			}
		}
		return false;
	}

	public void dropPieces(int column, char gamePiece) {
		lastMoveChar = gamePiece;
		lastMoveRow = -1;
		lastMoveColumn = column;
		for (int rows = MAX_ROW_NUMBER - 1; rows >= 0; rows--) {
			if (boardMatrix[rows][column] == '0') {
				boardMatrix[rows][column] = gamePiece;
				lastMoveRow = rows;
				break;
			}
		}
	}

	/**
	 * Gets the bottom-most free row
	 * 
	 * @param column
	 *            in which the free row has to be obtained
	 * @return bottom-most row number
	 */
	public int getHighestRowNumber(int column) {
		for (int rows = MAX_ROW_NUMBER - 1; rows >= 0; rows--) {
			if (boardMatrix[rows][column] == '0') {
				return rows;
			}
		}
		return -1;
	}

	public boolean didLastMoveWin() {
		if (lastMoveRow == -1) {
			return false;
		}
		// Check row
		int continuousCharacterCount = 0;
		for (int index = 0; index < 4; index++) {
			if (((lastMoveRow + index) < 0)
					|| ((lastMoveRow + index) >= MAX_ROW_NUMBER)) {
				continue;
			}
			if (boardMatrix[lastMoveRow + index][lastMoveColumn] == lastMoveChar) {
				continuousCharacterCount++;
			} else if (continuousCharacterCount >= 4) {
				return true;
			} else {
				continuousCharacterCount = 0;
			}
		}
		if (continuousCharacterCount >= 4) {
			return true;
		}
		// Check for previous 3 and next 4 columns
		continuousCharacterCount = 0;
		for (int index = -3; index < 4; index++) {
			if ((lastMoveColumn + index < 0)
					|| (lastMoveColumn + index >= MAX_COLUMN_NUMBER)) {
				continue;
			}
			if (boardMatrix[lastMoveRow][lastMoveColumn + index] == lastMoveChar) {
				continuousCharacterCount++;
			} else if (continuousCharacterCount >= 4) {
				return true;
			} else {
				continuousCharacterCount = 0;
			}
		}
		if (continuousCharacterCount >= 4) {
			return true;
		}

		// check diagnol - previous 3 and next 3 diagnol elements
		continuousCharacterCount = 0;
		for (int index = -3; index < 4; index++) {
			if ((lastMoveColumn + index < 0)
					|| (lastMoveColumn + index >= MAX_COLUMN_NUMBER)
					|| (lastMoveRow + index < 0)
					|| (lastMoveRow + index >= MAX_ROW_NUMBER)) {
				continue;
			}
			if (boardMatrix[lastMoveRow + index][lastMoveColumn + index] == lastMoveChar) {
				continuousCharacterCount++;
			} else if (continuousCharacterCount >= 4) {
				return true;
			} else {
				continuousCharacterCount = 0;
			}
		}
		if (continuousCharacterCount >= 4) {
			return true;
		}
		return false;
	}

	public boolean isItaDraw() {
		for (int rows = 0; rows < MAX_ROW_NUMBER; rows++) {
			for (int columns = 0; columns < MAX_COLUMN_NUMBER; columns++) {
				if (boardMatrix[rows][columns] == '0') {
					return false;
				}
			}
		}
		return true;
	}

	public void update(Observable arg0, Object arg1) {

		if (arg1 instanceof Integer) {
			checkAndDrop((GameControl_client) arg0);
		}
	}

	/**
	 * Check and drop it in the specified location of gameControl
	 * @param GameControl_client
	 */
	public void checkAndDrop(GameControl_client GameControl_client) {
		int move = GameControl_client.getCurrentgameMove();
		char gameChar = GameControl_client.getCurrentGamePiece();
		if (checkIfPiecedCanBeDroppedIn(move)) {
			dropPieces(move, gameChar);
			if (isItaDraw()) {
				GameControl_client.setGameTie();
			}
			if (didLastMoveWin()) {
				GameControl_client.setGameWin();

			}
		} else {
			GameControl_client.setGameError("Invalid Drop");
		}
		
	}

}
