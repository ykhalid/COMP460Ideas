package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.states.PlayState;

import box2dLight.RayHandler;

public class RangedHitbox extends Hitbox {

	public RangedHitbox(PlayState state, float x, float y, int width, int height, float lifespan, int dura,
			float rest, Vector2 startVelo, short filter, boolean sensor, World world, OrthographicCamera camera,
			RayHandler rays, Schmuck creator) {
		super(state, x, y, 
				(int) (width * (1 + creator.getBodyData().getProjectileSize())), 
				(int) (height * (1 + creator.getBodyData().getProjectileSize())), 
				lifespan * (1 + creator.getBodyData().getProjectileLifespan()),
				(int) (dura + creator.getBodyData().getProjectileDurability()), 
				rest + creator.getBodyData().getProjectileBounciness(), 
				startVelo.scl(1 + creator.getBodyData().getProjectileSpeed()), filter, sensor, world, camera, rays, creator);
	}

}
