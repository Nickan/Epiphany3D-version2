package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.nickan.epiphany.framework.finitestatemachine.BaseEntity;

public abstract class Entity extends BaseEntity {
	BoundingBox boundingBox;
	/** 
	 * For the use of setting up temporary values for vectors that should not be overridden by
	 * Vector3 operations such as add, sub, mul, etc.
	 */
	static final Vector3 tempVec1 = new Vector3();
	
	public Entity(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	protected void update() {
		tempVec1.set(boundingBox.min);
		// Update the max as min I think will always be manipulated as the position
		boundingBox.set(boundingBox.min, tempVec1.add(boundingBox.getDimensions()));
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
}
