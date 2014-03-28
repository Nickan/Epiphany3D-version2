package com.nickan.epiphany.model.characterstate;

import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.model.Character;

public class IdleState implements BaseState<Character> {
	private static final IdleState instance = new IdleState();
	
	private IdleState() {
		
	}

	@Override
	public void start(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Character entity, float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage(Character entity, Message message) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getStatus() {
		return "Idle State";
	}

	public static BaseState<Character> getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

}
