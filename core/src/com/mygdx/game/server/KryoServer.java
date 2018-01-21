package com.mygdx.game.server;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

public class KryoServer {

	int serverPort = 25565;
	
	Server server;
	ServerNetworkListener serverListener;
	
	public KryoServer() {
		this.server = new Server();
		this.serverListener = new ServerNetworkListener();

		server.addListener(serverListener);
		
		try {
			server.bind(serverPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		registerPackets();
		
		server.start();
	}
	
	private void registerPackets() {
		Kryo kryo = server.getKryo();
		
		kryo.register(Packets.Packet01Message.class);
		kryo.register(Packets.Packet02Input.class);
		
	}
}
