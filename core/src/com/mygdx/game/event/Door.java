package com.mygdx.game.event;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.event.userdata.EventData;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.UserDataTypes;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

public class Door extends Event {

	private static String name = "Door";
	
	private boolean activated = false;
	
	public Door(PlayState state, World world, OrthographicCamera camera, RayHandler rays, int width,
			int height, int x, int y) {
		super(state, world, camera, rays, name, width, height, x, y);
	}
	
	public void create() {
		this.eventData = new EventData(world, this, UserDataTypes.WALL) {
			public void onActivate(EventData activator) {
				if (!activated) {
					activated = true;
					event.queueDeletion();
				}
			}
		};
		
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 1, 0, true, true, Constants.BIT_WALL, 
				(short) (Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PROJECTILE | Constants.BIT_SENSOR | Constants.BIT_WALL),
				(short) 0, false, eventData);
	}
	
	@Override
	public void controller(float delta) {
		
	}
}