package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.InputProcessor;

/**
 * Controls the World, but in constructor, only the WorldRenderer is passed, as the WorldRenderer has the copy
 * of World instance
 * @author nickan
 *
 */
public class WorldController implements InputProcessor {
	World world;
	WorldRenderer worldRenderer;
	
	public WorldController(WorldRenderer worldRenderer) {
		this.worldRenderer = worldRenderer;
		this.world = worldRenderer.world;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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
