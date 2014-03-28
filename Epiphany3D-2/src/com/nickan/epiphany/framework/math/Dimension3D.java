package com.nickan.epiphany.framework.math;

/**
 * Defines the Dimension3D of the Entity
 * @author Nickan
 *
 */
public class Dimension3D {
	public float width;
	public float height;
	public float depth;
	
	public Dimension3D() {
		width = 1;
		height = 1;
		depth = 1;
	}
	
	public Dimension3D(float width, float height, float depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	public void set(float width, float height, float depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
}
