package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.math.Euler;

public abstract class MoveableEntity extends Entity {
	float speed;
	float forwardSpeed;
	float sideSpeed;
	
	/** The view vector of the entity */
	Vector3 forwardVector;
	Vector3 rightVector;
	
	protected static Vector3 tempVec1 = new Vector3();
	protected static Vector3 tempVec2 = new Vector3();
	protected static Vector3 tempVec3 = new Vector3();
	
	public enum Movement { FORWARD, BACKWARD, LEFT, RIGHT, STOP };
	private Movement movement = Movement.STOP;

	public MoveableEntity(Vector3 position, Dimension3D dimension, float speed) {
		super(position, dimension);
		this.speed = speed;
		
		// Default is viewing to the right
		this.forwardVector = new Vector3(1.0f, 0, 0);
	}
	
	protected void update(float delta) {
		tempVec1.set(forwardVector);
		
		// Get the right vector
		tempVec2.set(tempVec1).crs(Vector3.Y);
		
		// Set the velocity
		tempVec3.set(tempVec1.scl(forwardSpeed).add(tempVec2.scl(sideSpeed)));

		position.add(tempVec3);
	}
	
	public boolean isInRange(Vector3 target, float range) {
		// Check if the length of the distance between the target is lower than the radius of the target
		tempVec1.set(Vector3.Zero);
		tempVec2.set(target);
		
		// Get the view vector to the target
		tempVec1.set(tempVec2.sub(position));
		
		return (tempVec1.len2() <= range * range);
	}
	
	public void seek(Vector3 target, float delta) {
		// Reset value
		tempVec1.set(Vector3.Zero);
		tempVec2.set(target);

		// Get the view vector to the target
		tempVec1.set(tempVec2.sub(position));
		tempVec1.nor();
		
		// Track the target
		position.add(tempVec1.scl(speed * delta));
	}
	
	public void setForwardVector(Vector3 camDirection) {
		forwardVector.set(camDirection);
		
		// Set the elevation movement to 0
		forwardVector.y = 0;
		forwardVector.nor();
	}
	
	public Vector3 getForwardVector() {
		return forwardVector;
	}
	
	public Vector3 getRightVector() {
		return Euler.getOrthogonalAxes(forwardVector, Vector3.Y);
	}
	
	public void setMovement(Movement movement) {
		switch (movement) {
		case FORWARD: forwardSpeed = speed;
			break;
		case BACKWARD: forwardSpeed = -speed;
			break;
		case LEFT: sideSpeed = -speed;
			break;
		case RIGHT: sideSpeed = speed;
			break;
		case STOP:
			forwardSpeed = 0;
			sideSpeed = 0;
			break;
		}
		
		// Detects whether there is movement changed
		if (this.movement != movement) {
			movementChanged(movement);
			this.movement = movement;
		}
	}
	
	/**
	 * Will be called whenever the movement has changed
	 */
	public abstract void movementChanged(Movement movement);
	
	public Movement getMovement() {
		return movement;
	}
	
	
	
}
