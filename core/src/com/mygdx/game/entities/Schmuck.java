package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.equipment.Equipment;
import com.mygdx.game.entities.userdata.CharacterData;
import com.mygdx.game.states.PlayState;

import box2dLight.RayHandler;

public class Schmuck extends Entity {

	//user data.
	protected CharacterData bodyData;
	
	//Counters that keep track of delay between action initiation + action execution and action execution + next action
	public float shootCdCount = 0;
	public float shootDelayCount = 0;
	
	//The last used tool. This is used to process equipment with a delay between using and executing.
	public Equipment usedTool;
	
	//This counter keeps track of elapsed time so the entity behaves the same regardless of engine tick time.
	public float controllerCount = 0;
	
	public float desiredXVel = 0.0f;
	public float desiredYVel = 0.0f;
	
	public float desiredAngleVel = 0.0f;
	
	public float acceleration = 0.1f;
	
	/**
	 * This constructor is called when a Schmuck is made.
	 * @param state: Current playState
	 * @param world: Box2d world
	 * @param camera: Game camera
	 * @param rays: game rayhandler
	 * @param w: width
	 * @param h: height
	 * @param startX: starting x position
	 * @param startY: starting y position
	 */
	public Schmuck(PlayState state, World world, OrthographicCamera camera, RayHandler rays, float w, float h,
			float startX, float startY) {
		super(state, world, camera, rays, w, h, startX, startY);
	}

	/**
	 * When this schmuck is added to the world, give it a foot to keep track of whether it is grounded or not.
	 * IMPORTANT: this method does not create the entity's body! 
	 * Subclasses must create the schmuck's body before calling super.create()! Otherwise body + bodyData will be null.
	 */
	@Override
	public void create() {
		this.userData = bodyData;
	}

	
	/**
	 * The basic behaviour of a schmuck depends on its moveState.
	 * This method contains some physics that constrains schmucks in addition to box2d stuff.
	 */
	@Override
	public void controller(float delta) {
		controllerCount+=delta;
		if (controllerCount >= 1/60f) {
			controllerCount -= 1/60f;
			
			Vector2 currentVel = body.getLinearVelocity();
			
			float newX = acceleration * desiredXVel + (1 - acceleration) * currentVel.x;
			
			float newY = acceleration * desiredYVel + (1 - acceleration) * currentVel.y;
			
			Vector2 force = new Vector2(newX - currentVel.x, newY - currentVel.y).scl(body.getMass());
			body.applyLinearImpulse(force, body.getWorldCenter(), true);
			
			desiredXVel = 0.0f;
			desiredYVel = 0.0f;
			
			float currentAngleVel = body.getAngularVelocity();
			
			float newAngleVel = acceleration * desiredAngleVel + (1 - acceleration) * currentAngleVel;
			
			
			float angularForce = (newAngleVel - currentAngleVel) * (body.getMass());
			body.applyAngularImpulse(angularForce, true);
			
			desiredAngleVel = 0.0f;
		}
		
		//Apply base hp regen
		bodyData.regainHp(bodyData.hpRegen * delta);
		
		//process cooldowns
		shootCdCount-=delta;
		shootDelayCount-=delta;
		
		//If the delay on using a tool just ended, use thte tool.
		if (shootDelayCount <= 0 && usedTool != null) {
			useToolEnd();
		}
	}
	


	/**
	 * Draw the schmuck
	 */
	@Override
	public void render(SpriteBatch batch) {
		
	}
	
	/**
	 * This method is called when a schmuck wants to use a tool.
	 * @param delta: Time passed since last usage. This is used for Charge tools that keep track of time charged.
	 * @param tool: Equipment that the schmuck wants to use
	 * @param hitbox: aka filter. Who will be affected by this equipment? Player or enemy or neutral?
	 * @param x: x screen coordinate that represents where the tool is being directed.
	 * @param y: y screen coordinate that represents where the tool is being directed.
	 * @param wait: Should this tool wait for base cooldowns. No for special tools like built-in airblast/momentum freezing/some enemy attacks
	 */
	public void useToolStart(float delta, Equipment tool, short hitbox, int x, int y, boolean wait) {
		
		//Only register the attempt if the user is not waiting on a tool's delay or cooldown. (or if tool ignores wait)
		if ((shootCdCount < 0 && shootDelayCount < 0) || !wait) {
	
			//account for the tool's use delay.
			shootDelayCount = tool.useDelay;
			
			//Register the tool targeting the input coordinates.
			tool.mouseClicked(delta, state, bodyData, hitbox, x, y, world, camera, rays);
			
			//set the tool that will be executed after delay to input tool.
			usedTool = tool;
		}
	}

	/**
	 * This method is called after a tool is used following the tool's delay.
	 */
	public void useToolEnd() {
			
		//the schmuck will not register another tool usage for the tool's cd
		shootCdCount = usedTool.useCd;
		
		//execute the tool.
		usedTool.execute(state, bodyData, world, camera, rays);
		
		//clear the used tool field.
		usedTool = null;
	}

	/**
	 * This method is called after the user releases the button for a tool. Mostly used by charge weapons that execute when releasing
	 * instead of after pressing.
	 * @param tool: tool to release
	 * @param hitbox: aka filter. Who will be affected by this equipment? Player or enemy or neutral?
	 * @param x: x screen coordinate that represents where the tool is being directed.
	 * @param y: y screen coordinate that represents where the tool is being directed.
	 */
	public void useToolRelease(Equipment tool, short hitbox, int x, int y) {
		tool.release(state, bodyData, world, camera, rays);
	}

}
