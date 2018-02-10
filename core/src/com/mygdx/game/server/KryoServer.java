package com.mygdx.game.server;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.states.TitleState;

public class KryoServer {

	int serverPort = 25565;
	int players = 0;
	public Server server;
	GameStateManager gsm;
	boolean setMaster = true;

	public KryoServer(GameStateManager gameStateManager) {
		Kryo kryo = new Kryo();
		kryo.setReferences(true);
		KryoSerialization serialization = new KryoSerialization(kryo);
		this.server = new Server(16384, 2048, serialization);

		gsm = gameStateManager;

		server.addListener(new Listener() {
			public void disconnected(Connection c) {
				// This message should be sent when a player disconnects from the game

			}

			public void received(Connection c, Object o) {
				Log.info("" + (o.getClass().getName()));
				if (o instanceof Packets.PlayerConnect) {
					// We have received a player connection message.
					Packets.PlayerConnect p = (Packets.PlayerConnect) o;

					// Ignore the object if the name is invalid.
					String name = p.message;
					if (name == null) {
						server.sendToTCP(c.getID(), new Packets.PlayerConnect("Invalid Player name."));
						return;
					}
					name = name.trim();
					if (name.length() == 0) {
						server.sendToTCP(c.getID(), new Packets.PlayerConnect("Cannot have empty player name."));
						return;
					}
					Packets.PlayerConnect newPlayer = new Packets.PlayerConnect( name + " has joined the game server.");
					Log.info(name + " has joined the game.");
					server.sendToAllExceptTCP(c.getID(), newPlayer);
					server.sendToTCP(c.getID(), new Packets.IDMessage(c.getID()));
					setMaster = false;

				}

				else if (o instanceof Packets.KeyPressOrRelease) {
					// We have received a player movement message.
					Packets.KeyPressOrRelease p = (Packets.KeyPressOrRelease) o;
					server.sendToAllTCP(p);
				}

				else if (o instanceof Packets.Shoot) {
					// We have received a mouse click.
					Packets.Shoot p = (Packets.Shoot) o;
					server.sendToAllTCP(p);
				}

				else if (o instanceof Packets.ReadyToPlay) {
					Log.info("Server received ReadyToPlay");
				    Packets.ReadyToPlay p = (Packets.ReadyToPlay) o;
				    players += 1;
					Log.info("Player " + c.getID() + " ready.");
				    if (players == 2) {
				        server.sendToAllTCP(new Packets.EnterPlayState());
						Gdx.app.postRunnable(new Runnable() {
							public void run() {
								gsm.addState(GameStateManager.State.PLAY, TitleState.class);
							}
						});
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
					server.sendToAllTCP(p);
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

