package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.math.Euler;
import com.nickan.epiphany.framework.math.OrientedBoundingBox;

/**
 * I will have a major overhaul, will study and implement the steering behavior
 * @author Nickan
 *
 */
public abstract class MoveableEntity extends Entity {
	Vector3 velocity;
	Vector3 heading;
	float mass;
	float maxForce;
	// In degrees
	float maxTurnRate;
	float maxSpeed;
	
	float sightRange;
	
	/** Will be the basis forward when the forward key is pressed */
	Vector3 camForwardVector;
	
	/** 
	 * For the use of setting up temporary values for vectors that should not be overridden by
	 * Vector3 operations such as add, sub, mul, etc.
	 */
	protected static final Vector3 tempVec1 = new Vector3();
	protected static final Vector3 tempVec2 = new Vector3();
	
	protected CollisionDetector detector;
	
	public MoveableEntity(Vector3 pos, Vector3 dim, Vector3 rot, float mass, float maxForce, 
			float maxTurnRate, float maxSpeed, float sightRange) {
		super(pos, dim, rot);
		this.mass = mass;
		this.maxForce = maxForce;
		this.maxTurnRate = maxTurnRate;	
		this.maxSpeed = maxSpeed;
		this.sightRange = sightRange;
		
		// Default is viewing to the from
		heading = new Vector3();
		setHeading(Vector3.Z);
		velocity = new Vector3();
		
		detector = new CollisionDetector(this);
	}

	protected void update(float delta) {
		detector.update();
		super.update();
		rotate();
	}
	
	/**
	 * Handles the rotation
	 */
	private void rotate() {
	//	if (heading.x != currentForwardVector.x || heading.y != currentForwardVector.y ||
	//			heading.z != currentForwardVector.z) {
	//		currentForwardVector.set(heading);
			obb.setToLookAt(heading, Vector3.Y);
	//	}	
	}
	
	public boolean isInRange(Vector3 targetPos, float range) {
		// Get the origin of the target
		tempVec2.set(targetPos);
		
		// Get the view vector to the target
		tempVec1.set(tempVec2.sub(position));
		
		return (tempVec1.len2() <= range * range);
	}
	
	public Vector3 getRightVector() {
		tempVec1.set(camForwardVector);
		return Euler.getOrthogonalAxes(tempVec1, Vector3.Y);
	}
	
	public void setMaxSpeed(float maxSpeed) { this.maxSpeed = maxSpeed; }
	public float getMaxSpeed() { return maxSpeed; }
	
	public void setSightRange(float sightRange) { this.sightRange = sightRange; }
	public float getSightRange() { return sightRange; }
	
	public void setCamForwardVector(Vector3 camForwardVector) { this.camForwardVector = camForwardVector; }
	public Vector3 getCamForwardVector() { return camForwardVector; }
	
	public void setHeading(Vector3 heading) { this.heading.set(heading); }
	public Vector3 getHeading() { return heading; }	
	
	//
	public OrientedBoundingBox getBoxDetector() { return detector.obb; }

	public Vector3 getVelocity() { return velocity; }
	public void setVelocity(Vector3 velocity) { this.velocity = velocity; }

	public float getMass() { return mass; }
	public void setMass(float mass) { this.mass = mass; }

	public float getMaxForce() { return maxForce; }
	public void setMaxForce(float maxForce) { this.maxForce = maxForce; }
	
	public float getMaxTurnRate() { return maxTurnRate; }
	public void setMaxTurnRate(float maxTurnRate) { this.maxTurnRate = maxTurnRate; }
	
	
}
