package com.nickan.epiphany.framework.math;

import com.badlogic.gdx.math.Vector3;

/**
 * Created my own BoundingBox class but renamed to BoundBox to prevent name collision. I only need 
 * to know the position, the dimension and the center of the box, no fancy stuff
 * 
 * @author Nickan
 *
 */
public class BoundBox {
	public Vector3 position;
	public Dimension3D dimension;
	Vector3 center;
	
	public BoundBox(Vector3 position, Dimension3D dimension) {
		this.position = position;
		this.dimension = dimension;
		center = new Vector3();
	}
	
	/**
	 * 
	 * @return The center of the bounding box
	 */
	public Vector3 getCenter() {
		center.x = position.x + dimension.width / 2;
		center.y = position.y + dimension.height / 2;
		center.z = position.z + dimension.depth / 2;
		return center;
	}
}
