package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.server.Packets.*;

public class ServerNetworkListener extends Listener {
	public int playerCount = 0;

	public ServerNetworkListener() {
		
	}
	
	public void disconnected(Connection c) {
		// This message should be sent when a player disconnects from the game

	}
	
	public void received(Connection c, Object o) {
		
		if (o instanceof Packets.Packet01Message) {
			// We have received a player connection message.
			Packet01Message p = (Packet01Message) o;

			// Ignore the object if the name is invalid.
			String name = ((Packet01Message)o).message;
//			if (name == null) return server.s ;
			name = name.trim();
			if (name.length() == 0) return;
			// Store the name on the connection.

		}
		
		if (o instanceof Packets.Packet02Input) {
			// We have received a player movement message.
			Packet02Input p = (Packet02Input) o;
		}

		if (o instanceof Packets.Packet03Click) {
			// We have received a mouse click.
			Packet03Click p = (Packet03Click) o;
		}
	}
}
