package com.nickan.epiphany.screens.gamescreen;

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
import com.nickan.epiphany.model.inventory.Inventory;
import com.nickan.epiphany.screens.gamescreen.World.State;

/**
 * It will be hard for me to manage all the button detection if I will create a rectangle for buttons
 * and assign a texture for it, so I use the existing awesome Scene2D.
 * 
 * @author Nickan
 *
 */
public class AllGameButtonsView {
	World world;
	GameScreen screen;
	
	Button forward;
	Button backward;
	Button left;
	Button right;
	
	Button attack;
	
	Button pause;
	Button resume;
	
	Button head;
	Button leftHand;
	Button rightHand;
	Button body;
	Button gloves;
	Button boots;
	
	private Skin skin;
	Stage stage;
	private TextureAtlas atlas;
	
	private static final int INVENTORY_COLUMNS = 4;
	private static final int INVENTORY_ROWS = 8;
	
	// Needs to be a field, as needed for button detection
	float inventoryX;
	float inventoryY;
	float inventoryButtonWidth;
	float inventoryButtonHeight;

	Button[][] inventoryButtons = new Button[INVENTORY_COLUMNS][INVENTORY_ROWS];
	
	public AllGameButtonsView(World world, GameScreen screen) {
		this.world = world;
		this.screen = screen;
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
		float unitX = width / 24.0f;
		float unitY = height / 18.0f;

		initializeMovementButtons(unitX, unitY);
		initializeSkillsAndAttackButtons(unitX, unitY);
		initializePauseButton(unitX, unitY);
		initializeResumeButton(unitX, unitY);
		initializeInventoryButtons(unitX, unitY);
		initializeEquipmentButtons(unitX, unitY);
		
		switch (world.currentState) {
		case GAME:
			addMovementButtons();
			addSkillsAndAttackButtons();
			stage.addActor(pause);
			screen.setGameController();
			break;
		case PAUSE:
			addInventoryButtons();
			addEquipmentButtons();
			stage.addActor(resume);
			screen.setPauseController();
			break;
		}
	}
	
	public void show() {
		atlas = new TextureAtlas(Gdx.files.internal("gamescreen/allgamebuttonstexture.pack"), false);
		skin = new Skin(atlas);
	}
	
