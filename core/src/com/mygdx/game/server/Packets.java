package com.mygdx.game.server;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.equipment.Equipment;

public class Packets {
	
	public static class Packet01Message {
		public String message;
	}
	
	public static class Packet02Input {
		public Input.Keys message;
	}
	
	public static class Packet03Click {
		public Vector2 location;
		public Equipment usedTool;
	}

}
