package com.mygdx.game.client;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.server.*;
//import com.mygdx.game.server.Packets;

import javax.swing.*;

public class KryoClient {

	int portSocket = 25565;
	String ipAddress = "localhost";
	
	public Client client;
	public ClientNetworkListener clientListener;
	
	public static final int timeout = 5000;
    String name;
	
	public KryoClient() {
		this.client = new Client();
        client.start();
		this.clientListener = new ClientNetworkListener();
		
		clientListener.init(client);
		registerPackets();

		client.addListener(new Listener() {

			public void connected(Connection c) {
                Packets.Packet01Message connected = new Packets.Packet01Message(name);
                client.sendTCP(connected);
			}

			public void disconnected(Connection c) {

			}

			public void received(Connection c, Object o) {

				if (o instanceof Packets.Packet01Message) {
					Packets.Packet01Message p = (Packets.Packet01Message) o;
				}
			}
		});

        // Request the host from the user.
        String input = (String) JOptionPane.showInputDialog(null, "Host:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE,
                null, null, "localhost");
        if (input == null || input.trim().length() == 0) System.exit(1);
        final String host = input.trim();

        // Request the user's name.
        input = (String)JOptionPane.showInputDialog(null, "Name:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE, null,
                null, "Test");
        if (input == null || input.trim().length() == 0) System.exit(1);
        name = input.trim();



        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(5000, host, portSocket);
                    // Server communication after connection can go here, or in Listener#connected().
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();
	}
	
	private void registerPackets() {
		Kryo kryo = client.getKryo();

		kryo.register(Packets.Packet01Message.class);
		kryo.register(Packets.Packet02Input.class);
        kryo.register(Packets.Packet03Click.class);
        kryo.register(Packets.Packet04EnterPlayState.class);
	}
}
