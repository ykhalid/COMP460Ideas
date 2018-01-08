package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.userdata.PlayerData;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

public class Player extends Schmuck {

	//player stats
	private final static int playerWidth = 32;
	private final static int playerHeight = 32;
		
	//is the player currently in the process of holding their currently used tool?
	private boolean charging = false;
		
	//user data
	public PlayerData playerData;
		
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
		super(state, world, camera, rays, playerWidth, playerHeight, x, y);
	}
	
	/**
	 * Create the player's body and initialize player's user data.
	 */
	public void create() {
		this.playerData = new PlayerData(world, this);
		this.bodyData = playerData;
		
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 1, 0, false, true, Constants.BIT_PLAYER, 
				(short) (Constants.BIT_WALL | Constants.BIT_SENSOR | Constants.BIT_PROJECTILE | Constants.BIT_ENEMY),
				Constants.PLAYER_HITBOX, false, playerData);
        
		super.create();
	}

	/**
	 * The player's controller currently polls for input.
	 */
	public void controller(float delta) {
	
		if(Gdx.input.isKeyPressed((Input.Keys.W))) {
			desiredYVel = playerData.maxSpeed;
		}
		if(Gdx.input.isKeyPressed((Input.Keys.A))) {
			desiredXVel = -playerData.maxSpeed;
		}
		if(Gdx.input.isKeyPressed((Input.Keys.S))) {
			desiredYVel = -playerData.maxSpeed;
		}
		if(Gdx.input.isKeyPressed((Input.Keys.D))) {
			desiredXVel = playerData.maxSpeed;
		}
		
		//Pressing 'R' = reload current weapon.
		if(Gdx.input.isKeyJustPressed((Input.Keys.R))) {
			playerData.currentTool.reloading = true;
		}
		
		//Pressing 'Q' = switch to last weapon.
		if(Gdx.input.isKeyJustPressed((Input.Keys.Q))) {
			playerData.switchToLast();
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
		
		//If player is reloading, run the reload method of the current equipment.
		if (playerData.currentTool.reloading) {
			playerData.currentTool.reload(delta);
		}				
				
		super.controller(delta);
	}
	
	public void render(SpriteBatch batch) {
		
	}
	
	public void dispose() {
		super.dispose();
	}
	
	public PlayerData getPlayerData() {
		return playerData;
	}
}
