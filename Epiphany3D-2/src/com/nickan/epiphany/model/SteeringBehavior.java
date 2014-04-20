package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.nickan.epiphany.framework.math.OrientedBoundingBox;
import com.nickan.epiphany.framework.pathfinder.AStarPathFinder;
import com.nickan.epiphany.framework.pathfinder.Node;
import com.nickan.epiphany.screens.gamescreen.World;

public class SteeringBehavior {
	Character character;
	Character targetChar = null;
	
	Vector3 steeringForce;
	Vector3 targetPos = new Vector3();
	Vector3 seekingPos = new Vector3();
	
	//...
	Vector3 force = new Vector3();
	
	/* 
	 * For the obstacle avoidance, if the result should turn back from the seek target,
	 * it should track the result target for a set amount of distance then seek the
	 * target again
	 */
	private boolean hasDetectedObstacle = false;
	
	private static final Vector3 tmpVec1 = new Vector3();
	private static final Vector3 tmpVec2 = new Vector3();
	
	private int behavior;
	
	private enum Behavior_Type {
		NONE(0x000000),
		SEEK(0X000002),
		OBSTACLE_AVOIDANCE(0x000100);
		
		
		private int flag;
		
		Behavior_Type(int flag) {
			this.flag = flag;
		}
		
		public int flag() {
			return this.flag;
		}
	};
	
	Array<Node> pathList = new Array<Node>();
	
	public SteeringBehavior(Character character) {
		this.character = character;
		steeringForce = new Vector3();
	}
	
	/**
	 * 
	 * @return The steering force to be used by the moving entity
	 */
	public Vector3 calcSteeringForce() {
		steeringForce.set(Vector3.Zero);
		// The simple obstacle avoidance and seek is working fine with zombies,
		// so I will fix and implement this later if needed
		//handlePathfinding();
		
		if (on(Behavior_Type.OBSTACLE_AVOIDANCE)) {
			force.set(obstacleAvoidance(character.world.getZombies()));
			
			if (!accumulateForce(steeringForce, force)) {
				return steeringForce; 
			}
		}
		
		if (on(Behavior_Type.SEEK)) {
			force.set(seek(seekingPos));
			if (!accumulateForce(steeringForce, force)) return steeringForce;
		}

		return steeringForce;
	}
	
	/**
	 * Handles the pathfinding ability of the character
	 */
	private void handlePathfinding() {
		if (hasDetectedObstacle) {
			hasDetectedObstacle = false;
			handleDetectedObstacle();
		}
		
		// If there is no path in the list, set the target position as the seeking position
		if (pathList.size == 0) {
			seekingPos.set(targetPos);
		} else {
			// If the character has reached the current target path list
			float charRadius = character.getBoundingBox().getDimension().x / 2;
			tmpVec1.set(character.getBoundingBox().getCenter()).y = 0;
			tmpVec1.sub(seekingPos);
			if (tmpVec1.len2() < charRadius * charRadius) {
				assignNextSeekNode();
				//...
				System.out.println("Assign next node");
			} else {
				//...
				System.out.println("Not in range");
			}
		}
	}
	
	private void handleDetectedObstacle() {
		// Check if the result of the force created by the obstacleAvoidance() makes
		// the velocity of the character heads opposite direction to the target
		// position
		float dot = getDotProduct(character.getVelocity(), targetPos);
		// To know the view of the character
		if (dot < .25f) {
			pathFindTarget();
		}
		//...
		System.out.println("Handle detected obstacle");
	}
	
