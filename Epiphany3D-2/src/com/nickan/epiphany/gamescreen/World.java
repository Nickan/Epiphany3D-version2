package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.Epiphany;
import com.nickan.epiphany.framework.math.Euler;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.Dimension3D;

public class World {
	Character player = new Character(new Vector3(5, 0, 5), new Dimension3D(), 0.01f);
	Character enemy = new Character(new Vector3(10, 0, 0), new Dimension3D(), 1.0f);
	
	/** Camera rotation in degrees */
	private Vector3 camRotation;
	/** Normalized vector for camera */
	private Vector3 camDirection;
	
	public World(Epiphany game) {
		camRotation = new Vector3();
		camDirection = new Vector3();
	}
	
	public void update(float delta) {
		player.update(delta);
	}
	
	public void resize(int width, int height) {
		
	}
	
	/**
	 * Sets up the camera's rotation
	 * @param incX
	 * @param incY
	 * @param incZ
	 */
	public void incCamRotation(float incX, float incY, float incZ) {
		camRotation.x += incY;
		camRotation.y += incX;
		camRotation.z += incZ;
		
		// Limit the rotation that can be done
		if (camRotation.x < 15)
			camRotation.x = 15;
		if (camRotation.x > 80)
			camRotation.x = 80;
		if (camRotation.y < 0)
			camRotation.y = 359;
		if (camRotation.y > 359)
			camRotation.y = 0;
		
		// Set the direction the reverse of the camRotation
		// Need to save the rotation, as it is very costly to make it a normalized direction vector
		camDirection.set(Euler.toAxes(camRotation)).nor().scl(-1);
		
		player.setForwardVector(camDirection);
	}
	
	public Vector3 getCamDirection() {
		return camDirection;
	}
	
}
