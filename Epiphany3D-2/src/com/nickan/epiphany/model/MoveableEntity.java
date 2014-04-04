package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.math.Euler;
import com.nickan.epiphany.framework.math.OrientedBoundingBox;

/**
 * An entity class that knows everything about movement(I guess)
 * @author Nickan
 *
 */
public abstract class MoveableEntity extends Entity {
	float maxSpeed;
	float forwardSpeed;
	float sideSpeed;
	float sightRange;
	
	/** The view vector of the entity */
	Vector3 forwardVector;
	Vector3 currentForwardVector;
	
	/** Will be the basis forward when the forward key is pressed */
	Vector3 camForwardVector;
	
	/** 
	 * For the use of setting up temporary values for vectors that should not be overridden by
	 * Vector3 operations such as add, sub, mul, etc.
	 */
	protected static final Vector3 tempVec1 = new Vector3();
	protected static final Vector3 tempVec2 = new Vector3();
	
	public enum Movement { FORWARD, BACKWARD, LEFT, RIGHT, STOP };
	private Movement currentMovement = Movement.STOP;
	
	/** A movement set by the user */
	private Movement commandedMovement = Movement.STOP;
	
	private CollisionDetector detector;

	public MoveableEntity(Vector3 center, Vector3 dimension, Vector3 rotation, float sightRange, float maxSpeed) {
		super(center, dimension, rotation);
		this.sightRange = sightRange;
		this.maxSpeed = maxSpeed;
		
		// Default is viewing to the from
		this.forwardVector = new Vector3(1.0f, 0, 0);
		this.currentForwardVector = new Vector3();
		
		detector = new CollisionDetector(this);
	}

	protected void update(float delta) {
		// Set the velocity
		tempVec1.set(forwardVector);
		tempVec1.scl(forwardSpeed * delta);
		
		position.add(tempVec1);
		rotate();
		
		detector.update();
		super.update();
	}
	
	/**
	 * Handles the rotation
	 */
	public void rotate() {
		if (forwardVector.x != currentForwardVector.x || forwardVector.y != currentForwardVector.y ||
				forwardVector.z != currentForwardVector.z) {
			currentForwardVector.set(forwardVector);
			obb.setToLookAt(forwardVector, Vector3.Y);
			//...
			System.out.println("Set: " + forwardVector);
		}
	}
	
	public boolean isInRange(Vector3 centerTargetPos, float range) {
		// Get the origin of the target
		tempVec2.set(centerTargetPos);
		
		// Get the view vector to the target
		tempVec1.set(tempVec2.sub(position));
		
		return (tempVec1.len2() <= range * range);
	}
	
	/**
	 * Approaches the target
	 * @param centerTargetPos
	 * @param delta
	 */
	public void seek(Vector3 centerTargetPos, float delta) {
		// Get the origin of the target
		tempVec1.set(centerTargetPos);

		// Get the view vector to the target
		forwardVector.set(tempVec1.sub(obb.getCenter()));
		forwardVector.nor();
		
		// Track the target, time-based movement
		position.add(forwardVector.scl(maxSpeed * delta));
	}
	
	/**
	 * Set up the camera's direction to forward direction, setting up the y to zero then normalized
	 */
	public void forwardVectorToCamDirection() {
		forwardVector.set(camForwardVector);
		forwardVector.y = 0;
		forwardVector.nor();
	}
	
	public Vector3 getRightVector() {
		tempVec1.set(forwardVector);
		return Euler.getOrthogonalAxes(tempVec1, Vector3.Y);
	}
	
	public void setForwardSpeed(float forwardSpeed) { this.forwardSpeed = forwardSpeed; };
	public float getForwardSpeed() { return forwardSpeed; }
	
	public void setMaxSpeed(float maxSpeed) { this.maxSpeed = maxSpeed; }
	public float getMaxSpeed() { return maxSpeed; }
	
	public void setSightRange(float sightRange) { this.sightRange = sightRange; }
	public float getSightRange() { return sightRange; }
	
	public void setCamForwardVector(Vector3 camForwardVector) { this.camForwardVector = camForwardVector; }
	
	public void setCommandedMovement(Movement movement) { this.commandedMovement = movement; }
	public Movement getCommandedMovement() { return commandedMovement; }
	
	public void setCurrentMovement(Movement currentMovement) { this.currentMovement = currentMovement; }
	public Movement getCurrentMovement() { return currentMovement; }
	
	/**
	 * Will ignore the y movement and be normalized
	 * @param forwardVector
	 */
	public void setForwardVector(Vector3 forwardVector) {
		this.forwardVector.set(forwardVector);
		
		// Set the elevation movement to 0
		this.forwardVector.y = 0;
		this.forwardVector.nor();
	}
	public Vector3 getForwardVector() { return forwardVector; }
	
	
	//
	public OrientedBoundingBox getBoxDetector() {
		return detector.obb;
	}
}
