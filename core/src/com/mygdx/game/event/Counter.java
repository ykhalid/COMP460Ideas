package com.mygdx.game.event;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.event.userdata.EventData;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

public class Counter extends Event {

	private static final String name = "Sensor";

	int maxCount;
	int currentCount = 0;
	
	public Counter(PlayState state, World world, OrthographicCamera camera, RayHandler rays, int width, int height,
			int x, int y, int maxCount) {
		super(state, world, camera, rays, name, width, height, x, y);
		this.maxCount = maxCount;
	}
	
	public void create() {
		this.eventData = new EventData(world, this) {
			public void onActivate(EventData activator) {
				currentCount++;
				if (currentCount >= maxCount && event.getConnectedEvent() != null) {
					event.getConnectedEvent().eventData.onActivate(this);
				}
			}
		};
		
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 1, 0, true, true, Constants.BIT_SENSOR, 
				(short) (Constants.BIT_PLAYER),
				(short) 0, true, eventData);
	}
}