	private void initializeMovementButtons(float unitX, float unitY) {
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
	
	private void initializeSkillsAndAttackButtons(float unitX, float unitY) {
		float buttonWidth = unitX * 2.5f;
		float buttonHeight = unitY * 2.5f;
		
		attack = new Button(skin.getDrawable("attack"));
		attack.setPosition(unitX * 21, unitY * 2);
		attack.setSize(buttonWidth, buttonHeight);
		
		
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
	
	private void initializePauseButton(float unitX, float unitY) {
		pause = new Button(skin.getDrawable("pausenormal"), skin.getDrawable("pausepressed"));
		pause.setPosition(0, unitY * 9);
		pause.setSize(unitX * 1.5f, unitY * 1.5f);
		
		pause.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				world.currentState = State.PAUSE;
				world.stopCameraRotation = false;
				removeMovementButtons();
				removeSkillsAndAttackButtons();
				pause.remove();
				stage.addActor(resume);
				screen.setPauseController();
				addInventoryButtons();
				addEquipmentButtons();
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				world.stopCameraRotation = true;
			}
		});
	}
	
	private void initializeResumeButton(float unitX, float unitY) {
		resume = new Button(skin.getDrawable("resumenormal"), skin.getDrawable("resumepressed"));
		resume.setPosition(0, unitY * 9);
		resume.setSize(unitX * 1.5f, unitY * 1.5f);
		
		resume.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				world.currentState = State.GAME;
				world.stopCameraRotation = false;
				addMovementButtons();
				addSkillsAndAttackButtons();
				resume.remove();
				stage.addActor(pause);
				screen.setGameController();
				removeInventoryButtons();
				removeEquipmentButtons();
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				world.stopCameraRotation = true;
			}
		});
	}
	
	private void initializeInventoryButtons(float unitX, float unitY) {
		inventoryButtonWidth = unitX * 1.78f;
		inventoryButtonHeight = unitY * 2.1f;
		inventoryX = unitX * 9.2f;
		inventoryY = unitY * 0.5f;
		// Setting up the inventory buttons
		for (int col = 0; col < INVENTORY_COLUMNS; ++col) {
			for (int row = 0; row < INVENTORY_ROWS; ++row) {
				Button button = new Button(skin.getDrawable("itemslotnormal"),
						skin.getDrawable("itemslotpressed"));
				button.setPosition((row * inventoryButtonWidth) + inventoryX,
						(col * inventoryButtonHeight) + inventoryY);
				button.setSize(inventoryButtonWidth, inventoryButtonHeight);
				
				inventoryButtons[col][row] = button;
				button.addListener(new InputListener() {
					public boolean touchDown(InputEvent event, float x,
							float y, int button, int pointer) {	
						// I need what column and row number this button is
						// Remove the starting position
						float startX = event.getStageX() - inventoryX;
						float startY = event.getStageY() - inventoryY;
						
						// Getting the index by their size
						int col = (int) (startX / inventoryButtonWidth);
						int row = (int) (startY / inventoryButtonHeight);
						
						Inventory inventory = world.player.inventory;
						inventory.use(row, col);
						return true;
					}

					public void touchUp(InputEvent event, float x, float y,
							int button, int pointer) {

					}

					public void touchDragged(InputEvent event, float x,
							float y, int pointer) {

					}
				});
			}
		}
	}
	
	private void initializeEquipmentButtons(float unitX, float unitY) {
		head = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		head.setPosition(unitX * 12.0f, unitY * 15f);
		head.setSize(unitX * 1.5f, unitY * 1.5f);
		
		leftHand = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		leftHand.setPosition(unitX * 10f, unitY * 13f);
		leftHand.setSize(unitX * 1.5f, unitY * 1.5f);
		
		rightHand = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		rightHand.setPosition(unitX * 14.0f, unitY * 13.0f);
		rightHand.setSize(unitX * 1.5f, unitY * 1.5f);
		
		body = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		body.setPosition(unitX * 12.0f, unitY * 13.0f);
		body.setSize(unitX * 1.5f, unitY * 1.5f);
		
		gloves = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		gloves.setPosition(unitX * 13.0f, unitY * 11.0f);
		gloves.setSize(unitX * 1.5f, unitY * 1.5f);
		
		boots = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		boots.setPosition(unitX * 11.0f, unitY * 11.0f);
		boots.setSize(unitX * 1.5f, unitY * 1.5f);
		
		head.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getHelm() != null) {
					inventory.removeEquippedItem(inventory.getHelm());
				}
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
			}
		});
		
		leftHand.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getLeftHand() != null) {
					inventory.removeEquippedItem(inventory.getLeftHand());
				}
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
			}
		});
		
		rightHand.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getRightHand() != null) {
					inventory.removeEquippedItem(inventory.getRightHand());
				}
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
			}
		});
		
		body.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getArmor() != null) {
					inventory.removeEquippedItem(inventory.getArmor());
				}
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
			}
		});
		
		gloves.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getGloves() != null) {
					inventory.removeEquippedItem(inventory.getGloves());
				}
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
			}
		});
		
		boots.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getBoots() != null) {
					inventory.removeEquippedItem(inventory.getBoots());
				}
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
			}
		});
	}
	
	
	private void addMovementButtons() {
		stage.addActor(forward);
		stage.addActor(backward);
		stage.addActor(left);
		stage.addActor(right);
	}
	
	private void addSkillsAndAttackButtons() {
		stage.addActor(attack);
	}
	
	private void addInventoryButtons() {
		for (int col = 0; col < INVENTORY_COLUMNS; ++col) {
			for (int row = 0; row < INVENTORY_ROWS; ++row) {
				 Button button = inventoryButtons[col][row];
				 stage.addActor(button);
			}
		}
	}
	
	private void addEquipmentButtons() {
		stage.addActor(head);
		stage.addActor(leftHand);
		stage.addActor(rightHand);
		stage.addActor(body);
		stage.addActor(gloves);
		stage.addActor(boots);
	}
	
	
	private void removeMovementButtons() {
		forward.remove();
		backward.remove();
		left.remove();
		right.remove();
	}
	
	private void removeSkillsAndAttackButtons() {
		attack.remove();
	}
	
	private void removeInventoryButtons() {
		for (int col = 0; col < INVENTORY_COLUMNS; ++col) {
			for (int row = 0; row < INVENTORY_ROWS; ++row) {
				 Button button = inventoryButtons[col][row];
				 button.remove();
			}
		}
	}
	
	private void removeEquipmentButtons() {
		head.remove();
		leftHand.remove();
		rightHand.remove();
		body.remove();
		gloves.remove();
		boots.remove();
	}
	
	public void dispose() {
		skin.dispose();
	}
	
}
