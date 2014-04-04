package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.finitestatemachine.BaseEntity;
import com.nickan.epiphany.framework.math.OrientedBoundingBox;

public abstract class Entity extends BaseEntity {
	protected Vector3 position;
	/** Contains the center, rotation and corners of the entity */
	protected OrientedBoundingBox obb;
	
	public Entity(Vector3 position, Vector3 dimension, Vector3 rotation) {
		this.position = position;
		obb = new OrientedBoundingBox(new Vector3(position.x,
				position.y + dimension.y / 2, position.z),
				dimension, rotation);
	}
	
	protected void update() {
		// Update the OBB's position
		Vector3 dimension = obb.getDimension();
		obb.setCenter(position.x, position.y + dimension.y / 2, position.z);
	}

	public OrientedBoundingBox getBoundingBox() { return obb; }
	
	public void setPosition(Vector3 position) { this.position.set(position); }
	public Vector3 getPosition() { return position; }
}
