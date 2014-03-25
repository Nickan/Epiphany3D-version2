package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.Epiphany;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.Dimension3D;

public class World {
	Character player = new Character(new Vector3(0, 0, 5), new Vector3(0, 0, 0), new Dimension3D(), 1.0f);
	Character enemy = new Character(new Vector3(10, 0, 0), new Vector3(0, 0, 0), new Dimension3D(), 1.0f);
	
	public World(Epiphany game) {
		
	}
	
	public void update(float delta) {
		
	}
	
	public void resize(int width, int height) {
		
	}
}
