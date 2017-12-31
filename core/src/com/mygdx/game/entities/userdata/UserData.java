package com.mygdx.game.entities.userdata;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.Entity;
import com.mygdx.game.util.UserDataTypes;

/**
 * This is the basic user data of any entity. The only thing this needs to do is keep track of type and basic stuff like that.
 * @author Zachary Tu
 *
 */
public class UserData {

	//the number of other entities or walls this entity is touching. Atm only used for feet to determine groundedness
	private int numContacts;
	
	//Enum that describes the type of entity. This field is often checked on contact.
	private UserDataTypes type;
	
	//The entity that owns this data
	private Entity entity;
	
	/**
	 * aye
	 * @param world
	 * @param type
	 * @param entity
	 */
	public UserData(World world, UserDataTypes type, Entity entity) {
		this.type = type;
		this.entity = entity;
		this.numContacts = 0;
	}

	public int getNumContacts() {
		return numContacts;
	}

	public void setNumContacts(int numContacts) {
		this.numContacts = numContacts;
	}

	public UserDataTypes getType() {
		return type;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
}