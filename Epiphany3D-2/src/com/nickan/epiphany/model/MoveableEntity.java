package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;

public abstract class MoveableEntity extends Entity {
	float speed;
	Vector3 rotation;
	Vector3 velocity;
	
	private static Vector3 tempVec1 = new Vector3();
	private static Vector3 tempVec2 = new Vector3();

	public MoveableEntity(Vector3 position, Vector3 rotation, Dimension3D dimension, float speed) {
		super(position, dimension);
		this.rotation = rotation;
		this.speed = speed;
		this.velocity = new Vector3();
	}
	
	protected void update(float delta) {
		tempVec1.set(velocity);
		position.add(tempVec1.scl(speed * delta));
		
		// Set to zero
		tempVec1.set(Vector3.Zero);
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
	
	public Vector3 getVelocity() {
		return velocity;
	}
	
	public Vector3 getRotation() {
		return rotation;
	}

}