	private void pathFindTarget() {
		AStarPathFinder pathFinder = character.world.pathFinder;

		pathList.clear();

		// Path find the target's node, if the target character is null,
		// then the set target position
		if (targetChar != null) {
			pathList.addAll(pathFinder.getShortestPath(
					character.getCurrentNode(), targetChar.getCurrentNode()));
		} else {
			Node targetNode = new Node(
					(int) (targetPos.x / World.WORLD_UNIT_WIDTH),
					(int) (targetPos.z / World.WORLD_UNIT_DEPTH));
			pathList.addAll(pathFinder.getShortestPath(
					character.getCurrentNode(), targetNode));
			
			//...
			System.out.println("Target node: " + targetNode);
		}
		
		//...
		for (int index = 0; index < pathList.size; ++index) {
			System.out.println("List: " + pathList.get(index));
		}
		
		assignNextSeekNode();
		
	}
	
	private void assignNextSeekNode() {
		if (pathList.size != 0) {
			Node targetNode = pathList.pop();
			seekingPos.set( (targetNode.x * World.WORLD_UNIT_WIDTH) + World.WORLD_UNIT_WIDTH / 2f, 0, 
					(targetNode.y * World.WORLD_UNIT_DEPTH) + World.WORLD_UNIT_DEPTH / 2f);
			
			//...
			System.out.println("Seeking node: " + seekingPos);
		}
	}
	
	private Vector3 seek(Vector3 targetVec) {
		tmpVec1.set(Vector3.Zero);

		// Setting up the desired velocity
		tmpVec1.set(targetVec).sub(character.getPosition()).scl(character.getMaxSpeed());
				
		tmpVec1.sub(character.getVelocity());

		return tmpVec1;
	}
	
