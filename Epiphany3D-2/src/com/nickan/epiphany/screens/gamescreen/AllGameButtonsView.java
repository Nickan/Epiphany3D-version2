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
import com.nickan.epiphany.model.characterstate.MovingState.Movement;
import com.nickan.epiphany.model.inventory.Inventory;
import com.nickan.epiphany.model.inventory.Item;
import com.nickan.epiphany.screens.gamescreen.HeadsUpDisplay.PauseWindow;
import com.nickan.epiphany.screens.gamescreen.HeadsUpDisplay.SubScreenMode;
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
	private static final int INVENTORY_ROWS = 4;
	
	// Needs to be a field, as needed for registering listener for buttons
	float inventoryX;
	float inventoryY;
	float inventoryButtonWidth;
	float inventoryButtonHeight;

	Button[][] inventoryButtons = new Button[INVENTORY_COLUMNS][INVENTORY_ROWS];
	
	Button showStatus;
	Button showItems;
	
	Button use;
	Button cancel;
	Button discard;
	
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
		
		initializePauseSelection(unitX, unitY);
		
		initializeItemOptionButtons(unitX, unitY);
		
		setGameButtons();
	}
	
	private void setGameButtons() {
		world.currentState = State.GAME;
		world.stopCameraRotation = false;
		
		screen.setGameController();
		
		stage.addActor(pause);
		addMovementButtons();
		addSkillsAndAttackButtons();
		
		resume.remove();
		showStatus.remove();
		showItems.remove();
		removeInventoryButtons();
		removeEquipmentButtons();
	}
	
	private void setPauseButtons() {
		world.currentState = State.PAUSE;
		world.stopCameraRotation = false;
		
		screen.setPauseController();
		
		stage.addActor(resume);
		stage.addActor(showStatus);
		stage.addActor(showItems);
		
		pause.remove();
		removeMovementButtons();
		removeSkillsAndAttackButtons();
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
		pause.setPosition(0, unitY * 15);
		pause.setSize(unitX * 1.5f, unitY * 1.5f);
		
		pause.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				setPauseButtons();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				world.stopCameraRotation = true;
			}
		});
	}
	
	private void initializeResumeButton(float unitX, float unitY) {
		resume = new Button(skin.getDrawable("resumenormal"), skin.getDrawable("resumepressed"));
		resume.setPosition(0, unitY * 15);
		resume.setSize(unitX * 1.5f, unitY * 1.5f);
		
		resume.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				setGameButtons();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				world.stopCameraRotation = true;
			}
		});
	}
	
	private void initializeInventoryButtons(float unitX, float unitY) {
		inventoryButtonWidth = unitX * 2.2f;
		inventoryButtonHeight = unitY * 2.2f;
		inventoryX = unitX * 12.5f;
		inventoryY = unitY * 4.2f;
		
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
						showInventoryItemDesc(inventory.getItems()[row][col]);
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
		float equipmentWidth = unitX * 2.2f;
		float equipmentHeight = unitY * 2.2f;
		
		float posX = unitX * 5f;
		float posY = unitY * 10.5f;
		
		head = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		head.setPosition(posX + (equipmentWidth * 0), posY + (equipmentHeight * 0));
		head.setSize(equipmentWidth, equipmentHeight);
		
		leftHand = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		leftHand.setPosition(posX + (equipmentWidth * 0), posY + (equipmentHeight * -1.5f));
		leftHand.setSize(equipmentWidth, equipmentHeight);
		
		rightHand = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		rightHand.setPosition(posX + (equipmentWidth * -1.5f), posY + (equipmentHeight * -1.5f));
		rightHand.setSize(equipmentWidth, equipmentHeight);
		
		body = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		body.setPosition(posX + (equipmentWidth * 1.5f), posY + (equipmentHeight * -1.5f));
		body.setSize(equipmentWidth, equipmentHeight);
		
		gloves = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		gloves.setPosition(posX + (equipmentWidth * -0.75f), posY + (equipmentHeight * -3f));
		gloves.setSize(equipmentWidth, equipmentHeight);
		
		boots = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		boots.setPosition(posX + (equipmentWidth * 0.75f), posY + (equipmentHeight * -3f));
		boots.setSize(equipmentWidth, equipmentHeight);
		
		head.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getHelm() != null) {
					inventory.removeEquippedItem(inventory.getHelm());
				}
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) { }	
			public void touchDragged(InputEvent event, float x, float y, int pointer) { }
		});
		
		leftHand.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getLeftHand() != null) {
					inventory.removeEquippedItem(inventory.getLeftHand());
				}
				return true;
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
		});
		
		body.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getArmor() != null) {
					inventory.removeEquippedItem(inventory.getArmor());
				}
				return true;
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
		});
		
		boots.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				Inventory inventory = world.player.inventory;
				if (inventory.getBoots() != null) {
					inventory.removeEquippedItem(inventory.getBoots());
				}
				return true;
			}
		});
	}
	
	/**
	 * Initialize the selection buttons showing the equipment, attributes, etc. One selection at a time
	 * @param unitX
	 * @param unitY
	 */
	private void initializePauseSelection(float unitX, float unitY) {
		float buttonWidth = unitX * 1.5f;
		float buttonHeight = unitX * 1.5f;
		
		showStatus = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		showStatus.setPosition(unitX * 3f, unitY * 15);
		showStatus.setSize(buttonWidth, buttonHeight);
		
		showItems = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		showItems.setPosition(unitX * 6f, unitY * 15);
		showItems.setSize(buttonWidth, buttonHeight);
		
		showStatus.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				setShowStatus();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
			}
		});
		
		showItems.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				setShowItems();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int button, int pointer) {
				
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				
			}
		});
	}
	
	private void initializeItemOptionButtons(float unitX, float unitY) {
		float buttonSizeX = unitX * 2f;
		float buttonSizeY = unitX * 0.75f;
		use = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		use.setSize(buttonSizeX, buttonSizeY);
		use.setPosition(unitX * 2, unitY * 1.5f);
		
		cancel = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		cancel.setSize(buttonSizeX, buttonSizeY);
		cancel.setPosition(unitX * 5, unitY * 1.5f);
		
		discard = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		discard.setSize(buttonSizeX, buttonSizeY);
		discard.setPosition(unitX * 8, unitY * 1.5f);
	}
	
	
	private void showInventoryItemDesc(Item item) {
		screen.hud.itemToShowDesc = item;
		screen.hud.setSubScreen1(SubScreenMode.SHOW_ITEM_DESC);
		
		addItemOptionButtons();
		removeEquipmentButtons();
	}

	private void setShowStatus() {
		removeInventoryButtons();
		removeEquipmentButtons();
		removeItemOptionButtons();
		
		screen.hud.setPauseWindow(PauseWindow.SHOW_STATUS);
		screen.hud.setSubScreen1(SubScreenMode.NONE);
		screen.hud.setSubScreen2(SubScreenMode.NONE);
	}
	
	private void setShowItems() {
		addInventoryButtons();
		addEquipmentButtons();
		
		removeItemOptionButtons();
		
		screen.hud.setPauseWindow(PauseWindow.SHOW_ITEMS);
		screen.hud.setSubScreen1(SubScreenMode.NONE);
		screen.hud.setSubScreen2(SubScreenMode.NONE);
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
	
	private void addItemOptionButtons() {
		stage.addActor(use);
		stage.addActor(cancel);
		stage.addActor(discard);
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
	
	private void removeItemOptionButtons() {
		use.remove();
		cancel.remove();
		discard.remove();
	}
	
	public void dispose() {
		skin.dispose();
		stage.dispose();
	}
	
}
