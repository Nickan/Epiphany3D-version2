package com.nickan.epiphany.screens.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.nickan.epiphany.model.Zombie;

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
	private Array<ModelInstance> zombies;
	
	// For fixing problem light
//	private DirectionalLight dirLight;
	
	/** Array of ModelInstances that have same Environment effect on them, else don't add it here */
	private Array<ModelInstance> worldModelInstances;
	
	/** Array of animation handlers for the character */
	private Array<AnimationHandler> aniHandlers;
	
	PerspectiveCameraHandler camHandler;
	
	
	// For debugging
	private ShapeRenderer sRenderer = new ShapeRenderer();
	
	public WorldRenderer(World world) {
		this.world = world;
		worldModelInstances = new Array<ModelInstance>();
		zombies = new Array<ModelInstance>();
		aniHandlers = new Array<AnimationHandler>();
		
		loadAssets();
		
		// Set up the viewport of the game
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		pointLight = new PointLight().set(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 5f);
		environment = new Environment();
		
		// For fixing...
//		dirLight = new DirectionalLight().set(1.0f, 10.0f, 1.0f, 0, -1.0f, 0);
//		environment.add(dirLight);
		environment.add(pointLight);
		
		// Setting up the camera
		float fov = 67;
		perspectiveCam = new PerspectiveCamera(fov, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		perspectiveCam.position.set(1.0f, 1.0f, 10.0f);
		perspectiveCam.near = 1.0f;
		perspectiveCam.far = 300.0f;
		perspectiveCam.update();
		
		camHandler = new PerspectiveCameraHandler(perspectiveCam);
		
		modelBatch = new ModelBatch();
		
		loadingResources = true;
	}
	
	private void loadAssets() {
		assetManager = new AssetManager();
		assetManager.load("gamescreen/graphics3d/scene.g3db", Model.class);
		assetManager.load("gamescreen/graphics3d/player.g3db", Model.class);
		assetManager.load("gamescreen/graphics3d/zombie.g3db", Model.class);
	}
	
	/**
	 * Sets up all the Model instances and the scene of the game
	 */
	private void initializeAssets() {
		Model sceneModel = assetManager.get("gamescreen/graphics3d/scene.g3db", Model.class);
		
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
			} else if (id.startsWith("wall")) {
				worldModelInstances.add(newInstance);
			}
		}
		
		// Player's model
		player = new ModelInstance(assetManager.get("gamescreen/graphics3d/player.g3db", Model.class));

	
		worldModelInstances.add(player);
	}
	
	/**
	 * Initialize the AnimationHandlers of the Characters
	 */
	private void initializeAnimationHandlers() {
		int standingFrameNumber = 6;
		int runningFrameNumber = 40;
		int attackingFrameNumber = 10;
		
		aniHandlers.add(new AnimationHandler(world.player, new AnimationController(player), 
				standingFrameNumber, runningFrameNumber, attackingFrameNumber));
		
		Model zombieModel = assetManager.get("gamescreen/graphics3d/zombie.g3db", Model.class);
		for (Zombie zombie: world.zombies) {
			ModelInstance zomModInstance = new ModelInstance(zombieModel);
			zombies.add(zomModInstance);
			aniHandlers.add(new AnimationHandler(zombie, new AnimationController(zomModInstance), 11, 20, 10));
		}
		
		worldModelInstances.addAll(zombies);
	}
	
	public void render(float delta) {
		// The assets should only be initialize once all the ModelInstances and the scene of the game are loaded
		if (loadingResources && assetManager.update()) {
			initializeAssets();
			initializeAnimationHandlers();
			loadingResources = false;
		}
		
		// Set the position of the point light just about above the players position
		pointLight.position.set(world.player.getBoundingBox().getCenter()).y = 1.5f;
		
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
		
		camHandler.update(world.player.getBoundingBox().getCenter(), world.getCamDirection(), delta);
//		camHandler.update(world.zombies.get(0).getBoundingBox().getCenter(), world.getCamDirection(), delta);
		
		debug();
	}
	
	
	// For debugging methods
	private void debug() {
		sRenderer.setProjectionMatrix(perspectiveCam.combined);
		sRenderer.begin(ShapeType.Line);
		sRenderer.setColor(Color.WHITE);
		
		// Drawing the tiles
		for (int z = 0; z < 10; ++z) {
			for (int x = 0; x < 10; ++x) {
				sRenderer.identity();
				sRenderer.translate(x / 2f, 0, z / 2f);
				sRenderer.rotate(1, 0, 0, 90);
				
				// Representing the node, rect and circle form
			//	sRenderer.rect(0, 0, 0.5f, 0.5f);
			//	sRenderer.circle(0.25f, 0.25f, 0.25f, 10);
			}
		}
		
		
		// Drawing the node occupied by the entity
		Vector3 zomPos = world.zombies.get(0).getPosition();
		sRenderer.identity();
		sRenderer.translate(zomPos.x, 0, zomPos.z);
		sRenderer.rotate(1, 0, 0, 90);
		sRenderer.circle(0, 0, world.zombies.get(0).getBoundingBox().getDimension().x / 2, 10);
		
		sRenderer.end();
		
		sRenderer.begin(ShapeType.Line);
		// For OBB
		drawCorners(world.player.getBoundingBox().getCorners());
	//	drawCorners(world.player.getBoxDetector().getCorners());
		
		for (Zombie zombie: world.zombies) {
			drawCorners(zombie.getBoundingBox().getCorners());
			drawCorners(zombie.getBoxDetector().getCorners());
		}
		sRenderer.end();

		sRenderer.end();
	}
	
	public void resize(int width, int height) {
		
	}
	
	public void show() {
		
	}
	
	public void dispose() {
		assetManager.dispose();
		modelBatch.dispose();
	}
	
	private void drawCorners(Vector3[] corners) {
		sRenderer.setColor(Color.YELLOW);
		sRenderer.identity();
		
		// Back lower left to back lower right
		sRenderer.line(corners[0], corners[1]);
		// Back lower left to back upper left
		sRenderer.line(corners[0], corners[2]);
		// Back lower left to front lower left
		sRenderer.line(corners[0], corners[4]);
		
		// Front upper right to front upper left
		sRenderer.line(corners[7], corners[6]);
		// Front upper right to front lower right
		sRenderer.line(corners[7], corners[5]);
		// Front upper right to back upper right
		sRenderer.line(corners[7], corners[3]);		
	}
	
}
