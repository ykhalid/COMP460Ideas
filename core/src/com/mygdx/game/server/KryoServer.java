package com.mygdx.game.server;

import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.equipment.ranged.Gun;
import com.mygdx.game.manager.GameStateManager;

public class KryoServer {

	int serverPort = 25565;
	int players = 0;
	Server server;
	ServerNetworkListener serverListener;
	GameStateManager gsm;
	boolean setMaster = true;

	public KryoServer(GameStateManager gameStateManager) {
		Kryo kryo = new Kryo();
		kryo.setReferences(true);
		KryoSerialization serialization = new KryoSerialization(kryo);
		this.server = new Server(16384, 2048, serialization);

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
					server.sendToTCP(c.getID(), new Packets.IDMessage(c.getID(), setMaster));
					setMaster = false;

				}

				else if (o instanceof Packets.Packet02Input) {
					// We have received a player movement message.
					Packets.Packet02Input p = (Packets.Packet02Input) o;
					server.sendToAllTCP(p);
				}

				else if (o instanceof Packets.Packet03Click) {
					// We have received a mouse click.
					Packets.Packet03Click p = (Packets.Packet03Click) o;
					server.sendToAllTCP(p);
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

                else if (o instanceof Packets.SyncPlayState) {
					Log.info("Syncing PlayStates...");
					Packets.SyncPlayState p = (Packets.SyncPlayState) o;
					server.sendToAllExceptTCP(c.getID(),p);
				}

				else if (o instanceof Packets.SyncHitbox) {
					Log.info("Syncing Hitbox...");
					Packets.SyncHitbox p = (Packets.SyncHitbox) o;
					server.sendToAllExceptTCP(c.getID(),p);
				}

				else if (o instanceof Packets.SyncCreateSchmuck) {
					Log.info("Syncing Schmuck Creation...");
					Packets.SyncCreateSchmuck p = (Packets.SyncCreateSchmuck) o;
					server.sendToAllExceptTCP(c.getID(),p);
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
		Packets.allPackets(kryo);
	}
}

