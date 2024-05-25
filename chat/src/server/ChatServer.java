package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;

import client.IChatClient;

public class ChatServer extends UnicastRemoteObject implements IChatServer {
  
  String line = "≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛≛\n";
  private Vector<ConnectedClient> connectedClients;
	private static final long serialVersionUID = 1L;

  public ChatServer() throws RemoteException {
		super();
		connectedClients = new Vector<ConnectedClient>(10, 1);
	}

   public static void main(String[] args) {
		startRMIRegistry();	
		String host = "localhost";
		String service = "GroupChatService";
		
		if(args.length == 2){
			host = args[0];
			service = args[1];
		}
		
		try{
			IChatServer hello = new ChatServer();
			Naming.rebind("rmi://" + host + "/" + service, hello);
			System.out.println("Group Chat RMI Server is running...");
		}
		catch(Exception e){
			System.out.println("Server had problems starting");
		}	
	}

  private static void startRMIRegistry() {
    try{
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI Server ready");
		}
		catch(RemoteException e) {
			e.printStackTrace();
		}
  }

  public String sayHi(String ClientName) throws RemoteException {
		System.out.println(ClientName + "Envoyé un message");
		return "Bienvenue " + ClientName + " depuis la salle de discussion";
	}

  public void updateChat(String name, String next) throws RemoteException {
		String message =  name + " : " + next + "\n";
		sendToAll(message);
	}

  @Override
	public void passIdentity(RemoteRef ref) throws RemoteException {	
		try{
			System.out.println(line + ref.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

  @Override
	public void registerListener(String[] details) throws RemoteException {	
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println(details[0] + " a rejoint la salle de discussion");
		System.out.println(details[0] + "'s host : " + details[1]);
		System.out.println(details[0] + "'s RMI : " + details[2]);
		registerChatter(details);
	}

  private void registerChatter(String[] details){		
		try{
			IChatClient nextClient = ( IChatClient )Naming.lookup("rmi://" + details[1] + "/" + details[2]);
			
			connectedClients.addElement(new ConnectedClient(details[0], nextClient));
			
			nextClient.messageFromServer("[Server] : Bienvenue " + details[0] + " vous êtes maintenant libre de discuter.\n");
			
			sendToAll("[Server] : " + details[0] + " a rejoint le groupe.\n");
			
			updateUserList();		
		}
		catch(RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
	}

  private void updateUserList() {
    String[] currentUsers = getUserList();	
		for(ConnectedClient c : connectedClients){
			try {
				c.getClient().updateUserList(currentUsers);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
  }

  private String[] getUserList() {
    // generate an array of current users
		String[] allUsers = new String[connectedClients.size()];
		for(int i = 0; i< allUsers.length; i++){
			allUsers[i] = connectedClients.elementAt(i).getName();
		}
		return allUsers;
  }


  private void sendToAll(String text) throws RemoteException {
    for(ConnectedClient c : connectedClients){
			c.getClient().messageFromServer(text);
		}	
  }

  @Override
	public void quitChat(String username) throws RemoteException{
		
		for(ConnectedClient c : connectedClients){
			if(c.getName().equals(username)){
				System.out.println(line + username + " avait quitté la salle de discussion");
				System.out.println(new Date(System.currentTimeMillis()));
				connectedClients.remove(c);
				break;
			}
		}		
		if(!connectedClients.isEmpty()){
			updateUserList();
		}			
	}

  @Override
	public void privateDM(int[] privateGroup, String privateMessage) throws RemoteException{
		ConnectedClient cc;
		for(int i : privateGroup){
			cc= connectedClients.elementAt(i);
			cc.getClient().messageFromServer(privateMessage);
		}
	}

}
