package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.nickan.epiphany.model.MoveableEntity.Movement;

/**
 * Controls the World, but in constructor, only the WorldRenderer is passed, as the WorldRenderer has the copy
 * of World instance
 * @author nickan
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
		case Keys.W: world.player.setMovement(Movement.FORWARD);
			break;
		case Keys.S: world.player.setMovement(Movement.BACKWARD);
			break;
		case Keys.A: world.player.setMovement(Movement.LEFT);
			break;
		case Keys.D: world.player.setMovement(Movement.RIGHT);
			break;
		}
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		world.player.setMovement(Movement.STOP);
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		previousTouch.set(screenX, screenY);
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float scrolledX = screenX - previousTouch.x;
		float scrolledY = screenY - previousTouch.y;
		previousTouch.set(screenX, screenY);
		
		world.incCamRotation(scrolledX, scrolledY, 0);
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
