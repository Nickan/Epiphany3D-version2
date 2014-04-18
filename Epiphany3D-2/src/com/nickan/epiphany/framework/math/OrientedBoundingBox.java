package com.nickan.epiphany.framework.math;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * My class for OBB, as I need it for collision detection for avoidance movement.
 * 
 * @author Nickan
 *
 */
public class OrientedBoundingBox {
	private Vector3 center;
	private Vector3 dimension;
	private Vector3 rotation;
	
	private static final Vector3 tmpVec1 = new Vector3();
	private static final Vector3 tmpVec2 = new Vector3();
	private static final Vector3 tmpVec3 = new Vector3();
	
	
	private static final int CORNERS = 8;
	private final Vector3[] corners = new Vector3[CORNERS];
	
	/**  For the vector of the corners, the original position  */
	private final Vector3[] crnVec = new Vector3[CORNERS];
	
	private static final Vector3[] tmpCrn = new Vector3[CORNERS];

	public Matrix4 transform;
	private Matrix4 invTransform = new Matrix4();
	
	private Matrix4 mTranslate = new Matrix4();
	private Matrix4 mRotate = new Matrix4();
	private Matrix4 mScale = new Matrix4();
	private static final Matrix4 m1 = new Matrix4();
	
	public OrientedBoundingBox(Vector3 center, Vector3 dimension, Vector3 rotation) {
		this.center = center;
		this.dimension = dimension;
		this.rotation = rotation;
		this.transform = new Matrix4();
		
		for (int index = 0; index < corners.length; ++index) {
			crnVec[index] = new Vector3();
			corners[index] = new Vector3();
			tmpCrn[index] = new Vector3();
		}
		
		updateDimension();
		updateTranslation();
		updateRotation();
		updateScale();
		
		updateCorners();
	}
	
	public void setRotation(Vector3 rotation) { setRotation(rotation.x, rotation.y, rotation.z); }
	
	public void setRotation(float x, float y, float z) {
		rotation.set(x, y, z);
		updateRotation();
		updateCorners();
	}
	
	public void setToLookAt(Vector3 direction, Vector3 up) {
		mRotate.idt();
		// Reverse the direction and invert the rotation matrix
		tmpVec1.set(direction).scl(-1);
		mRotate.setToLookAt(tmpVec1, up);
		mRotate.inv();
		updateCorners();
	}
	
	public void setCenter(Vector3 position) { setCenter(position.x, position.y, position.z); }
	
	public void setCenter(float x, float y, float z) {
		center.set(x, y, z);
		updateTranslation();
		updateCorners();
	}
	
	public void setDimension(Vector3 dimension) { setDimension(dimension.x, dimension.y, dimension.z); }
	
	public void setDimension(float x, float y, float z) {
		dimension.set(x, y, z);
		updateDimension();
		updateCorners();
	}
	
	private void updateDimension() {
		// Imagining facing X (1, 0, 0)
		// Back face
		// X, -Y, -Z (Lower left) Should be the min
		crnVec[0].set(-dimension.x / 2, -dimension.y / 2, -dimension.z / 2);
		// X, -Y, Z (Lower right)
		crnVec[1].set(-dimension.x / 2, -dimension.y / 2, dimension.z / 2);
		// X, Y, -Z (Upper left)
		crnVec[2].set(-dimension.x / 2, dimension.y / 2, -dimension.z / 2);
		// X, Y, Z (Upper right)
		crnVec[3].set(-dimension.x / 2, dimension.y / 2, dimension.z / 2);

		// Front face
		// -X, -Y, -Z (Lower left)
		crnVec[4].set(dimension.x / 2, -dimension.y / 2, -dimension.z / 2);
		// -X, -Y, Z (Lower right)
		crnVec[5].set(dimension.x / 2, -dimension.y / 2, dimension.z / 2);
		// -X, Y, -Z (Upper left)
		crnVec[6].set(dimension.x / 2, dimension.y / 2, -dimension.z / 2);
		// -X, Y, Z (Upper right) Should be the max
		crnVec[7].set(dimension.x / 2, dimension.y / 2, dimension.z / 2);
	}
	
	private void updateTranslation() {
		mTranslate.idt();
		mTranslate.translate(center);
	}
	
	public Matrix4 getInverseTransform() {
		// Set the inverse scale
		invTransform.idt();
		m1.idt();
//		m1.scale(1 / (dimension.x / 2), 1 / (dimension.y / 2), 1 / (dimension.z / 2) );
//		m1.scale(1 / dimension.x, 1 / dimension.y, 1 / dimension.z);
//		invTransform.mul(m1);
		
		// Set the transpose of the rotation
		m1.idt();
		m1.set(mRotate);
		m1.tra();
		invTransform.mul(m1);
		
		// Set the inverse translation
		m1.idt();
		tmpVec1.set(center);
		tmpVec1.scl(-1);
		m1.translate(tmpVec1);
		
		return invTransform.mul(m1);
	}

