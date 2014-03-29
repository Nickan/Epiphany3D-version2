package com.nickan.epiphany.screens.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nickan.epiphany.Epiphany;

public class GameScreen implements Screen {
	Epiphany game;
	World world;
	WorldRenderer worldRenderer;
	WorldController worldController;
	HeadsUpDisplay hud;

	AllGameButtonsView allButtons;
	InputMultiplexer inputPlexer;
	
	public GameScreen(Epiphany game) {
		this.game = game;
		world = new World();
		worldRenderer = new WorldRenderer(world);
		worldController = new WorldController(worldRenderer);
		
		allButtons = new AllGameButtonsView(world, this);
		hud = new HeadsUpDisplay();
		hud.allButtons = allButtons;
		
		// Set the default view of the camera
		world.incCamRotation(15, 0, 0);
	}

	@Override
	public void render(float delta) {
		switch (world.currentState) {
		case GAME:
			world.update(delta);
			worldRenderer.render(delta);
			allButtons.render(delta);
			break;
		case PAUSE:
			SpriteBatch batch = (SpriteBatch) allButtons.stage.getSpriteBatch();
			hud.drawPauseBackground(batch);
			allButtons.render(delta);
			hud.drawPauseHud(batch, world.player);
			break;
		default:
			break;
		}

		
	}

	@Override
	public void resize(int width, int height) {
		world.resize(width, height);
		worldRenderer.resize(width, height);

		allButtons.resize(width, height);
		hud.resize(width, height);
	}
	
	void setGameController() {
		inputPlexer = new InputMultiplexer(worldController, allButtons.stage);
		Gdx.input.setInputProcessor(inputPlexer);
	}
	
	void setPauseController() {
		Gdx.input.setInputProcessor(allButtons.stage);
	}
	
	public SpriteBatch getSpriteBatch() {
		return (SpriteBatch) allButtons.stage.getSpriteBatch();
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
