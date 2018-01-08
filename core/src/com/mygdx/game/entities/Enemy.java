package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.userdata.CharacterData;
import com.mygdx.game.equipment.Equipment;
import com.mygdx.game.equipment.ranged.EnemyGun;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

public class Enemy extends Schmuck {

	public float maxSpeed = 12.0f;
	
	public float randSpeed;
	
	//enemy's speed randomly varies to avoid total stacking. Obv change this later when implementing actual ai.
	public float randSpeedCd = 3.0f;
	public float randSpeedCdCount = 0.0f;
	
	
	public Equipment weapon;
	
	public Enemy(PlayState state, World world, OrthographicCamera camera, RayHandler rays, float w, float h,
			float startX, float startY) {
		super(state, world, camera, rays, w, h, startX, startY);
		weapon = new EnemyGun(this);
		randSpeed = maxSpeed;
	}

	/**
	 * Create the enemy's body and initialize player's user data.
	 */
	public void create() {
		this.bodyData = new CharacterData(world, this);
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 1, 0, false, true, Constants.BIT_ENEMY, 
				(short) (Constants.BIT_WALL | Constants.BIT_SENSOR | Constants.BIT_PROJECTILE | Constants.BIT_PLAYER | Constants.BIT_ENEMY),
				Constants.ENEMY_HITBOX, false, bodyData);
	}
	
	@Override
	public void controller(float delta) {
		Vector2 player = state.getPlayer().getBody().getPosition();
		
		if(body.getPosition().x < player.x) {
			desiredXVel = randSpeed;
		} else {
			desiredXVel = -randSpeed;
		}
		
		if(body.getPosition().y < player.y) {
			desiredYVel = randSpeed;
		} else {
			desiredYVel = -randSpeed;
		}

		Vector3 target = new Vector3(state.getPlayer().getBody().getPosition().x, state.getPlayer().getBody().getPosition().y, 0);
		camera.project(target);
		useToolStart(delta, weapon, Constants.ENEMY_HITBOX, (int)target.x, (int)target.y, true);

		
		if (randSpeedCdCount > randSpeedCd) {
			randSpeedCdCount -= randSpeedCd;
			randSpeed = (float) (maxSpeed * (Math.random())) * 2;
		}
		
		randSpeedCdCount += delta;
		
		super.controller(delta);
	}
	
	/**
	 * draws enemy
	 */
	public void render(SpriteBatch batch) {
		
	}
	
	/**
	 * Deletes enemy. Currently also increments game score.
	 */
	public void dispose() {
		state.incrementScore(1);
		super.dispose();
	}
	
	
}
