package de.htwg.stratego.controller.impl;

public interface GameState {

	void changeState();
	
	boolean isMoveAllowed();
	boolean isAddAllowed();
	boolean isSwapAllowed();
	boolean isRemoveAllowed();
	boolean isFinishAllowed();

	String getName();
	
	String toStringPlayerStatus();

}
