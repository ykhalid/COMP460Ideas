package com.mygdx.game.server;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.manager.GameStateManager;

public class KryoServer {

	int serverPort = 25565;
	int players = 0;
	Server server;
	ServerNetworkListener serverListener;
	GameStateManager gsm;

	public KryoServer(GameStateManager gameStateManager) {
		this.server = new Server();
		this.serverListener = new ServerNetworkListener();
		gsm = gameStateManager;

		server.addListener(new Listener() {
			public void disconnected(Connection c) {
				// This message should be sent when a player disconnects from the game

			}

			public void received(Connection c, Object o) {
				Log.info("" + (o.getClass().getName()));
				if (o instanceof Packets.Packet01Message) {
					// We have received a player connection message.
					Packets.Packet01Message p = (Packets.Packet01Message) o;

					// Ignore the object if the name is invalid.
					String name = ((Packets.Packet01Message)o).message;
					if (name == null) {
						server.sendToTCP(c.getID(), new Packets.Packet01Message("Invalid Player name."));
						return;
					}
					name = name.trim();
					if (name.length() == 0) {
						server.sendToTCP(c.getID(), new Packets.Packet01Message("Cannot have empty player name."));
						return;
					}
					Packets.Packet01Message newPlayer = new Packets.Packet01Message( name + " has joined the game server.");
					Log.info(name + " has joined the game.");
					server.sendToAllExceptTCP(c.getID(), newPlayer);
					server.sendToTCP(c.getID(), new Packets.IDMessage(c.getID()));

				}

				else if (o instanceof Packets.Packet02Input) {
					// We have received a player movement message.
					Packets.Packet02Input p = (Packets.Packet02Input) o;
					server.sendToAllTCP(p);
				}

				else if (o instanceof Packets.Packet03Click) {
					// We have received a mouse click.
					Packets.Packet03Click p = (Packets.Packet03Click) o;
				}

				else if (o instanceof Packets.PacketReadyToPlay) {
					Log.info("Server received ReadyToPlay");
				    Packets.PacketReadyToPlay p = (Packets.PacketReadyToPlay) o;
				    players += 1;
					Log.info("Player " + c.getID() + " ready.");
				    if (players == 2) {
				        server.sendToAllTCP(new Packets.Packet04EnterPlayState());
                    }
                }
			}
		});

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
		kryo.register(Packets.Packet03Click.class);
		kryo.register(Packets.Packet04EnterPlayState.class);
		kryo.register(Packets.PacketReadyToPlay.class);
		kryo.register(Packets.IDMessage.class);

	}
}

