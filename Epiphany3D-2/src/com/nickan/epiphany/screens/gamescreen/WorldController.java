package com.nickan.epiphany.screens.gamescreen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

/**
 * Controls the World, but in constructor, only the WorldRenderer is passed, as the WorldRenderer has the copy
 * of World instance
 * @author Nickan
 *
 */
public class WorldController implements InputProcessor {
	World world;
	WorldRenderer worldRenderer;
	
	/** Will be used for the amount of dragged length */
	Vector2 previousTouch;
	
	public WorldController(WorldRenderer worldRenderer) {
		this.worldRenderer = worldRenderer;
		this.world = worldRenderer.world;
		
		previousTouch = new Vector2();
	}

	@Override
	public boolean keyDown(int keycode) {
		
		switch (keycode) {
		case Keys.W:
			world.stopUpdate = false;
			break;
		case Keys.S: 
			break;
		case Keys.A: 
			break;
		case Keys.D: 
			worldRenderer.setDebugging(!worldRenderer.isDebugging());
			break;
		}
		

		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		/*
		world.player.setMovement(Movement.STOP);
		*/
		switch (keycode) {
		case Keys.W:
		//	world.stopUpdate = false;
			break;
		case Keys.S:
			for (int index = 0; index < 5; ++index) {
				System.out.println("");
			}
			break;
		case Keys.A: 
			break;
		case Keys.D: 
			break;
		}
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		previousTouch.set(screenX, screenY);
		
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float scrolledX = screenX - previousTouch.x;
		float scrolledY = screenY - previousTouch.y;
		previousTouch.set(screenX, screenY);
		
		if (!world.stopCameraRotation) {
			world.incCamRotation(scrolledX, scrolledY, 0);
		}
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		worldRenderer.camHandler.zoomScale += amount * 3 * Gdx.graphics.getDeltaTime();
		return false;
	}
	
}
