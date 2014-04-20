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
	
	private static final Vector3 tmpVec = new Vector3();
	private static final Vector3 tmpVec2 = new Vector3();
	
	public World() {
		camRotation = new Vector3();
		camDirection = new Vector3();
		stopCameraRotation = false;
		
		float mass = 1.0f;
		float maxForce = 2.0f;
		float maxTurnRate = 0.05f;
		float maxSpeed = 0.1f;
		float playerSightRange = 7.0f;
		
		player = new Character(new Vector3(6, 0, 1), new Vector3(0.5f, 1.0f, 0.5f), new Vector3(0, 0, 0), 
				mass, maxForce, maxTurnRate, maxSpeed, playerSightRange);
		player.setCamForwardVector(camDirection);
		player.world = this;
		
		float zombieSightRange = 5.0f;
		zombies = new Array<Zombie>();
		zombies.add(newZombie(new Vector3(1f, 0, 1.1f), new Vector3(0.5f, 1, 0.5f), 
				new Vector3(0, 0, 0), 0.5f, zombieSightRange) );
		zombies.add(newZombie(new Vector3(2.5f, 0, 1), new Vector3(0.5f, 1, 0.5f), 
				new Vector3(0, 0, 0), 0.5f, zombieSightRange) );
		zombies.add(newZombie(new Vector3(3.5f, 0, 1.5f), new Vector3(0.5f, 1, 0.5f), 
				new Vector3(0, 0, 0), 0.5f, zombieSightRange) );
		zombies.add(newZombie(new Vector3(4.5f, 0, 2), new Vector3(0.5f, 1, 0.5f), 
				new Vector3(0, 0, 0), 0.5f, zombieSightRange) );
		
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
		
		// Don't set a node if it is negative, as it will cause the path finder
		// to crash
		if (nodeX < 0 || nodeZ < 0) {
			return;
		}
		
		player.setCurrentNode(nodeX, nodeZ);
		
		for (Zombie zombie: zombies) {
			Vector3 zomPos = zombie.getPosition();
			
			// Every half unit is a node
			nodeX = (int) (zomPos.x / WORLD_UNIT_WIDTH);
			nodeZ = (int) (zomPos.z / WORLD_UNIT_DEPTH);
			
			// Don't set a node if it is negative, as it will cause the path finder to crash
			if (nodeX < 0 || nodeZ < 0) {
				return;
			}

			pathFinder.setNodeType(NodeType.UNWALKABLE, nodeX, nodeZ);
			zombie.setCurrentNode(nodeX, nodeZ);
			//...
//			System.out.println("Occupied node: " + nodeX + ": " + nodeZ);
		}
		
	}
	
	/**
	 * The player will seek the nearest zombie and attack it
	 */
	void playerSeekTarget() {
		float dist2 = Float.MAX_VALUE;
		Zombie nearestZom = null;
		for (Zombie zombie: zombies) {
			tmpVec.set(player.getBoundingBox().getCenter()).y = 0; // No elevation
			tmpVec2.set(zombie.getBoundingBox().getCenter()).y = 0;
			
			float tmpDist2 = Vector3.dst2(tmpVec.x, tmpVec.y, tmpVec.z, tmpVec2.x, tmpVec2.y, tmpVec2.z);
			// If the distance is in range
			if (tmpDist2 < player.getSightRange() * player.getSightRange()) {
				
				// If the distance is lower than the saved nearest distance
				if (tmpDist2 < dist2) {
					dist2 = tmpDist2;
					nearestZom = zombie;
				}
			}
			
		}
		
		if (nearestZom != null) {
			MessageDispatcher.sendMessage(nearestZom.getId(), player.getId(), 0, 
					MessageType.IS_IN_RANGE, nearestZom.getBoundingBox());
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
