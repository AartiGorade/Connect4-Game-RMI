/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Server model implementation with UDP mode of connection
 * 
 */

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public abstract class PlayerModel_server implements Observer,Runnable{
	private char gameChar;
	private String playerName;
	protected Connect4GameModel_server connect4Board;
	private GameControl_server gameControl_server;
	
	
	public abstract int makeNextMove();
	
	public PlayerModel_server(char gameChar, String name, Connect4GameModel_server board,GameControl_server gameControl_server){
		this.gameChar=gameChar;
		playerName= name;
		connect4Board=board;
		this.gameControl_server=gameControl_server;
	}
	public char getGamePiece(){
		return gameChar;
	}
	public String getName(){
		return playerName;
	}
	public void update(Observable arg0, Object arg1) {
		if( arg1 instanceof Connect4GameModel_server){
			connect4Board = (Connect4GameModel_server)arg1;
		}
	}
	
	public void run(){
		try {
			gameControl_server.playTheGame();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
