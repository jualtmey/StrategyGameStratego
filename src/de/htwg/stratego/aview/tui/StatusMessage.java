package de.htwg.stratego.aview.tui;

import java.util.HashMap;
import java.util.Map;

import de.htwg.stratego.controller.GameStatus;

public class StatusMessage {

	public static final Map<GameStatus, String> textMap = new HashMap<>();
	
	private StatusMessage() { }

	static {
		textMap.put(GameStatus.WELCOME, "Welcome to HTWG Stratego!");
//		textMap.put(GameStatus.PLAYER_ONE_START, "Set your characters, player 1!");
//		textMap.put(GameStatus.PLAYER_TWO_START, "Set your characters, player 2!");
//		textMap.put(GameStatus.PLAYER_ONE_TURN, "It's your turn, player 1!");
//		textMap.put(GameStatus.PLAYER_TWO_TURN, "It's your turn, player 2!");
	}
	
}
