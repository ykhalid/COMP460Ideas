package com.mygdx.game.desktop;

import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.comp460game;
import com.mygdx.game.networking.ChatClient;
import com.mygdx.game.networking.ChatServer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1080;
		config.height = 720;
//		config.fullscreen = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
/*		try {
			new ChatServer();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	new ChatClient();*/

		new LwjglApplication(new comp460game(), config);
	}
}
