package com.mygdx.game.event;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.Schmuck;
import com.mygdx.game.entities.StandardEnemy;
import com.mygdx.game.entities.SteeringEnemy;
import com.mygdx.game.event.userdata.EventData;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

public class TriggerSpawn extends Event {
	
	public int id;
	public int limit;
	
	public float spawnCount = 0;
	public int amountCount = 0;
	
	public int spawnX, spawnY;
	
	private static final String name = "Schmuck Spawner";

	private ArrayList<Schmuck> spawns;
	
	private float controllerCount = 0;

	boolean defeated = false;
	
	public TriggerSpawn(PlayState state, World world, OrthographicCamera camera, RayHandler rays, int width, int height,
			int x, int y, int schmuckId, int limit) {
		super(state, world, camera, rays, name, width, height, x, y);
		this.id = schmuckId;
		this.limit = limit;
		this.spawnX = x;
		this.spawnY = y;
		
		this.spawns = new ArrayList<Schmuck>();
	}
	
	public void create() {

		this.eventData = new EventData(world, this) {
			public void onActivate(EventData activator) {
				for (int i = 0; i < limit; i++) {
					
					int randX = spawnX + (int)( (Math.random() - 0.5) * 100);
					int randY = spawnY + (int)( (Math.random() - 0.5) * 100);
					
					switch(id) {
					    case 0:
						    spawns.add(new StandardEnemy(state, world, camera, rays, 32, 32, randX, randY));
						    break;
                        case 2:
                        	spawns.add(new StandardEnemy(state, world, camera, rays, 24, 24, spawnX, spawnY));
                            break;
                        case 3:
                        	spawns.add(new SteeringEnemy(state, world, camera, rays, 24, 24, spawnX, spawnY));
					}
				}
			}
		};
		
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 1, 0, true, true, Constants.BIT_SENSOR, 
				(short) (Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PROJECTILE),
				(short) 0, true, eventData);
	}
	
	public void controller(float delta) {
		
		if (!defeated) {
			controllerCount+=delta;
			if (controllerCount >= 1f) {
				controllerCount = 0;
				
				if (!spawns.isEmpty()) {
					
					defeated = true;
					
					for (Schmuck s : spawns) {
						
						if (s.getBodyData().currentHp > 0) {
							defeated = false;
						}
					}
					
					if (defeated && getConnectedEvent() != null) {
						getConnectedEvent().eventData.onActivate(eventData);
					}
				}
			}
		}
		
	}
}
