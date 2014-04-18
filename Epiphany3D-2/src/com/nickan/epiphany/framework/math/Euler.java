package com.nickan.epiphany.framework.math;

import com.badlogic.gdx.math.Vector3;

/**
 * I realize that every method here uses heavy computations, needs to be improved I think.
 * @author Nickan
 *
 */
public class Euler {
	private static final Vector3 tmp = new Vector3();

	private Euler() { }
	
	/**
	 * 
	 * @param degrees
	 * @return
	 */
	public static Vector3 toAxes(final Vector3 degrees) {
		float thetaX = (float) Math.toRadians(degrees.x);
		float thetaY = (float) Math.toRadians(degrees.y);

		tmp.x = (float) (Math.cos(thetaY) * Math.cos(thetaX)); 	// Pitch
		tmp.y = (float) Math.sin(thetaX);						// Yaw
		tmp.z = (float) (Math.sin(thetaY) * Math.cos(thetaX));	// Roll
		return tmp;
	}

	public static Vector3 toAxes(float x, float y, float z) {
		float thetaX = (float) Math.toRadians(x);
		float thetaY = (float) Math.toRadians(y);

		tmp.x = (float) (Math.cos(thetaY) * Math.cos(thetaX)); 	// Pitch
		tmp.y = (float) Math.sin(thetaX);						// Yaw
		tmp.z = (float) (Math.sin(thetaY) * Math.cos(thetaX));	// Roll
		return tmp;
	}
	
	/**
	 * Still not finished, not working correctly
	 * @param unitVec
	 * @return
	 */
	static Vector3 toAngles(Vector3 unitVec) {
		tmp.set(Vector3.Zero);
		
		// If vector is (1, 0, 0), it should be (0, 0, 0), if (-1, 0, 0), degrees = (
		
		// If vector is (0, 1, 0), degrees = (90, 0, 0)
		
		// The heading should be define first to know the other values
//		float thetaX = (float) Math.atan2(unitVec.x + unitVec.z, unitVec.y);
		float thetaX = (float) Math.asin(-unitVec.y);
		// Rotation x is the pitch
		tmp.x = (float) Math.toDegrees(thetaX);
		
		// Rotation y is the yaw
		
		
		// ThetaY
		tmp.y = (float) Math.toDegrees(Math.atan2(unitVec.x, unitVec.z));
		
		return tmp;
	}
	
	// A grass method, should be removed >_<
	public static Vector3 getOrthogonalAxes(Vector3 v1, Vector3 v2) {
		tmp.set(v1);
		return tmp.crs(v2);
	}
}