	private void updateRotation() {
		// Pitch
		m1.idt();
		m1.rotate(Vector3.X, (float) rotation.x);
		mRotate.idt();
		mRotate.mul(m1);

		// Yaw (Heading)
		m1.idt();
		m1.rotate(Vector3.Y, (float) rotation.y);
		mRotate.mul(m1);

		// Roll (Bank)
		m1.idt();
		m1.rotate(Vector3.Z, (float) rotation.z);
		mRotate.mul(m1);
	}
	
	private void updateScale() {
		mScale.idt();
		mScale.scale(1, 1, 1);
	}
	
	private void updateCorners() {
		// Update the transform
		transform.idt();
		transform.mul(mTranslate);
		transform.mul(mRotate);
		transform.mul(mScale);
		
		for (int index = 0; index < corners.length; ++index) {
			// Set the corners
			corners[index].set(crnVec[index]);
			corners[index].mul(transform);
		}
	}
	
	public Vector3[] getCorners() { return corners; }
	
	public Vector3 getCenter() { return center; }
	
	public Vector3 getDimension() { return dimension; }
	
	/**
	 * Only returns the set Euler angles, but if the rotation is set by setToLookAt(), it doesn't
	 * calculate for the correct rotation (For now)
	 * @return
	 */
	public Vector3 getRotation() { return rotation; }
	
	public boolean contains(Vector3 v) {
		Matrix4 invTransform = getInverseTransform();
		for (int index = 0; index < CORNERS; ++index) {
			tmpCrn[index].set(corners[index]);
			tmpCrn[index].mul(invTransform);
		}
		Vector3 min = tmpCrn[0];
		Vector3 max = tmpCrn[7];
		return contains(min, max, v);
	}
	
	public static boolean contains(Vector3 min, Vector3 max, Vector3 v) {
		return min.x <= v.x && max.x >= v.x && min.y <= v.y && max.y >= v.y && min.z <= v.z && max.z >= v.z;
	}
	
	/**
	 * Checks both box if they collide
	 * @param box1
	 * @param box2
	 * @return
	 */
	public static boolean collides(OrientedBoundingBox box1, OrientedBoundingBox box2) {
		Matrix4 invTransform = box1.getInverseTransform();
		
		tmpVec1.set(box1.getCorners()[0]);
		tmpVec2.set(box1.getCorners()[7]);
		Vector3 min = tmpVec1.mul(invTransform);
		Vector3 max = tmpVec2.mul(invTransform);
		
		Vector3[] tmpCrn = box2.getCorners();
		// Loop through all the transformed corners of box2 of the local space of box1
		for (int index = 0; index < CORNERS; ++index) {
			tmpVec3.set(tmpCrn[index]);
			tmpVec3.mul(invTransform);
			if (contains(min, max, tmpVec3)) {
				return true;
			}
		}
		
		invTransform = box2.getInverseTransform();
		// Try the local space of the box2 then
		tmpVec1.set(box2.getCorners()[0]);
		tmpVec2.set(box2.getCorners()[7]);
		min = tmpVec1.mul(invTransform);
		max = tmpVec2.mul(invTransform);
		
		tmpCrn = box1.getCorners();
		// Loop through all the transformed corners of box1 of the local space of box1
		for (int index = 0; index < CORNERS; ++index) {
			tmpVec3.set(tmpCrn[index]);
			tmpVec3.mul(invTransform);
			if (contains(min, max, tmpVec3)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Returns the coordinates of the corner that collides, note that the result should
	 * be kept as it uses static vector that will be used again. But it will return a maximum
	 * value of the float when there is no corner collided.
	 * @param box1
	 * @param box2
	 * @return
	 */
	public static Vector3 getCollidedCorner(OrientedBoundingBox box1, OrientedBoundingBox box2) {
		Matrix4 invTransform = box1.getInverseTransform();
		
		tmpVec1.set(box1.getCorners()[0]);
		tmpVec2.set(box1.getCorners()[7]);
		Vector3 min = tmpVec1.mul(invTransform);
		Vector3 max = tmpVec2.mul(invTransform);
		
		Vector3[] tmpCrn = box2.getCorners();
		// Loop through all the transformed corners of box2 of the local space of box1
		for (int index = 0; index < CORNERS; ++index) {
			tmpVec3.set(tmpCrn[index]);
			tmpVec3.mul(invTransform);
			if (contains(min, max, tmpVec3)) {
				return tmpVec3;
			}
		}
		
		invTransform = box2.getInverseTransform();
		// Try the local space of the box2 then
		tmpVec1.set(box2.getCorners()[0]);
		tmpVec2.set(box2.getCorners()[7]);
		min = tmpVec1.mul(invTransform);
		max = tmpVec2.mul(invTransform);
		
		tmpCrn = box1.getCorners();
		// Loop through all the transformed corners of box1 of the local space of box1
		for (int index = 0; index < CORNERS; ++index) {
			tmpVec3.set(tmpCrn[index]);
			tmpVec3.mul(invTransform);
			if (contains(min, max, tmpVec3)) {
				return tmpVec3;
			}
		}
		
		return tmpVec3.set(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
	}
	
}
