package com.nickan.epiphany;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Epiphany3D Version 2: Alpha 0.0.1";
		cfg.useGL20 = true;
		cfg.width = 480;
		cfg.height = 320;
//		cfg.width = 340;
//		cfg.height = 210;
		
		new LwjglApplication(new Epiphany(), cfg);
	}
}
