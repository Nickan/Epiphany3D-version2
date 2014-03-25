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

	public static Vector3 getOrthogonalAxes(Vector3 v1, Vector3 v2) {
		tmp.set(v1);
		return tmp.crs(v2);
	}
}