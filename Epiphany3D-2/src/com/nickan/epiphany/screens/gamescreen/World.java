package com.nickan.epiphany.screens.gamescreen;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.nickan.epiphany.Epiphany;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.MessageDispatcher;
import com.nickan.epiphany.framework.math.Euler;
import com.nickan.epiphany.framework.pathfinder.AStarPathFinder;
import com.nickan.epiphany.framework.pathfinder.Node.NodeType;
import com.nickan.epiphany.model.Zombie;
import com.nickan.epiphany.model.Character;

public class World {
	Epiphany game;
	
	Character player;
	Array<Zombie> zombies;
	
	/** Camera rotation in degrees */
	private Vector3 camRotation;
	/** Normalized vector for camera */
	private Vector3 camDirection;
	
	/** For stopping rotating the camera*/
	boolean stopCameraRotation;
	
	enum State {PAUSE, GAME};
	State currentState = State.GAME;
	
	public boolean stopUpdate = false;
	
	// Path finder is not needed for now, but maybe later...
	private static final int PATHFINDER_WIDTH = 50;
	private static final int PATHFINDER_HEIGHT = 50;
	public AStarPathFinder pathFinder = new AStarPathFinder(PATHFINDER_WIDTH, PATHFINDER_HEIGHT);
	
	public static final float WORLD_UNIT_WIDTH = 0.5F;
	public static final float WORLD_UNIT_DEPTH = 0.5F;
	
	public World() {
		camRotation = new Vector3();
		camDirection = new Vector3();
		stopCameraRotation = false;
		
		float mass = 1.0f;
		float maxForce = 2.0f;
		float maxTurnRate = 0.05f;
		float maxSpeed = 0.1f;
		
		player = new Character(new Vector3(6, 0, 1), new Vector3(0.5f, 1.0f, 0.5f), new Vector3(0, 0, 0), 
				mass, maxForce, maxTurnRate, maxSpeed, 3.0f);
		player.setCamForwardVector(camDirection);
		
		zombies = new Array<Zombie>();
		zombies.add(newZombie(new Vector3(1f, 0, 1.1f), new Vector3(0.5f, 1, 0.5f), new Vector3(0, 0, 0), 0.5f, 10f) );
		zombies.add(newZombie(new Vector3(2.5f, 0, 1), new Vector3(0.5f, 1, 0.5f), new Vector3(0, 0, 0), 0.5f, 10f) );
		zombies.add(newZombie(new Vector3(3.5f, 0, 1.5f), new Vector3(0.5f, 1, 0.5f), new Vector3(0, 0, 0), 0.5f, 10f) );
		zombies.add(newZombie(new Vector3(4.5f, 0, 2), new Vector3(0.5f, 1, 0.5f), new Vector3(0, 0, 0), 0.5f, 10f) );
		
		MessageDispatcher.initialize();
	}

	private Zombie newZombie(Vector3 pos, Vector3 dim, Vector3 rot, float speed, float sightRange) {
		float mass = 1.0f;
		float maxForce = 2.0f;
		float maxTurnRate = 0.05f;
		float maxSpeed = 0.01f;
		
		Zombie newZom = new Zombie(pos, dim, rot, mass, maxForce, maxTurnRate, maxSpeed, sightRange);
		newZom.setTargetId(player.getId());
		newZom.world = this;
		return newZom;
	}
	
	public void update(float delta) {
		if (stopUpdate) {
			return;
		}
//		stopUpdate = true;
		updateEntitiesOccupiedNodes();
		player.update(delta);
		
//		zombies.get(0).update(delta);
		for (Zombie zombie: zombies) {
			zombie.update(delta);
		}
		
		MessageDispatcher.update(delta);
	}
	
	/**
	 * Updates the entities' occupied nodes for the path finder based on their
	 */
	private void updateEntitiesOccupiedNodes() {
		pathFinder.setAllNodesWalkable();
		
		int nodeX = (int) (player.getPosition().x / WORLD_UNIT_WIDTH);
		int nodeZ = (int) (player.getPosition().z / WORLD_UNIT_DEPTH);
		player.setCurrentNode(nodeX, nodeZ);
		
		for (Zombie zombie: zombies) {
			Vector3 zomPos = zombie.getPosition();
			
			// Every half unit is a node
			nodeX = (int) (zomPos.x / WORLD_UNIT_WIDTH);
			nodeZ = (int) (zomPos.z / WORLD_UNIT_DEPTH);

			pathFinder.setNodeType(NodeType.UNWALKABLE, nodeX, nodeZ);
			zombie.setCurrentNode(nodeX, nodeZ);
			//...
//			System.out.println("Occupied node: " + nodeX + ": " + nodeZ);
		}
		
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
			if (player.isInRange(zombie.getBoundingBox().getCenter(), player.getSightRange())) {
				MessageDispatcher.sendMessage(zombie.getId(), player.getId(), 0, 
						MessageType.IS_IN_RANGE, zombie.getBoundingBox());
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
	
	// For the steering behavior implementation
	public Array<Zombie> getZombies() { return zombies; }
	
	public Character getPlayer() { return player; }
	
	
}
