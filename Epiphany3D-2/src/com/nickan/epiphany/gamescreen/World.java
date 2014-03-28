package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.nickan.epiphany.Epiphany;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.MessageDispatcher;
import com.nickan.epiphany.framework.math.Dimension3D;
import com.nickan.epiphany.framework.math.Euler;
import com.nickan.epiphany.model.Player;
import com.nickan.epiphany.model.Zombie;

public class World {
	Player player;
	Array<Zombie> zombies;
	
	/** Camera rotation in degrees */
	private Vector3 camRotation;
	/** Normalized vector for camera */
	private Vector3 camDirection;
	
	/** For stopping rotating the camera*/
	boolean stopCameraRotation;
	
	public World(Epiphany game) { 
		camRotation = new Vector3();
		camDirection = new Vector3();
		stopCameraRotation = false;
		
		zombies = new Array<Zombie>();
		zombies.add(new Zombie(new Vector3(3, 0, 1), new Dimension3D(0.5f, 1, 1), 3.0f, 0.05f) );
		
		player = new Player(new Vector3(5, 0, 5), new Dimension3D(0.5f, 1, 1), 3.0f, 1.0f);
		player.setCamForwardVector(camDirection);
	}
	
	public void update(float delta) {
		player.update(delta);
		
		for (Zombie zombie: zombies) {
			zombie.update(delta);
		}
		
		MessageDispatcher.update(delta);
	}
	
	/**
	 * The player will seek target and attack it
	 */
	void playerSeekTarget() {
		// Don't search for a target if there is already
		if (player.getTargetId() != -1) {
			return;
		}
		
		for (Zombie zombie: zombies) {
			Vector3 center = zombie.getCenter();
			
			if (player.isInRange(center, player.getSightRange())) {
				MessageDispatcher.sendMessage(-10, player.getId(), 0, MessageType.PLAYER_ATTACK, zombie.getId());
			}
		}
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
	}
	
	public Vector3 getCamDirection() {
		return camDirection;
	}
	
}
