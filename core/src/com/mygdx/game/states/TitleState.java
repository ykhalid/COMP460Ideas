package com.mygdx.game.states;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.manager.GameStateManager.State;
import com.mygdx.game.networking.ChatClient;
import com.mygdx.game.networking.ChatServer;
import com.mygdx.game.comp460game;
import com.mygdx.game.actors.Text;

public class TitleState extends GameState {

	private Stage stage;
	
	//Temporary links to other modules for testing.
	private Actor playOption, exitOption, joinServerOption, startServerOption;
	
	public TitleState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void show() {
		stage = new Stage() {
			{
				playOption = new Text(comp460game.assetManager, "PLAY?", 150, comp460game.CONFIG_HEIGHT - 180);
				startServerOption = new Text(comp460game.assetManager, "START SERVER?", 150, comp460game.CONFIG_HEIGHT - 240);
				joinServerOption = new Text(comp460game.assetManager, "JOIN SERVER?", 150, comp460game.CONFIG_HEIGHT - 300);
				exitOption = new Text(comp460game.assetManager, "EXIT?", 150, comp460game.CONFIG_HEIGHT - 360);
				
				playOption.addListener(new ClickListener() {
			        public void clicked(InputEvent e, float x, float y) {
			        	gsm.addState(State.PLAY, TitleState.class);
			        }
			    });
				playOption.setScale(0.5f);
				
				joinServerOption.addListener(new ClickListener() {
			        public void clicked(InputEvent e, float x, float y) {
			        	new ChatClient();
			        }
			    });
				joinServerOption.setScale(0.5f);
				
				startServerOption.addListener(new ClickListener() {
			        public void clicked(InputEvent e, float x, float y) {
			        	//TODO: start a server
			        	try {
							new ChatServer();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
			        }
			    });
				startServerOption.setScale(0.5f);
				
				exitOption.addListener(new ClickListener() {
			        public void clicked(InputEvent e, float x, float y) {
			        	Gdx.app.exit();
			        }
			    });
				exitOption.setScale(0.5f);
				
				addActor(playOption);
				addActor(startServerOption);
				addActor(joinServerOption);
				addActor(exitOption);
			}
		};
		app.newMenu(stage);
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();	
	}

}
