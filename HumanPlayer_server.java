/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Human Player Server model implementation with UDP mode of connection
 * 
 */

public class HumanPlayer_server extends PlayerModel_server{
	public HumanPlayer_server(char gameChar, String name, Connect4GameModel_server board,GameControl_server gameControl_server) {
		super(gameChar, name, board, gameControl_server);
	}
	private boolean needUserNextMove;
	private int userMove;
	
	public int makeNextMove(char[][] boardMatrix) {
		return userMove;
	}
	
	public void setUserMove(int i){
		userMove = i;
	}
	
	public boolean needUserMove(){
		return needUserNextMove;
	}
	public int makeNextMove() {
		return userMove;
	}



}
