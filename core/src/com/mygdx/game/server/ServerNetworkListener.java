package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.server.Packets.Packet01Message;

public class ServerNetworkListener extends Listener {

	public ServerNetworkListener() {
		
	}
	
	public void connected(Connection c) {
		
	}
	
	public void disconnected(Connection c) {
		
	}
	
	public void received(Connection c, Object o) {
		
		if (o instanceof Packets.Packet01Message) {
			Packet01Message p = (Packet01Message) o;
		}
	}
}
