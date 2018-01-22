package com.mygdx.game.server;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.equipment.Equipment;
import com.mygdx.game.states.PlayState;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

public class Packets {
	
	public static class Packet01Message {
		public Packet01Message() {}
		public Packet01Message(String m) {
			message = m;
		}
		public String message;
	}
	
	public static class Packet02Input {
		public Input.Keys message;
	}
	
	public static class Packet03Click {
		public Vector2 location;
		public Equipment usedTool;
	}

	public static class ReadyToPlay {

    }

	public static class Packet04EnterPlayState {

		/*public Packet04EnterPlayState (PlayState playState) {
			ps = playState;
		}
		public PlayState ps;*/
	}
}
