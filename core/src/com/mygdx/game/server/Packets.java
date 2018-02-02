package com.mygdx.game.server;

import box2dLight.RayHandler;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryo.Kryo;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.entities.Player;
import com.mygdx.game.equipment.Equipment;
import com.mygdx.game.equipment.RangedWeapon;
import com.mygdx.game.equipment.ranged.Gun;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.states.PlayState;
//import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
//import javafx.stage.Stage;

import java.util.Set;
import java.util.UUID;

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
        public IDMessage(int ID, boolean mas) {
            this.ID = ID;
            master = mas;
        }
        public int ID;
        public boolean master;
    }

    public static class SyncPlayState {
	    public SyncPlayState() {}
	    public SyncPlayState(Vector2 bod, float a) {
	        body = bod;
	        angle = a;
        }
        public Vector2 body;
	    public float angle;
    }

    public static class SyncHitbox {
	    public SyncHitbox() {}
        public SyncHitbox(float x, float y, int width, int height, float lifespan, int dura, float rest,
                          Vector2 startVelo, short filter, boolean sensor) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
	        this.lifespan = lifespan;
            this.filter = filter;
            this.sensor = sensor;
            this.dura = dura;
            this.rest = rest;
            this.startVelo = startVelo;
        }
	    public float x, y, lifespan, rest;
        public int width, height, dura;
        public Vector2 startVelo;
        public short filter;
        public boolean sensor;
    }

    public static class SyncHitboxImage {
        public SyncHitboxImage() {}
        public SyncHitboxImage(float x, float y, int width, int height, float lifespan, int dura, float rest,
                          Vector2 startVelo, short filter, boolean sensor, String spriteID) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.lifespan = lifespan;
            this.filter = filter;
            this.sensor = sensor;
            this.dura = dura;
            this.rest = rest;
            this.startVelo = startVelo;
            this.spriteID = spriteID;
        }
        public float x, y, lifespan, rest;
        public int width, height, dura;
        public Vector2 startVelo;
        public short filter;
        public boolean sensor;
        public String spriteID;
    }

    public static class SyncCreateSchmuck {
	    public SyncCreateSchmuck() {}
	    public SyncCreateSchmuck(float w, float h, float startX, float startY, UUID id) {
	        this.w = w;
	        this.h = h;
	        this.startX = startX;
	        this.startY = startY;
	        this.id = id;
        }
	    public float w, h, startX, startY;
	    public UUID id;
    }
    public static void allPackets(Kryo kryo) {
        kryo.register(Packets.Packet01Message.class);
        kryo.register(Packets.Packet02Input.class);
        kryo.register(Packets.Packet03Click.class);
        kryo.register(Packets.Packet04EnterPlayState.class);
        kryo.register(Packets.PacketReadyToPlay.class);
        kryo.register(Packets.IDMessage.class);
        kryo.register(Vector2.class);
        kryo.register(Gun.class);
        kryo.register(RangedWeapon.class);
        kryo.register(Equipment.class);
        kryo.register(PlayState.class);
        kryo.register(SyncPlayState.class);
        kryo.register(Body.class);
        kryo.register(UUID.class);
        kryo.register(SyncHitbox.class);
        kryo.register(SyncCreateSchmuck.class);
        kryo.register(SyncHitboxImage.class);

        kryo.register(Set.class);
        kryo.register(Entity.class);
        kryo.register(java.util.HashSet.class);
        kryo.register(com.mygdx.game.event.Door.class);
        kryo.register(com.mygdx.game.event.InfoFlag.class);
        kryo.register(com.badlogic.gdx.utils.Array.class);
        kryo.register(com.mygdx.game.event.UsePortal.class);
        kryo.register(Object[].class);
        kryo.register(com.badlogic.gdx.physics.box2d.Fixture.class);

//        kryo.register(Player.class);
//        kryo.register(TiledMap.class);
//        kryo.register(OrthogonalTiledMapRenderer.class);
//        kryo.register(BitmapFont.class);
//        kryo.register(RayHandler.class);
//        kryo.register(Box2DDebugRenderer.class);
//        kryo.register(World.class);
//        kryo.register(Entity.class);
//        kryo.register(Stage.class);
//        kryo.register(GameStateManager.class);
//        kryo.register(SpriteBatch.class);
//        kryo.register(OrthographicCamera.class);
//        kryo.register(com.mygdx.game.comp460game.class);
//        kryo.register(Matrix4.class);
//        kryo.register(float[].class);
//        kryo.register(com.badlogic.gdx.graphics.Mesh.class);
//        kryo.register(com.badlogic.gdx.graphics.glutils.IndexArray.class);

    }
}
