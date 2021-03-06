package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.math.OrientedBoundingBox;

/**
 * Separates the detection of collision to ease management of it
 * @author Nickan
 *
 */
public class CollisionDetector {
	OrientedBoundingBox obb;
	MoveableEntity entity;
	private final Vector3 view = new Vector3();
	private static final Vector3 tmpVec1 = new Vector3();
	private static final Vector3 tmpVec2 = new Vector3();
	
	public CollisionDetector(MoveableEntity entity) {
		this.entity = entity;
		
		OrientedBoundingBox box = entity.getBoundingBox();
		
		Vector3 center = new Vector3(box.getCenter());
		Vector3 dimension = new Vector3(box.getDimension());
		Vector3 rotation = new Vector3(box.getRotation());
		
		obb = new OrientedBoundingBox(center, dimension, rotation);
	}
	
	public void update() {
		position();
		rotate();
	}
	
	/**
	 * Set the position of the collision detector box in front of the entity
	 */
	private void position() {
		// For now, the dimension.x of the entity will be doubled to be the collision detector for its
		// movement
		OrientedBoundingBox entBound = entity.getBoundingBox();
		
		tmpVec1.set(entBound.getCenter());
		
		// Place the point in front of the bound
		float frontDepth = entity.getBoundingBox().getDimension().z / 2;
		tmpVec2.set(entity.getHeading()).scl(frontDepth);
		tmpVec1.add(tmpVec2);
		
		// Compensate the center position of the collision box
		float collisionDepth = obb.getDimension().z / 2;
		tmpVec2.set(entity.getHeading()).scl(collisionDepth);
		tmpVec1.add(tmpVec2);
		
		// Compensating the scale of the collision obb
//		tmpVec2.set(entity.getHeading()).scl(obb.getDimension().z / 2);
		
		obb.setCenter(tmpVec1);
	}
	
	private void rotate() {
		Vector3 entView = entity.getHeading();
		// Will only set the view if the view of the entity has changed
		if (view.x != entView.x || view.y != entView.y || view.z != entView.z) {
			view.set(entView);
			obb.setToLookAt(entity.getHeading(), Vector3.Y);
		}
	}
	
	public void setCollisionDepth(float depth) {
		obb.setDimension(obb.getDimension().x, obb.getDimension().y, depth);
	}
	
}
