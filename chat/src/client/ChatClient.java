package client;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import server.IChatServer;

public class ChatClient extends UnicastRemoteObject implements IChatClient {
  ChatUI chatUI;
  private String host = "localhost";
  private String service = "GroupChatService";
  private String clientService;
  private String name;
  protected IChatServer IServer;
  protected boolean connectionProblem = false;

  public ChatClient(ChatUI chatUI, String username) throws RemoteException {
    super();
    this.chatUI = chatUI;
    this.name = username;
    this.clientService = "ClientListenService_" + username;
  }

    public void startClient() throws RemoteException {
        String[] details = {name, host, clientService};
		try {
			Naming.rebind("rmi://" + host + "/" + clientService, this);
			IServer = ( IChatServer )Naming.lookup("rmi://" + host + "/" + service);	
		} 
		catch (ConnectException  e) {
			JOptionPane.showMessageDialog(
					chatUI.frame, "Le serveur semble être indisponible\nVeuillez réessayer ultérieurement",
					"Problème de connexion", JOptionPane.ERROR_MESSAGE);
			connectionProblem = true;
			e.printStackTrace();
		}
		catch(NotBoundException | MalformedURLException me){
			connectionProblem = true;
			me.printStackTrace();
		}
		if(!connectionProblem){
			registerWithServer(details);
		}	
		System.out.println("Le serveur RMI est en cours d'écoute...\n");
    }
    public void registerWithServer(String[] details) {
        try {
            IServer.passIdentity(this.ref);
            IServer.registerListener(details);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void messageFromServer(String message) throws RemoteException {
        System.out.println(message);
        chatUI.textArea.append(message);
        chatUI.textArea.setCaretPosition(chatUI.textArea.getDocument().getLength());
    }
    @Override
    public void updateUserList(String[] currentUsers) throws RemoteException {
        if (currentUsers.length < 2) {
            chatUI.privateMsgButton.setEnabled(false);            
        }
        chatUI.userPanel.remove(chatUI.clientPanel);
        chatUI.setClientPanel(currentUsers);
        chatUI.clientPanel.repaint();
        chatUI.clientPanel.revalidate();
    }
}
