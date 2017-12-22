package com.mygdx.game.client;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.game.server.Packets.*;

public class KryoClient {

	int portSocket = 25565;
	String ipAddress = "localhost";
	
	public Client client;
	public ClientNetworkListener clientListener;
	
	public static final int timeout = 5000;
	
	
	public KryoClient() {
		this.client = new Client();
		this.clientListener = new ClientNetworkListener();
		
		clientListener.init(client);
		registerPackets();
		client.addListener(clientListener);
		
		client.start();
		
		try {
			client.connect(timeout, ipAddress, portSocket);
		}catch(IOException e) {
		}
	}
	
	private void registerPackets() {
		Kryo kryo = client.getKryo();
		kryo.register(Packet01Message.class);
	}
}
