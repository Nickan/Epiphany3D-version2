package com.nickan.epiphany.screens.gamescreen;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.nickan.epiphany.Epiphany;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.MessageDispatcher;
import com.nickan.epiphany.framework.math.Dimension3D;
import com.nickan.epiphany.framework.math.Euler;
import com.nickan.epiphany.model.Player;
import com.nickan.epiphany.model.Zombie;
import com.nickan.epiphany.model.characterstate.IdleState;

public class World {
	Epiphany game;
	
	Player player;
	Array<Zombie> zombies;
	
	/** Camera rotation in degrees */
	private Vector3 camRotation;
	/** Normalized vector for camera */
	private Vector3 camDirection;
	
	/** For stopping rotating the camera*/
	boolean stopCameraRotation;
	
	enum State {PAUSE, GAME};
	State currentState = State.GAME;
	
	public World() {
		camRotation = new Vector3();
		camDirection = new Vector3();
		stopCameraRotation = false;
		
		zombies = new Array<Zombie>();
		zombies.add(new Zombie(new Vector3(0, 0, 1), new Dimension3D(0.75f, 1, 1), 3.0f, 0.5f) );
		zombies.add(new Zombie(new Vector3(1, 0, 1), new Dimension3D(0.75f, 1, 1), 3.0f, 0.5f) );
		zombies.add(new Zombie(new Vector3(2, 0, 1), new Dimension3D(0.75f, 1, 1), 3.0f, 0.5f) );
		zombies.add(new Zombie(new Vector3(3, 0, 1), new Dimension3D(0.75f, 1, 1), 3.0f, 0.5f) );
		zombies.add(new Zombie(new Vector3(4, 0, 1), new Dimension3D(0.75f, 1, 1), 3.0f, 0.5f) );
		
		player = new Player(new Vector3(5, 0, 5), new Dimension3D(0.75f, 1, 1), 3.0f, 1.0f);
		player.setCamForwardVector(camDirection);
	}
	
	public void update(float delta) {
		player.update(delta);
		
		for (Zombie zombie: zombies) {
			zombie.update(delta);
			
			// Zombie doesn't have any target
			if (zombie.getTargetId() == -1) {
				// The player is in range
				if (zombie.isInRange(player.getCenter(), zombie.getSightRange())) {
					MessageDispatcher.sendMessage(player.getId(), zombie.getId(), 0, 
							MessageType.IS_IN_RANGE, player.getBoundBox());
				}
			} else {
				
				// The zombie has a target but has gets out so far away from its sight range
				// If the player gets so far away from its sight range
				if (!zombie.isInRange(player.getCenter(), zombie.getSightRange() + 2)) {
					zombie.charChangeState(IdleState.getInstance());
				}
			}
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
			if (player.isInRange(zombie.getCenter(), player.getSightRange())) {
				MessageDispatcher.sendMessage(zombie.getId(), player.getId(), 0, 
						MessageType.IS_IN_RANGE, zombie.getBoundBox());
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
