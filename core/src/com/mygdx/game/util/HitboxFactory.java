package com.mygdx.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.Hitbox;
import com.mygdx.game.entities.Schmuck;
import com.mygdx.game.states.PlayState;

import box2dLight.RayHandler;

/**
 * The HitboxFactory is a thing that creates a hitbox when asked.
 * @author Zachary Tu
 *
 */
public abstract class HitboxFactory {
	
	/**
	 * Create a hitbox. This is used by most weapons.
	 * @param user: The creator of the hitbox. Hitboxes must be sourced to a Schmuck
	 * @param state: The game state.
	 * @param startVelocity: Initial speed vector of the hitbox
	 * @param x: Starting x of the hitbox.
	 * @param y: Starting y of the hitbox.
	 * @param filter: What does this hit box hit? 0 for everything. Otherwise, player/enemy_hitbox
	 * @param world: current world
	 * @param camera: current camera
	 * @param rays: current rays
	 * @return: The Hitbox entity created.
	 */
	public abstract Hitbox makeHitbox(Schmuck user, PlayState state, Vector2 startVelocity, float x, float y, short filter,
			World world, OrthographicCamera camera, RayHandler rays);
}
