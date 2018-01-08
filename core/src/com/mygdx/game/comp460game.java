package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.manager.GameStateManager;

public class comp460game extends ApplicationAdapter {
	

	//The main camera scales to the viewport size scaled to this. Useful for zoom-in/out testing.
	//TODO: replace this with a constant aspect ratio?
	private final float SCALE = 1.0f;
		
	
	//Camera and Spritebatch. This is pretty standard stuff.
	private OrthographicCamera camera, hud;
	private SpriteBatch batch;

	//This is the Gamestate Manager. It manages the current game state.
	private GameStateManager gsm;
		
    public static FitViewport viewport;

	private static final int DEFAULT_WIDTH = 1080;
	private static final int DEFAULT_HEIGHT = 720;
	public static int CONFIG_WIDTH;
	public static int CONFIG_HEIGHT;
	
	/**
	 * This creates a game, setting up the sprite batch to render things and the main game camera.
	 * This also initializes the Gamestate Manager.
	 */
	@Override
	public void create () {
		CONFIG_WIDTH = DEFAULT_WIDTH;
		CONFIG_HEIGHT = DEFAULT_HEIGHT;

		batch = new SpriteBatch();

		camera = new OrthographicCamera(CONFIG_WIDTH * SCALE, CONFIG_HEIGHT * SCALE);
		camera.setToOrtho(false, CONFIG_WIDTH * SCALE, CONFIG_HEIGHT * SCALE);
		
		hud = new OrthographicCamera(CONFIG_WIDTH, CONFIG_HEIGHT);
	    hud.setToOrtho(false, CONFIG_WIDTH, CONFIG_HEIGHT);
		
		viewport = new FitViewport(CONFIG_WIDTH * SCALE, CONFIG_HEIGHT * SCALE, camera);
	    viewport.apply();
		
		gsm = new GameStateManager(this);	
	}

	/**
	 * This is run every engine tick according to libgdx.
	 * Here, we tell the gsm to tell the current state of the elapsed time.
	 */
	@Override
	public void render() {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		
		//TODO: Tentatively pressing esc exits the game. Will replace later with an actual menu.
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) { Gdx.app.exit(); }
	}
	
	/**
	 * Run when the window resizes. We tell the gsm to delegate that update to the current state.
	 */
	@Override
	public void resize (int width, int height) { 
		viewport.update((int)(width * SCALE), (int)(height * SCALE), true);
//		camera.setToOrtho(false, width, height);
//		camera.update();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		gsm.resize((int)(width * SCALE), (int)(height * SCALE));
		viewport.apply();
		
		CONFIG_WIDTH = width;
		CONFIG_HEIGHT = height;
	}
	
	/**
	 * Upon deleting, this is run. Again, we delegate to the gsm.
	 * We also need to dispose of anything else here. (such as the batch)
	 */
	@Override
	public void dispose () {
		gsm.dispose();
		batch.dispose();
	}
	
	/**
	 * Getter for the main game camera
	 * @return: the camera
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	/**
	 * Getter for the hud camera
	 * @return: the camera
	 */
	public OrthographicCamera getHud() {
		return hud;
	}
	
	/**
	 * Getter for the main game sprite batch
	 * @return: the batch
	 */
	public SpriteBatch getBatch() {
		return batch;
	}
}
