package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.userdata.PlayerData;
import com.mygdx.game.event.Event;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

public class Player extends Schmuck {

	protected MoveStates moveState1, moveState2;

	//Fixtures and user data
	protected Fixture viewWedge;
		
	//is the player currently in the process of holding their currently used tool?
	private boolean charging = false;
		
	protected float interactCd = 0.15f;
	protected float interactCdCount = 0;
		
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
	public Player(PlayState state, World world, OrthographicCamera camera, RayHandler rays, int x, int y) {
		super(state, world, camera, rays, x, y, "torpedofish_swim", 250, 161, 161, 250);
	}
	
	/**
	 * Create the player's body and initialize player's user data.
	 */
	public void create() {
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
		
		if (Gdx.input.isKeyPressed((Input.Keys.W))) {
			desiredYVel += playerData.maxSpeed;
		}
		if (Gdx.input.isKeyPressed((Input.Keys.A))) {
			desiredXVel += -playerData.maxSpeed;
		}
		if (Gdx.input.isKeyPressed((Input.Keys.S))) {
			desiredYVel += -playerData.maxSpeed;
		}
		if (Gdx.input.isKeyPressed((Input.Keys.D))) {
			desiredXVel += playerData.maxSpeed;
		}
		
		if (Gdx.input.isKeyPressed((Input.Keys.UP))) {
			desiredYVel += playerData.maxSpeed;
		}
		if (Gdx.input.isKeyPressed((Input.Keys.LEFT))) {
			desiredXVel += -playerData.maxSpeed;
		}
		if (Gdx.input.isKeyPressed((Input.Keys.DOWN))) {
			desiredYVel += -playerData.maxSpeed;
		}
		if (Gdx.input.isKeyPressed((Input.Keys.RIGHT))) {
			desiredXVel += playerData.maxSpeed;
		}
		
		if (Gdx.input.isKeyPressed((Input.Keys.E))) {
			desiredAngleVel += -playerData.maxAngularSpeed;
		}
		
		if (Gdx.input.isKeyPressed((Input.Keys.PERIOD))) {
			desiredAngleVel += -playerData.maxAngularSpeed;
		}
		
		if (Gdx.input.isKeyPressed((Input.Keys.Q))) {
			desiredAngleVel += playerData.maxAngularSpeed;
		}
		
		if (Gdx.input.isKeyPressed((Input.Keys.COMMA))) {
			desiredAngleVel += playerData.maxAngularSpeed;
		}
		
		//Pressing 'R' = reload current weapon.
		if(Gdx.input.isKeyJustPressed((Input.Keys.R))) {
			playerData.currentTool.reloading = true;
		}
		
		//Pressing 'Q' = switch to last weapon.
		if(Gdx.input.isKeyJustPressed((Input.Keys.Q))) {
//			playerData.switchToLast();
		}
		
		//Pressing '1' ... '0' = switch to weapon slot.
		if(Gdx.input.isKeyJustPressed((Input.Keys.NUM_1))) {
			playerData.switchWeapon(1);
		}
		
		if(Gdx.input.isKeyJustPressed((Input.Keys.NUM_2))) {
			playerData.switchWeapon(2);
		}
		
		if(Gdx.input.isKeyJustPressed((Input.Keys.NUM_3))) {
			playerData.switchWeapon(3);
		}
		
		if(Gdx.input.isKeyJustPressed((Input.Keys.NUM_4))) {
			playerData.switchWeapon(4);
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
		
		//Pressing 'E' = interact with an event
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
}
