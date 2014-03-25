package com.nickan.epiphany.model;

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
	
}
