package com.mygdx.game.entities;

import static com.mygdx.game.util.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.userdata.UserData;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.UserDataTypes;
import com.mygdx.game.util.b2d.BodyBuilder;

import box2dLight.RayHandler;

public class ParticleEntity extends Entity {

	private ParticleEffect effect;
	private Entity attachedEntity;
	private float lifespan;
	private boolean despawn;
	
	public ParticleEntity(PlayState state, World world, OrthographicCamera camera, RayHandler rays,
			float startX, float startY, ParticleEffect effect, float lifespan) {
		super(state, world, camera, rays, 0, 0, startX, startY);
		this.effect = effect;
		this.despawn = false;
		this.lifespan = lifespan;
		
		effect.start();
	}
	
	public ParticleEntity(PlayState state, World world, OrthographicCamera camera, RayHandler rays,
			Entity entity, ParticleEffect effect, float lifespan) {
		super(state, world, camera, rays, 0, 0, 0, 0);
		this.attachedEntity = entity;
		this.effect = effect;
		this.despawn = false;
		this.lifespan = lifespan;
		effect.start();

	}

	@Override
	public void create() {
		this.userData = new UserData(world, UserDataTypes.FEET, this);
		this.body = BodyBuilder.createBox(world, startX, startY, width, height, 1, 0, 0, false, true, Constants.BIT_PLAYER, 
				(short) (Constants.BIT_WALL | Constants.BIT_SENSOR | Constants.BIT_PROJECTILE | Constants.BIT_ENEMY),
				Constants.PLAYER_HITBOX, true, userData);
	}

	@Override
	public void controller(float delta) {
		if (attachedEntity.alive) {
			effect.setPosition(attachedEntity.getBody().getPosition().x * PPM, attachedEntity.getBody().getPosition().y * PPM);
		} else {
			despawn = true;
			effect.allowCompletion();
		}
		
		if (despawn) {
			lifespan -= delta;
			
			if (lifespan <= 0) {
				this.queueDeletion();
			}
		}
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(state.sprite.combined);
		effect.draw(batch, Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose() {
		effect.dispose();
		super.dispose();
	}

}
