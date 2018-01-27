package com.mygdx.game.status;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.entities.userdata.CharacterData;

import box2dLight.RayHandler;

public class Status {

	//References to game fields.
	public PlayState state;
	protected World world;
	protected OrthographicCamera camera;
	protected RayHandler rays;
	
	public float duration;
	public String name;
	public boolean perm, decay, removedEnd, visible;
	
	public int priority;
	
	public CharacterData perp, vic;
	
	public Status(PlayState state, World world, OrthographicCamera camera, RayHandler rays,
			int i, String n, Boolean perm, Boolean vis, Boolean end, Boolean dec, CharacterData p, CharacterData v, int pr){
		this.state = state;
		this.world = world;
		this.camera = camera;
		this.rays = rays;		
		
		this.duration=i;
		this.name = n;
		this.perm = perm;
		this.visible = vis;
		this.removedEnd = end;
		this.decay = dec;
		this.perp = p;
		this.vic = v;
		this.priority = pr;
	}
	
	public void statChanges(CharacterData bodyData){
		
	}
	
	public void timePassing(float delta) {
		if (decay) { 
			duration -= delta;
			
			if (duration <= 0 && !perm) {
				perp.removeStatus(this);
			}
		}
	}
	
	public float onDealDamage(float damage, CharacterData vic, DamageTypes... tags) {
		return damage;
	}
	
	public float onReceiveDamage(float damage, CharacterData perp, DamageTypes... tags) {
		return damage;
	}
	
	public void onKill(CharacterData vic) {
		
	}
	
	public void onDeath(CharacterData vic) {
		
	}
}
