package de.htwg.stratego.controller.state.impl;

import de.htwg.stratego.controller.impl.AbstractStrategoController;
import de.htwg.stratego.controller.state.GameState;
import de.htwg.stratego.model.EGameState;
import de.htwg.stratego.model.IPlayer;

public class PlayerTurn implements GameState {

	private AbstractStrategoController sc;
	private IPlayer player;
	
	public PlayerTurn(IPlayer player, AbstractStrategoController sc) {
		this.sc = sc;
		this.player = player;
	}

	@Override
	public boolean isMoveAllowed() {
		return true;
	}

	@Override
	public boolean isAddAllowed() {
		return false;
	}
	
	@Override
	public boolean isSwapAllowed() {
		return false;
	}

	@Override
	public boolean isRemoveAllowed() {
		return false;
	}

	@Override
	public boolean isFinishAllowed() {
		return false;
	}

	@Override
	public String toStringPlayerStatus() {
		return "It's your turn, player " + player.getName() + "!";
	}

	@Override
	public void changeState() {
		sc.setState(new PlayerTransfer(sc.nextChangePlayer(), sc));
		sc.setVisibilityOfAllCharacters(false);
	}

	@Override
	public String getName() {
		return "turn";
	}

	@Override
	public EGameState getEGameState() {
		return EGameState.PLAYER_TURN;
	}

}
