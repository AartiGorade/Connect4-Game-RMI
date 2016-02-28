/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Client View implementation with UDP mode of connection
 * 
 */

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;


public class Connect4UserView_client implements Observer {	
	
	public void displayBoard(char[][] boardMatrix, int MAX_ROW_NUMBER, int MAX_COLUMN_NUMBER){
		for (int rows = 0; rows < MAX_ROW_NUMBER; rows++) {
			for (int columns = 0; columns < MAX_COLUMN_NUMBER; columns++) {
				if (boardMatrix[rows][columns] == '1') {
					System.out.print(" ");
				} else {
					System.out.print(boardMatrix[rows][columns]);
				}
			}
			System.out.println("");
		}
		System.out.println("*************************************\n");
	}

	
	public int getUserMove(){
		Scanner myScanner = new Scanner(System.in);
		if(myScanner.hasNextLine()){
			String line = myScanner.nextLine();
			try{
			int move = Integer.parseInt(line);
			return move;
			}catch(NumberFormatException ne){
				System.err.println("User Entered Invalid Column number");
				return -1;
			}	
		}
		return -1;
		
	}


	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof GameControl_client){
			displayBoard(((GameControl_client)arg1).getGameBoard().getBoardMatrix(), Connect4GameModel_client.getMaxRows(), Connect4GameModel_client.getMaxColumns());
		}
		else if ( arg1 instanceof PlayerModel_client){
			System.out.println("Turn of the Player  "+((PlayerModel_client)arg1).getName());
			if(arg1 instanceof HumanPlayer_client){
			((HumanPlayer_client) arg1).setUserMove(getUserMove());
			}
			}
		else if(arg1 instanceof String){
			System.out.println(arg1.toString());
		}
		
	}
	
	
}
