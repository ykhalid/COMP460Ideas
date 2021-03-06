package com.mygdx.game.event;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.userdata.UserData;
import com.mygdx.game.event.userdata.EventData;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

public class Victory extends Event {

	private static final String name = "VICTORY";

	boolean touched = false;
	
	public Victory(PlayState state, World world, OrthographicCamera camera, RayHandler rays, int width, int height, int x, int y) {
		super(state, world, camera, rays, name, width, height, x, y);
	}
	
	public void create() {

		this.eventData = new EventData(world, this) {
			public void onTouch(UserData fixB) {
				if (!touched) {
					touched = true;
					state.gameOver(true);
					event.queueDeletion();
				}
			}
		};
		
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 1, 0, true, true, Constants.BIT_SENSOR, 
				(short) (Constants.BIT_PLAYER),
				(short) 0, true, eventData);
	}
}
