/***
 * @author Aarti Gorade
 * @author Lakshmi Gorade
 * 
 * Server Test class implementation with RMI
 * 
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;



public class TestConnect4Game_server extends UnicastRemoteObject implements nextMoveInterface{
	
	static GameControl_server gameControl_server;
	static nextMoveInterface testRMIObject;
	static String hostname;
	
	
	protected TestConnect4Game_server() throws RemoteException {
		super();
	}

	private static void printMessage()	{
		System.out.println("-h		---->	help");
		System.out.println("[-symbol 	symbol");
		System.out.println("[-host 		hostName");
		System.out.println(" -port 		port");
		System.out.println(" {-port 		port}");
		System.out.println("or ");
		System.out.println(" no argument");
		System.exit(0);
	   }

	@Override
	/***
	 * nextMoveInterface Interface method implementation 
	 * returns nextMove Updated on RMIRegistry
	 */
	public String nextMove() throws RemoteException {
		return GameControl_server.currentStatus;
	}
	
	/***
	 * nextMoveInterface Interface method implementation 
	 * Update RMI Registry with input string received from player
	 */
	public void updateRegistry(String s){
		try {
			GameControl_server.currentStatus=s;
			Naming.rebind("//"+hostname+"/currentStatus", testRMIObject);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, IOException{
		hostname=InetAddress.getLocalHost().getHostName();
		char chosenSymbol='*';
		for (int i = 0; i < args.length; i ++) {
			   	if (args[i].equals("-h")) 
					printMessage();
			   	else if (args[i].equals("-symbol")) {
			   		chosenSymbol = args[++i].charAt(0);
			   	}
			}
		
	testRMIObject=new TestConnect4Game_server();
	gameControl_server = new GameControl_server(testRMIObject);
	Connect4UserView_server gameView = new Connect4UserView_server();
	gameControl_server.addObserver(gameView);
	Connect4GameModel_server gameBoard = new Connect4GameModel_server();
	
	System.out.println("Please select\n 1: human vs Computer game\n 2. Human vs Human");
	int input=1;
	
	Scanner myScanner = new Scanner(System.in);
	if(myScanner.hasNextLine()){
		input= myScanner.nextInt();
	}
	
	if(input == 1){
	PlayerModel_server compPlayer = new ComputerPlayer(chosenSymbol, "Comp_Player",gameBoard,gameControl_server);
	gameControl_server.addObserver(compPlayer);
	gameControl_server.addObserver(gameBoard);
	gameControl_server.init(gameBoard,compPlayer);
	
	}
	else{
		PlayerModel_server humPlayer = new HumanPlayer_server(chosenSymbol,"Human_Player1", gameBoard,gameControl_server);
		gameControl_server.addObserver(gameBoard);
		gameControl_server.init(gameBoard,humPlayer);
		
	}
}

}