/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Client control implementation with RMI
 * 
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Observable;


public class GameControl_client extends Observable{

	private int currentUserMove;
	private boolean isGameTie;
	private boolean isGameWin;
	private Connect4GameModel_client connect4Board;
	private PlayerModel_client myPlayers[];
	private PlayerModel_client currentPlayer;
	private char currentPlayerGamePiece;
	private char opponetGamePiece;
	private String gameMessage;
	private static int index=0;
	private static String currentStatus="0 0 2";
	private final int myNumber=1;
	private static int whosTurn=0;
	private static boolean boardUpdated=false;
	private static nextMoveInterface c;
	private static String hostname="Aartis-MacBook-Pro.local";
	
	/***
	 * Constructor lookup for RMI object and start fetching nextMove status 
	 * updated by other players.
	 * 
	 * @param args
	 */
	public GameControl_client(String args[]){
		try {
			c=(nextMoveInterface)(Naming.lookup("//"+hostname+"/currentStatus"));
			currentStatus=fetchFromRegistry();
		} catch (Exception e) {
		
			e.printStackTrace();
		} 
	}
	
	/***
	 * Fetch nextMove from RMI Registry
	 * @return nextMOve value
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public String fetchFromRegistry() throws MalformedURLException, RemoteException, NotBoundException{
		return c.nextMove();
	}
	
	/***
	 * Updated nextMove calculated to RMIRegistry so that other players can use it
	 * 
	 * @param currentStatus : Update nextMove calculated
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public void updateRMI(String currentStatus) throws RemoteException, MalformedURLException, NotBoundException{
		c.updateRegistry(currentStatus);
	}

	/**
	 * Plays the actual game Player-1 and 2 makes their moves
	 * If the player is Human Player, notifies View for input move.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws NotBoundException 
	 * @throws InterruptedException 
	 * 
	 */
	
		
	public void playTheGame() throws ClassNotFoundException, IOException, NotBoundException, InterruptedException {
		
		do{
			currentStatus=fetchFromRegistry();
			String[] tokens=currentStatus.split("\\s+");
			if(tokens[0].equals("Game")){
				 System.out.println(currentStatus);
				 break;
			}
			currentUserMove=Integer.parseInt(tokens[0]);
			currentPlayerGamePiece=tokens[1].charAt(0);
			whosTurn=Integer.parseInt(tokens[2]);
			if((whosTurn==myNumber) && !boardUpdated){
					setChanged();
					notifyObservers(index);
					setChanged();
					notifyObservers(this);
					
					if (isGameTie || isGameWin) {
						gameMessage = "Game Won by Another Player ";
						setChanged();
						notifyObservers(this);
						setChanged();
						notifyObservers(gameMessage);
						currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+((whosTurn+1)%2);
						updateRMI(currentStatus);
						currentStatus=gameMessage;
						updateRMI(currentStatus);
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
					updateRMI(currentStatus);
					
					setChanged();
					notifyObservers(index);
					setChanged();
					notifyObservers(this);
					boardUpdated=true;
			 }else{
				 boardUpdated=false;
				 
			 }
			
				if (isGameTie || isGameWin) {
					gameMessage = "Game Won by Player "
							+ myPlayers[index].getName();
					setChanged();
					notifyObservers(this);
					setChanged();
					notifyObservers(gameMessage);
					currentStatus=currentUserMove+" "+currentPlayerGamePiece+" "+((whosTurn+1)%2);
					updateRMI(currentStatus);
					currentStatus=gameMessage;
					updateRMI(currentStatus);
					break;
				}
				if (gameMessage != null) {
					setChanged();
					notifyObservers(gameMessage);
				}
				
		}while((!(isGameTie || isGameWin)));
	}

	

	public PlayerModel_client getCurrentPlayer() {
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
	 * @param p2
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void init(Connect4GameModel_client gameBoard, PlayerModel_client p2) throws ClassNotFoundException, IOException {
		
		connect4Board = gameBoard;
		myPlayers = new PlayerModel_client[2];
		myPlayers[0] = p2;
		new Thread(myPlayers[0]).start();
		System.out.println("Client player started");
	}

	public Connect4GameModel_client getGameBoard() {
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
