package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

public interface IChatServer extends  Remote {
  public void updateChat(String username, String chatMessage)throws RemoteException;
	
	public void passIdentity(RemoteRef ref)throws RemoteException;
	
	public void registerListener(String[] details)throws RemoteException;
	
	public void quitChat(String username)throws RemoteException;
	
	public void privateDM(int[] privateGroup, String privateMessage)throws RemoteException;
}
