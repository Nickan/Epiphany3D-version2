package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.utils.Array;

public class WorldRenderer {
	World world;
	
	/** 
	 * Indicator to know if the AssetManager is done loading the resources, ready to be initialized and 
	 * ready to be rendered 
	 */
	private boolean loadingResources;
	
	private AssetManager assetManager;
	
	/** Environment where lights will be added */
	private Environment environment;
	private ModelBatch modelBatch;
	private PerspectiveCamera perspectiveCam;
	private PointLight pointLight;
	
	private ModelInstance player;
	private Array<ModelInstance> enemies;
	
	// For fixing problem light
	private DirectionalLight dirLight;
	
	/** Array of ModelInstances that have same Environment effect on them, else don't add it here */
	private Array<ModelInstance> worldModelInstances;
	
	/** Array of animation handlers for the character */
	private Array<AnimationHandler> aniHandlers;
	
	public WorldRenderer(World world) {
		this.world = world;
		worldModelInstances = new Array<ModelInstance>();
		enemies = new Array<ModelInstance>();
		aniHandlers = new Array<AnimationHandler>();
		
		loadAssets();
		
		// Set up the viewport of the game
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		pointLight = new PointLight().set(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 10.0f);
		environment = new Environment();
		
		// For fixing...
		dirLight = new DirectionalLight().set(1.0f, 10.0f, 1.0f, 0, -1.0f, 0);
		environment.add(dirLight);
//		environment.add(pointLight);
		
		// Setting up the camera
		float fov = 67;
		perspectiveCam = new PerspectiveCamera(fov, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		perspectiveCam.position.set(1.0f, 1.0f, 10.0f);
		perspectiveCam.near = 1.0f;
		perspectiveCam.far = 300.0f;
		perspectiveCam.update();
		
		modelBatch = new ModelBatch();
		
		loadingResources = true;
	}
	
	private void loadAssets() {
		assetManager = new AssetManager();
		assetManager.load("gamescreen/scene.g3db", Model.class);
		assetManager.load("gamescreen/player.g3db", Model.class);
	}
	
	/**
	 * Sets up all the Model instances and the scene of the game
	 */
	private void initializeAssets() {
		Model sceneModel = assetManager.get("gamescreen/scene.g3db", Model.class);
		
		// Load all the nodes the scene have
		for (int index = 0; index < sceneModel.nodes.size; ++index) {
			// Get the Id of the node
			String id = sceneModel.nodes.get(index).id;
			
			// Create a new ModelInstance of the node of the scene
			ModelInstance newInstance = new ModelInstance(sceneModel, id);
			
			// Set up the node of the new ModelInstance
			Node node = newInstance.getNode(id);
			
			// Initialize the new ModelInstance's transform based on its node
			// (Wouldn't it be good just to initialize all of this when the ModelInstance is created 
			// with a passed ID? or they let it this way for flexibility)
			newInstance.transform.set(node.globalTransform);
			
			// Initialize the node of the new ModelInstance
			node.translation.set(0, 0, 0);
			node.scale.set(1, 1, 1);
			node.rotation.idt();
			
			// For what I understand, it calculates the RTS of all the nodes this new ModelInstance have
			newInstance.calculateTransforms();
			
			// FIXME TO DO MODE HERE... I HAVE WALL MODEL BUT NOT OPTIMIZED
			
			// Then add the needed models
			if (id.startsWith("ground")) {
				worldModelInstances.add(newInstance);
			}
		}
		
		// Player's model
		player = new ModelInstance(assetManager.get("gamescreen/player.g3db", Model.class));
		
		worldModelInstances.add(player);
	}
	
	private void initializeAnimationHandlers() {
		int standingFrameNumber = 6;
		int runningFrameNumber = 10;
		int attackingFrameNumber = 40;
		aniHandlers.add(new AnimationHandler(world.player, new AnimationController(player), 
				standingFrameNumber, runningFrameNumber, attackingFrameNumber));
	}
	
	
	public void render(float delta) {
		// The assets should only be initialize once all the ModelInstances and the scene of the game are loaded
		if (loadingResources && assetManager.update()) {
			initializeAssets();
			initializeAnimationHandlers();
			loadingResources = false;
		}
		
		// Update the AnimationHandlers
		for (AnimationHandler handler: aniHandlers) {
			handler.update(delta);
		}
		
		// Clear the background, using Gdx.gl10 will give weird result
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
		
		// Start of batching
		modelBatch.begin(perspectiveCam);
		
		// Regular world instances
		modelBatch.render(worldModelInstances, environment);
		
		modelBatch.end();
	}
	
	public void resize(int width, int height) {
		
	}
	
	public void show() {
		
	}
	
	public void dispose() {
		assetManager.dispose();
		modelBatch.dispose();
	}
	
}
