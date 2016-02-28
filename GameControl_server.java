/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Server control implementation with RMI
 * 
 */

import java.io.IOException;
import java.util.Observable;




public class GameControl_server extends Observable{

	private int currentUserMove;
	private boolean isGameTie;
	private boolean isGameWin;
	private Connect4GameModel_server connect4Board;
	private PlayerModel_server myPlayers[];
	private PlayerModel_server currentPlayer;
	private char currentPlayerGamePiece;
	private char opponetGamePiece;
	private String gameMessage;
	private static int index=0;
	private final int myNumber=0;
	private int whosTurn=0;
	static String currentStatus="-1 0 0";
	private static nextMoveInterface objectRMI;
	private static boolean boardUpdated=false;
	
	
	public GameControl_server(nextMoveInterface t){
		this.objectRMI=t;
	}
	
	
	/**
	 * Plays the actual game Player-1 and 2 makes their moves
	 * If the player is Human Player, notifies View for input move.
	 * Each player update nextMove on RMIRegistry each time and fetched from 
	 * their before making next new move.
	 * Accordingly Game board is updated.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	
		
	public void playTheGame() throws ClassNotFoundException, IOException {
		objectRMI.updateRegistry(currentStatus);
		do{
		 String[] tokens=currentStatus.split("\\s+");
		 if(tokens[0].equals("Game")){
			 System.out.println(currentStatus);
			 break;
		 }
		 currentUserMove=Integer.parseInt(tokens[0]);
		 currentPlayerGamePiece=tokens[1].charAt(0);
		 whosTurn=Integer.parseInt(tokens[2]);
		 if((whosTurn==myNumber) ){
			 	setChanged();
				notifyObservers(index);
				setChanged();
				notifyObservers(this);
				
				if (isGameTie || isGameWin) {
					gameMessage = "Game Won by another Player ";
					setChanged();
					notifyObservers(this);
					setChanged();
					notifyObservers(gameMessage);
					currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+((whosTurn+1)%2);
					objectRMI.updateRegistry(currentStatus);
					currentStatus=gameMessage;
					objectRMI.updateRegistry(currentStatus);
					System.out.println(gameMessage);
					break;
				}
			
			 	System.out.println("It's my Turn");	 	
			 	gameMessage = null;
				index=index%2;
				currentPlayerGamePiece = myPlayers[index].getGamePiece();
				currentPlayer = myPlayers[index];
				setChanged();
				notifyObservers(myPlayers[index]);
				
				// Set the Player's move and gamePiece
				currentUserMove = myPlayers[index].makeNextMove();
				whosTurn=((whosTurn+1)%2);
				currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+whosTurn;
				objectRMI.updateRegistry(currentStatus);
				setChanged();
				notifyObservers(index);
				setChanged();
				notifyObservers(this);
				
				boardUpdated=true;
		 }	
		 
		 else if((whosTurn==(myNumber)%2)){
				boardUpdated=false;
				setChanged();
				notifyObservers(index);
				setChanged();
				notifyObservers(this);
				
		}
		 
				if (isGameTie || isGameWin) {
					gameMessage = "Game Won by Player "
							+ myPlayers[index].getName();
					setChanged();
					notifyObservers(this);
					setChanged();
					notifyObservers(gameMessage);
					currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+((whosTurn+1)%2);
					objectRMI.updateRegistry(currentStatus);
					currentStatus=gameMessage;
					objectRMI.updateRegistry(currentStatus);
					break;
				}
				if (gameMessage != null) {
					setChanged();
					notifyObservers(gameMessage);
				}
		}while(!(isGameTie || isGameWin));
		
	}

	public PlayerModel_server getCurrentPlayer() {
		return currentPlayer;
	}

	public char getCurrentGamePiece() {
		return currentPlayerGamePiece;
	}

	public char getOpponentGamePiece() {
		return opponetGamePiece;
	}

	public int getCurrentgameMove() {
		return currentUserMove;
	}

	/***
	 * Game initialized and New Thread started for Player
	 * @param gameBoard
	 * @param p1
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void init(Connect4GameModel_server gameBoard, PlayerModel_server p1) throws IOException, ClassNotFoundException {
		
		connect4Board = gameBoard;
		myPlayers = new PlayerModel_server[2];
		myPlayers[0] = p1;
		new Thread(myPlayers[0]).start();
	}

	public Connect4GameModel_server getGameBoard() {
		return connect4Board;
	}

	public void setGameTie() {
		isGameTie = true;
	}

	public void setGameError(String error) {
		gameMessage = error;
	}

	public void setGameWin() {
		isGameWin = true;
	}
	
}
