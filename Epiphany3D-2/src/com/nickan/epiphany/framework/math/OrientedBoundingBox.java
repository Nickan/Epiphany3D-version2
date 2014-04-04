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
	private static final Vector3 tmpVec = new Vector3();
	
	private static final int CORNERS = 8;
	private final Vector3[] corners = new Vector3[CORNERS];
	
	/**  For the vector of the corners, the original position  */
	private final Vector3[] crnVec = new Vector3[CORNERS];

	private Matrix4 transform;
	private static final Matrix4 m1 = new Matrix4();
	
	public OrientedBoundingBox(Vector3 center, Vector3 dimension, Vector3 rotation) {
		this.center = center;
		this.dimension = dimension;
		this.rotation = rotation;
		this.transform = new Matrix4();
		
		for (int index = 0; index < corners.length; ++index) {
			crnVec[index] = new Vector3();
			corners[index] = new Vector3();
		}
		
		updateDimension();
		updateRotation();
		updateCorners();
	}
	
	public void setRotation(Vector3 rotation) { setRotation(rotation.x, rotation.y, rotation.z); }
	
	public void setRotation(float x, float y, float z) {
		rotation.set(x, y, z);
		updateRotation();
		updateCorners();
	}
	
	public void setToLookAt(Vector3 direction, Vector3 up) {
		transform.idt();
		
		// Reverse the vector and inverts the matrix to correct the positioning of the corners
		tmpVec.set(direction).scl(-1);
		transform.setToLookAt(tmpVec, up);
		transform.inv();
		updateCorners();
	}
	
	public void setCenter(Vector3 position) { setCenter(position.x, position.y, position.z); }
	
	public void setCenter(float x, float y, float z) {
		center.set(x, y, z);
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
		// Front face
		// X, -Y, -Z (Lower left)
		crnVec[0].set(dimension.x / 2, -dimension.y / 2, -dimension.z / 2);
		// X, -Y, Z (Lower right)
		crnVec[1].set(dimension.x / 2, -dimension.y / 2, dimension.z / 2);
		// X, Y, -Z (Upper left)
		crnVec[2].set(dimension.x / 2, dimension.y / 2, -dimension.z / 2);
		// X, Y, Z (Upper right)
		crnVec[3].set(dimension.x / 2, dimension.y / 2, dimension.z / 2);

		// Back face
		// -X, -Y, -Z (Lower left)
		crnVec[4].set(-dimension.x / 2, -dimension.y / 2, -dimension.z / 2);
		// -X, -Y, Z (Lower right)
		crnVec[5].set(-dimension.x / 2, -dimension.y / 2, dimension.z / 2);
		// -X, Y, -Z (Upper left)
		crnVec[6].set(-dimension.x / 2, dimension.y / 2, -dimension.z / 2);
		// -X, Y, Z (Upper right)
		crnVec[7].set(-dimension.x / 2, dimension.y / 2, dimension.z / 2);
	}
	
	private void updateRotation() {
		// Pitch
		m1.idt();
		m1.rotate(Vector3.X, (float) Math.toDegrees(rotation.x));
		transform.idt();
		transform.mul(m1);

		// Yaw (Heading)
		m1.idt();
		m1.rotate(Vector3.Y, (float) Math.toDegrees(rotation.y));
		transform.mul(m1);

		// Roll (Bank)
		m1.idt();
		m1.rotate(Vector3.Z, (float) Math.toDegrees(rotation.z));
		transform.mul(m1);
	}
	
	private void updateCorners() {
		for (int index = 0; index < corners.length; ++index) {
			// Set the corners
			corners[index].set(crnVec[index]);
			corners[index].mul(transform);
			
			// Adding the position
			corners[index].add(center);
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
	
}
