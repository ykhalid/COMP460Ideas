package com.mygdx.game.entities.userdata;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.Schmuck;
import com.mygdx.game.util.UserDataTypes;

public class CharacterData extends UserData {

	//The Character that owns this data
	public Schmuck schmuck;
	
	//Speed on ground
	public float maxSpeed = 10.0f;
	public float maxAngularSpeed = 5.0f;

	//Hp and regen
	public int maxHp = 100;
	public float currentHp = 100;
	public float hpRegen = 0.0f;
		
	/**
	 * This is created upon the create() method of any schmuck.
	 * Character are the Body data type.
	 * @param world
	 * @param schmuck
	 */
	public CharacterData(World world, Schmuck schmuck) {
		super(world, UserDataTypes.BODY, schmuck);
		this.schmuck = schmuck;		
	}	
	
	/**
	 * This method is called when this schmuck receives damage.
	 * @param basedamage: amount of damage received
	 * @param knockback: amount of knockback to apply.
	 *TODO: include the source of damage
	 */
	public void receiveDamage(float basedamage, Vector2 knockback) {
		currentHp -= basedamage;
		schmuck.getBody().applyLinearImpulse(knockback, schmuck.getBody().getLocalCenter(), true);
		if (currentHp <= 0) {
			currentHp = 0;
			die();
		}
	}
	
	/**
	 * This method is called when the schmuck is healed
	 * @param heal: amount of Hp to regenerate
	 */
	public void regainHp(float heal) {
		currentHp += heal;
		if (currentHp >= maxHp) {
			currentHp = maxHp;
		}
	}
	
	/**
	 * This method is called when the schmuck dies. Queue up to be deleted next engine tick.
	 */
	public void die() {
		schmuck.queueDeletion();
	}

	public Schmuck getSchmuck() {
		return schmuck;
	}
}
