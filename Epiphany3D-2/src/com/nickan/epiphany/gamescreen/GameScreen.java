package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.nickan.epiphany.Epiphany;

public class GameScreen implements Screen {
	World world;
	WorldRenderer worldRenderer;
	
	public GameScreen(Epiphany game) {
		world = new World(game);
		worldRenderer = new WorldRenderer(world);
		Gdx.input.setInputProcessor(new WorldController(worldRenderer));
	}

	@Override
	public void render(float delta) {
		world.update(delta);
		worldRenderer.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		world.resize(width, height);
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		worldRenderer.show();
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