	/**
	 * Returns the dot product of the two vectors without changing their values
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	private float getDotProduct(Vector3 vec1, Vector3 vec2) {
		tmpVec1.set(vec1).nor();
		tmpVec2.set(vec2).nor();
		return tmpVec1.dot(tmpVec2);
	}
	
	private Vector3 obstacleAvoidance(Array<Zombie> zombies) {
		Zombie nearestZom = getNearestZombie(zombies);

		steeringForce.set(Vector3.Zero);
		if (nearestZom != null) {
			// The basis of the steering force avoidance is to determine the distance of the nearest
			// obstacle center in local space of this entity
			
			// Should analyze the center of the bounds, not the position of the entity
			OrientedBoundingBox charBound = character.getBoundingBox();
			Matrix4 charInvTransform = charBound.getInverseTransform();
			Vector3 localNearestZomTransform = new Vector3(nearestZom.getBoundingBox().getCenter());
			localNearestZomTransform.mul(charInvTransform);
			
			//...
//			System.out.println("Local zombie transform: " + localNearestZomTransform);
			
			//...
//			System.out.println("Local transform: " + localNearestZomTransform);
			float multiplier = charBound.getDimension().z - Math.abs(localNearestZomTransform.x) * 
					charBound.getDimension().z;
			
			Vector3 zomDim = nearestZom.getBoundingBox().getDimension();
			float zomRadius = new Vector2(zomDim.x, zomDim.z).len();
			steeringForce.x = (zomRadius - Math.abs(localNearestZomTransform.x)) * multiplier;
			
			// To correct the polarity for applying the steering force about x-axis
			if (localNearestZomTransform.x > 0) {
				steeringForce.x *= -1;
			}
			
			//...
//			System.out.println("Radius: " + zomRadius);
//			System.out.println("Multiplier x: " + multiplier);
			
			
			float breakingForce = 0.1f;
			steeringForce.z = (zomRadius - Math.abs(localNearestZomTransform.z)) * 
					breakingForce;
//			System.out.println("steeringForce: " + steeringForce);
			
			// Add the force based on the character's position
//			steeringForce.add(character.getPosition());
//			steeringForce.y = 0;	// Remove the movement about y-axis for now
			
			Matrix4 toWorld = new Matrix4(character.getBoundingBox().transform);
			steeringForce.mul(toWorld);
			steeringForce.y = 0;	// Remove the movement about y-axis for now
			steeringForce.sub(character.getPosition());
			
			//...
//			System.out.println("Final steeringForce: " + steeringForce);
//			System.out.println("");
			
			hasDetectedObstacle = true;
			
			//...
//			System.out.println("Obstacle detected");
		}
		return steeringForce;
	}
	
	private Zombie getNearestZombie(Array<Zombie> zombies) {
		// FIXME implements the sphere collision detection
		boolean useSphere = false;
		
		float nearestObstacleDistSqr = Float.MAX_VALUE;
		Vector3 localCoorCrn = new Vector3();
		Zombie nearestZom = null;
		
		// Can't use enhanced for loop, iterator exception
		for (int index = 0; index < zombies.size; ++index) {
			Zombie zombie = zombies.get(index);

			// Don't include itself and the target
			if (character == zombie || targetChar == zombie) {
				continue;
			}

			// FIXME posing a potential problem when the length of the detector box
			// goes way too long
			
			// Limits the checking of the bounds by the view range
			float viewRange = 3.0f;
			if (character.isInRange(zombie.getPosition(), viewRange)) {

				// Set the depth(or length) of the collision detector box
				float speed = character.getVelocity().len();
				float detectorDefaultLength = 0.5f;
				float detectorLength = (speed / character.getMaxSpeed())
						* detectorDefaultLength;
				character.detector.setCollisionDepth(detectorLength);

				// ...
				// System.out.println("Detector length: " + detectorLength);
				// System.out.println("Velocity: " + character.getVelocity());

				// If the zombie's bounds collides
				// Get the coordinates of the collided corner
				tmpVec1.set(OrientedBoundingBox.getCollidedCorner(
						character.getBoxDetector(), zombie.getBoundingBox()));
				// Check if the coordinates is valid
				if (tmpVec1.len2() < nearestObstacleDistSqr) {
					localCoorCrn.set(tmpVec1);
					nearestObstacleDistSqr = tmpVec1.len2();
					nearestZom = zombie;
				}

			}
		}
		
		return nearestZom;
	}
	
	
	/**
	 * Returns if there is accumulated force and at the same time, manipulates the value of the
	 * running total depending on the current running total steering force
	 * @param runningTotal
	 * @param forceToAdd
	 * @return
	 */
	private boolean accumulateForce(Vector3 runningTotal, Vector3 forceToAdd) {
		float currentMagnitude = runningTotal.len();
		float magnitudeRemaining = character.getMaxForce() - currentMagnitude;
		
		// Returns if the maximum force has been reached
		if (magnitudeRemaining <= 0.0f) {
			return false;
		}
		
		float magnitudeToAdd = forceToAdd.len();
		// If the maximum force has been filled yet
		if (magnitudeToAdd < magnitudeRemaining) {
			runningTotal.add(forceToAdd);
		} else {
			// Fill in the remaining force to be added
			runningTotal.add(forceToAdd.nor().scl(magnitudeRemaining));
		}
		return true;
	}

	public Vector3 getTargetPos() { return targetPos; }
	public void setTargetPos(Vector3 tarPos) {
		targetPos.set(tarPos).y = 0;	// No elevation for now
		
		//...
		seekingPos.set(targetPos);
	}
	
	public void setTargetChar(Character targetChar) { 
		this.targetChar = targetChar;
		setTargetPos(targetChar.getBoundingBox().getCenter());
	}
	public Character getTargetChar() { return targetChar; }
	
	public boolean on(Behavior_Type type) { return (behavior & type.flag()) == type.flag(); }
	
	public void resetBehavior() { behavior = Behavior_Type.NONE.flag(); }
	
	public void seekOn() { behavior |= Behavior_Type.SEEK.flag(); }
	public void seekOff() {
		if (on(Behavior_Type.SEEK)) {
			behavior ^= Behavior_Type.SEEK.flag(); 
		}
	}
	
	public void avoidanceOn() { behavior |= Behavior_Type.OBSTACLE_AVOIDANCE.flag(); }
	public void avoidanceOff() {
		if (on(Behavior_Type.OBSTACLE_AVOIDANCE)) {
			behavior ^= Behavior_Type.OBSTACLE_AVOIDANCE.flag();
		}
	}
	
}
