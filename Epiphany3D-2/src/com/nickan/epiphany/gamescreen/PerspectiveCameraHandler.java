package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.math.Euler;

/**
 * Has basic camera operations, for now chasing the position given
 * @author Nickan
 *
 */
public class PerspectiveCameraHandler {
	PerspectiveCamera cam;
	
	Vector3 rotation;
	
	public PerspectiveCameraHandler(PerspectiveCamera cam) {
		this.cam = cam;
		rotation = new Vector3();
	}
	
	public void update(Vector3 position, float delta) {
		cam.position.set(position);
		
		cam.update();
	}
	
	public void setViewRotation(float x, float y, float z) {
		rotation.set(x, y, z);
		
		// Set the rotation, normalize and reverse the direction
		cam.direction.set(Euler.toAxes(rotation)).nor().scl(-1);
	}
}
