/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Client model implementation with UDP mode of connection
 * 
 */

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Observable;
import java.util.Observer;

public abstract class PlayerModel_client implements Observer,Runnable{
	private char gameChar;
	private String playerName;
	protected Connect4GameModel_client connect4Board;
	private GameControl_client gameControl_client;
	
	
	public abstract int makeNextMove();
	
	public PlayerModel_client(char gameChar, String name, Connect4GameModel_client board,GameControl_client gameControl_client){
		this.gameChar=gameChar;
		playerName= name;
		connect4Board=board;
		this.gameControl_client=gameControl_client;
	}
	public char getGamePiece(){
		return gameChar;
	}
	public String getName(){
		return playerName;
	}
	public void update(Observable arg0, Object arg1) {
		if( arg1 instanceof Connect4GameModel_client){
			connect4Board = (Connect4GameModel_client)arg1;
		}
	}
	
	public void run(){
		try {
			gameControl_client.playTheGame();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
