package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.client.KryoClient;
import com.mygdx.game.entities.userdata.PlayerData;
import com.mygdx.game.event.Event;
import com.mygdx.game.server.Packets;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

import java.util.HashMap;

public class Player extends Schmuck implements InputProcessor {

	protected MoveStates moveState1, moveState2;

	//Fixtures and user data
	protected Fixture viewWedge;
	private KryoClient client;
		
	//is the player currently in the process of holding their currently used tool?
	private boolean charging = false;
		
	protected float interactCd = 0.15f;
	protected float interactCdCount = 0;

	//is the button for that respective movement pressed currently?
    public boolean wPressed = false, aPressed = false, sPressed = false, dPressed = false, qPressed = false, ePressed = false;
    public boolean wPressed2 = false, aPressed2 = false, sPressed2 = false, dPressed2 = false, qPressed2 = false, ePressed2 = false;
		
	//user data
	public PlayerData playerData;
	public Event currentEvent;
		
	/**
	 * This constructor is called by the player spawn event that must be located in each map
	 * @param state: current gameState
	 * @param world: box2d world
	 * @param camera: game camera
	 * @param rays: game rayhandler
	 * @param x: player starting x position.
	 * @param y: player starting x position.
	 */
	public Player(KryoClient client, PlayState state, World world, OrthographicCamera camera, RayHandler rays, int x, int y) {
		super(state, world, camera, rays, x, y, "torpedofish_swim", 250, 161, 161, 250);
		this.client = client;
	}
	
	/**
	 * Create the player's body and initialize player's user data.
	 */
	public void create() {
	    Gdx.input.setInputProcessor(this);
		this.playerData = new PlayerData(world, this);
		this.bodyData = playerData;
		
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 1, 0, false, false, Constants.BIT_PLAYER, 
				(short) (Constants.BIT_WALL | Constants.BIT_SENSOR | Constants.BIT_PROJECTILE | Constants.BIT_ENEMY),
				Constants.PLAYER_HITBOX, false, playerData);
        
		FixtureDef fixtureDef = new FixtureDef();
		
		PolygonShape pShape = new PolygonShape();
		fixtureDef.shape = pShape;
		
		fixtureDef.density = 0;
		fixtureDef.filter.categoryBits = Constants.BIT_SENSOR;
		fixtureDef.filter.maskBits = 0;
		fixtureDef.filter.groupIndex = (short) 0;
		pShape.set(new float[]{0, 0, -500, 500, -500, -500});
		
		fixtureDef.isSensor = true;
		
		this.viewWedge = this.body.createFixture(fixtureDef);
		
		super.create();
	}

	/**
	 * The player's controller currently polls for input.
	 */
	public void controller(float delta) {
	
		desiredYVel = 0;
		desiredXVel = 0;
		desiredAngleVel = 0;
		
		if (wPressed) {
			desiredYVel += playerData.maxSpeed;
		}
		if (aPressed) {
			desiredXVel += -playerData.maxSpeed;
		}
		if (sPressed) {
			desiredYVel += -playerData.maxSpeed;
		}
		if (dPressed) {
			desiredXVel += playerData.maxSpeed;
		}

        if (wPressed2) {
            desiredYVel += playerData.maxSpeed;
        }
        if (aPressed2) {
            desiredXVel += -playerData.maxSpeed;
        }
        if (sPressed2) {
            desiredYVel += -playerData.maxSpeed;
        }
        if (dPressed2) {
            desiredXVel += playerData.maxSpeed;
        }
		
		if (ePressed) {
			desiredAngleVel += -playerData.maxAngularSpeed;
		}
		if (ePressed2) {
			desiredAngleVel += -playerData.maxAngularSpeed;
		}
		
		if (qPressed) {
			desiredAngleVel += playerData.maxAngularSpeed;
		}
		if (qPressed2) {
			desiredAngleVel += playerData.maxAngularSpeed;
		}
		
		//Clicking left mouse = use tool. charging keeps track of whether button is held.
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			charging = true;
			useToolStart(delta, playerData.currentTool, Constants.PLAYER_HITBOX, Gdx.input.getX() , Gdx.graphics.getHeight() - Gdx.input.getY(), true);
		} else {
			if (charging) {
				useToolRelease(playerData.currentTool, Constants.PLAYER_HITBOX, Gdx.input.getX() , Gdx.graphics.getHeight() - Gdx.input.getY());
			}
			charging = false;
		}
		
		//Pressing 'SPACE' = interact with an event
		if(Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
			if (currentEvent != null && interactCdCount < 0) {
				interactCdCount = interactCd;
				currentEvent.eventData.onInteract(this);
			}
		}
				
		//If player is reloading, run the reload method of the current equipment.
		if (playerData.currentTool.reloading) {
			playerData.currentTool.reload(delta);
		}				
				
		interactCdCount-=delta;

		super.controller(delta);
	}
	
	public void dispose() {
		super.dispose();
	}
	
	public PlayerData getPlayerData() {
		return playerData;
	}

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.W) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.W, Packets.Packet02Input.PRESSED, -1));
        }
        if (keycode == Input.Keys.A) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.A, Packets.Packet02Input.PRESSED, -1));
        }
        if (keycode == Input.Keys.S) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.S, Packets.Packet02Input.PRESSED, -1));
        }
        if (keycode == Input.Keys.D) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.D, Packets.Packet02Input.PRESSED, -1));
        }
        if (keycode == Input.Keys.Q) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.Q, Packets.Packet02Input.PRESSED, -1));
        }
        if (keycode == Input.Keys.E) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.E, Packets.Packet02Input.PRESSED, -1));
        }

        //Pressing 'R' = reload current weapon.
        if(keycode == Input.Keys.R) {
            playerData.currentTool.reloading = true;
        }

        //Pressing '1' ... '0' = switch to weapon slot.
        if(keycode == Input.Keys.NUM_1) {
            playerData.switchWeapon(1);
        }

        if(keycode == Input.Keys.NUM_2) {
            playerData.switchWeapon(2);
        }

        if(keycode == Input.Keys.NUM_3) {
            playerData.switchWeapon(3);
        }

        if(keycode == Input.Keys.NUM_4) {
            playerData.switchWeapon(4);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.W, Packets.Packet02Input.RELEASED, -1));
        }
        if (keycode == Input.Keys.A) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.A, Packets.Packet02Input.RELEASED, -1));
        }
        if (keycode == Input.Keys.S) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.S, Packets.Packet02Input.RELEASED, -1));
        }
        if (keycode == Input.Keys.D) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.D, Packets.Packet02Input.RELEASED, -1));
        }
        if (keycode == Input.Keys.Q) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.Q, Packets.Packet02Input.RELEASED, -1));
        }
        if (keycode == Input.Keys.E) {
            client.client.sendTCP(new Packets.Packet02Input(Input.Keys.E, Packets.Packet02Input.RELEASED, -1));
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
