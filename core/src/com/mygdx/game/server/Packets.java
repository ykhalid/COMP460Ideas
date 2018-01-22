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
	    public static final int PRESSED = 0;
        public static final int RELEASED = 1;
	    public Packet02Input() {}
	    public Packet02Input(int m, int pOrR, int playerID) {
	        message = m;
	        pressOrRelease = pOrR;
	        this.playerID = playerID;
        }
        public int playerID;
		public int message;
	    public int pressOrRelease; //0 = pressed, 1 = released.
	}
	
	public static class Packet03Click {
		public Packet03Click() {}
		public Packet03Click(Vector2 loc, Equipment tool, int id, float d) {
		    location = loc;
		    usedTool = tool;
		    playerID = id;
		    delta = d;
        }
        public int playerID;
        public Vector2 location;
		public Equipment usedTool;
		public float delta;
	}

	public static class PacketReadyToPlay {
	    public PacketReadyToPlay() {}
    }

	public static class Packet04EnterPlayState {
        public Packet04EnterPlayState() {}
	}

	public static class IDMessage {
        public IDMessage() {}
        public IDMessage(int ID) {
            this.ID = ID;
        }
        public int ID;
    }
}
