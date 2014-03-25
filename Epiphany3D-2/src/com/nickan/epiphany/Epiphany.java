package com.nickan.epiphany;

import com.badlogic.gdx.Game;
import com.nickan.epiphany.gamescreen.GameScreen;

public class Epiphany extends Game {

	@Override
	public void create() {
		this.setScreen(new GameScreen(this));
	}

}
