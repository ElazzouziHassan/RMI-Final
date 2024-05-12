package server;

import client.IChatClient;

public class ConnectedClient {
  public String name;
	public IChatClient client;
	
	public ConnectedClient(String name, IChatClient client){
		this.name = name;
		this.client = client;
	}

	
	public String getName(){
		return name;
	}
	public IChatClient getClient(){
		return client;
	}
}
