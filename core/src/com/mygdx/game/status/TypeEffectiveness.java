package com.mygdx.game.status;

import java.util.Arrays;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.entities.userdata.CharacterData;

import box2dLight.RayHandler;

public class TypeEffectiveness extends Status {

	private static String name = "Damage Resistance";
	private float power;
	private DamageTypes type;
	
	public TypeEffectiveness(PlayState state, World world, OrthographicCamera camera, RayHandler rays, 
			int i, float power, DamageTypes resisted, CharacterData p, CharacterData v, int pr) {
		super(state, world, camera, rays, i, name, false, false, true, true, p, v, pr);
		this.type = resisted;
		this.power = power;
	}
	
	public TypeEffectiveness(PlayState state, World world, OrthographicCamera camera, RayHandler rays, 
			float power, DamageTypes resisted, CharacterData p, CharacterData v, int pr) {
		super(state, world, camera, rays, 0, name, true, false, false, false, p, v, pr);
		this.type = resisted;
		this.power = power;
	}
	
	@Override
	public float onDealDamage(float damage, CharacterData vic, DamageTypes... tags) {
		if (Arrays.asList(tags).contains(type)) {
			damage *= power;
		}
		return damage;
	}

}
