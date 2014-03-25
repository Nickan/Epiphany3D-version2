package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Has basic camera operations, for now chasing the position given
 * @author Nickan
 *
 */
public class PerspectiveCameraHandler {
	PerspectiveCamera cam;
	
	/** Determines how far away the camera from the basePosition */
	private float zoomScale = 5.0f;

	private static final Vector3 tempVector = new Vector3();
	
	public PerspectiveCameraHandler(PerspectiveCamera cam) {
		this.cam = cam;
	}
	
	public void update(Vector3 basePosition, Vector3 direction, float delta) {
		cam.direction.set(direction);
		setPosition(basePosition, delta);
		cam.update();
	}
	
	private void setPosition(Vector3 basePosition, float delta) {
		tempVector.set(cam.direction);
		
		// Reverse the direction
		tempVector.scl(-1);
		tempVector.scl(zoomScale);
		
		// Add the base position
		tempVector.add(basePosition);
		
		// Move up the camera a bit
		tempVector.y += 1.0f;
		cam.position.set(tempVector);
	}
}
