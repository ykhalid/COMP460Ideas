package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.userdata.PlayerData;
import com.mygdx.game.states.PlayState;

import box2dLight.RayHandler;

public class Player2Dummy extends Schmuck {

	Player player;
	
	public Player2Dummy(PlayState state, World world, OrthographicCamera camera, RayHandler rays, float w, float h,
			float startX, float startY, Player player) {
		super(state, world, camera, rays, w, h, startX, startY);
	this.player = player;
	}

	@Override
	public void create() {
		this.bodyData = player.getPlayerData();
		this.body = player.getBody();
	}
	
}
