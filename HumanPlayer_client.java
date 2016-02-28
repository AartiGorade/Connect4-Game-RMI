/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Human player client model implementation with UDP mode of connection
 * 
 */

public class HumanPlayer_client extends PlayerModel_client{
	public HumanPlayer_client(char gameChar, String name, Connect4GameModel_client board,GameControl_client gameControl_client) {
		super(gameChar, name, board, gameControl_client);
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
