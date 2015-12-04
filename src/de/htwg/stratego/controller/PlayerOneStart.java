package de.htwg.stratego.controller;

import de.htwg.stratego.model.Character;
import de.htwg.stratego.model.impl.Player;

public class PlayerOneStart extends AbstractGameState {

	public PlayerOneStart(StrategoController sc) {
		super(sc);
	}

	@Override
	public Player getCurrentPlayer() {
		return sc.getPlayerOne();
	}
	
	@Override
	public String getFieldString() {
		return sc.getField().getFieldString(sc.getPlayerOne());
	}

	@Override
	public Character remove(int x, int y) {
		return null;
	}

	@Override
	public void add(int x, int y, int rank) {
		
	}

	@Override
	public void moveChar(int fromX, int fromY, int toX, int toY) {
		
	}

	@Override
	public String toStringPlayerStatus() {
		return "Set your characters, player 1!";
	}

	@Override
	public void changeState() {
		sc.setState(new PlayerTwoStart(sc));
	}

}
