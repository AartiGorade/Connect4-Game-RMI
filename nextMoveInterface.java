
public interface nextMoveInterface extends java.rmi.Remote{

	public String nextMove() throws java.rmi.RemoteException;

	public void updateRegistry(String s) throws java.rmi.RemoteException;
	
}
