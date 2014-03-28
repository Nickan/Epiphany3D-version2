package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.MessageDispatcher;
import com.nickan.epiphany.model.MoveableEntity.Movement;

/**
 * It will be hard for me to manage all the button detection if I will create a rectangle for buttons
 * and assign a texture for it, I know I tried. So I resorted to use the existing Scene2D, though
 * I can't rotate the image, later I will try to find out how to do it.
 * 
 * @author Nickan
 *
 */
public class AllGameButtonsView {
	World world;
	
	Button forward;
	Button backward;
	Button left;
	Button right;
	
	Button attack;
	
	Skin skin;
	Stage stage;
	TextureAtlas atlas;
	
	public AllGameButtonsView(World world) {
		this.world = world;
	}
	
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}
	
	public void resize(int width, int height) {
		if (stage == null) {
			stage = new Stage(width, height, true);
		}
		stage.setViewport(width, height, true);
		stage.clear();
		
		// Set the sizes and the position of the buttons
		float unitX = width / 24;
		float unitY = height / 18;
		float buttonWidth = unitX * 2.5f;
		float buttonHeight = unitY * 2.5f;
		
		// Movement buttons
		forward = new Button(skin.getDrawable("forwardnormal"), skin.getDrawable("forwardpressed"));
		forward.setPosition(unitX * 2.5f, unitY * 5);
		forward.setSize(buttonWidth, buttonHeight);
		
		backward = new Button(skin.getDrawable("backwardnormal"), skin.getDrawable("backwardpressed"));
		backward.setPosition(unitX * 2.5f, 0);
		backward.setSize(buttonWidth, buttonHeight);
		
		left = new Button(skin.getDrawable("leftnormal"), skin.getDrawable("leftpressed"));
		left.setPosition(0, unitY * 2.5f);
		left.setSize(buttonWidth, buttonHeight);
		
		right = new Button(skin.getDrawable("rightnormal"), skin.getDrawable("rightpressed"));
		right.setPosition(unitX * 5, unitY * 2.5f);
		right.setSize(buttonWidth, buttonHeight);
		
		
		attack = new Button(skin.getDrawable("attack"));
		attack.setPosition(unitX * 21, unitY * 2);
		attack.setSize(buttonWidth, buttonHeight);
		
		
		stage.addActor(forward);
		stage.addActor(backward);
		stage.addActor(left);
		stage.addActor(right);

		
		stage.addActor(attack);
		
		initializeMovementButtons();
		initializeSkillsAndAttackButtons();
	}
	
	public void show() {
		atlas = new TextureAtlas(Gdx.files.internal("gamescreen/allgamebuttonstexture.pack"), false);
		skin = new Skin(atlas);
	}
	
	private void initializeMovementButtons() {
		// The touchUp and the touchDragged will only be activated if the button was previously touched down
		
		forward.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				MessageDispatcher.sendMessage(-10, world.player.getId(), 0, 
						MessageType.PLAYER_MOVE, Movement.FORWARD);
				
				world.stopCameraRotation = false;
				return true;
			}
			
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				MessageDispatcher.sendMessage(-10, world.player.getId(), 0, MessageType.PLAYER_MOVE, Movement.STOP);
				world.stopCameraRotation = false;
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				world.stopCameraRotation = true;
			}

		});
		
		backward.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				MessageDispatcher.sendMessage(-10, world.player.getId(), 0, 
						MessageType.PLAYER_MOVE, Movement.BACKWARD);
				
				world.stopCameraRotation = false;
				return true;
			}
			
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				MessageDispatcher.sendMessage(-10, world.player.getId(), 0, MessageType.PLAYER_MOVE, Movement.STOP);
				world.stopCameraRotation = false;
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				world.stopCameraRotation = true;
			}
		});
		
		left.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				MessageDispatcher.sendMessage(-10, world.player.getId(), 0, 
						MessageType.PLAYER_MOVE, Movement.LEFT);
				
				world.stopCameraRotation = false;
				return true;
			}
			
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				MessageDispatcher.sendMessage(-10, world.player.getId(), 0, 
						MessageType.PLAYER_MOVE, Movement.STOP);
				world.stopCameraRotation = false;
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				world.stopCameraRotation = true;
			}
		});
		
		right.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				MessageDispatcher.sendMessage(-10, world.player.getId(), 0, 
						MessageType.PLAYER_MOVE, Movement.RIGHT);
				world.stopCameraRotation = false;
				return true;
			}
			
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				MessageDispatcher.sendMessage(-10, world.player.getId(), 0, 
						MessageType.PLAYER_MOVE, Movement.STOP);
				world.stopCameraRotation = false;
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				world.stopCameraRotation = true;
			}
		});
	}
	
	private void initializeSkillsAndAttackButtons() {
		attack.setColor(1.0f, 1.0f, 1.0f, 0.4f);
		attack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				attack.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				world.stopCameraRotation = false;
				world.playerSeekTarget();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {	
				attack.setColor(1.0f, 1.0f, 1.0f, 0.4f);
				world.stopCameraRotation = false;
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				world.stopCameraRotation = true;
			}
			
		});
	}
	
	
	public void dispose() {
		skin.dispose();
		atlas.dispose();
	}
	
}
