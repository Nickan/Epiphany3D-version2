package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.nickan.epiphany.Epiphany;

public class GameScreen implements Screen {
	World world;
	WorldRenderer worldRenderer;
	WorldController worldController;

	AllGameButtonsView allButtons;
	InputMultiplexer inputPlexer;
	
	public GameScreen(Epiphany game) {
		world = new World(game);
		worldRenderer = new WorldRenderer(world);
		worldController = new WorldController(worldRenderer);
		
		allButtons = new AllGameButtonsView(world);
		
		// Set the default view of the camera
		world.incCamRotation(15, 0, 0);
	}

	@Override
	public void render(float delta) {
		world.update(delta);
		worldRenderer.render(delta);

		allButtons.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		world.resize(width, height);
		worldRenderer.resize(width, height);

		allButtons.resize(width, height);
		inputPlexer = new InputMultiplexer(worldController, allButtons.stage);
		Gdx.input.setInputProcessor(inputPlexer);
	}

	@Override
	public void show() {
		worldRenderer.show();
		allButtons.show();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		worldRenderer.dispose();
	}

}
