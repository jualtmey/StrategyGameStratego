package de.htwg.stratego.controller.rules;

import de.htwg.stratego.controller.impl.AbstractStrategoController;
import de.htwg.stratego.controller.state.GameState;
import de.htwg.stratego.model.IPlayer;

public interface IRuleSystem {

	boolean verifyAdd(int x, int y, int rank, IPlayer player, AbstractStrategoController strategoController);
	boolean verifySwap(int fromX, int fromY, int toX, int toY, IPlayer player, AbstractStrategoController strategoController);
	boolean verifyRemove(int x, int y, IPlayer player, GameState gameState);
	boolean verifyMove(int fromX, int fromY, int toX, int toY, IPlayer player, GameState gameState);
	
	String message();
	
}
