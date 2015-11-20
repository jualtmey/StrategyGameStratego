package de.htwg.stratego.controller;

import java.util.ArrayList;
import java.util.List;

import de.htwg.stratego.model.Cell;
import de.htwg.stratego.model.Character;
import de.htwg.stratego.model.Field;
import de.htwg.stratego.model.impl.Flag;
import de.htwg.stratego.model.impl.Sergeant;
import de.htwg.stratego.util.observer.Observable;

public class StrategoController extends Observable {
	private GameStatus status = GameStatus.WELCOME;
	private Field field;
	
	private List<Character> characterListPlayer1;
	private List<Character> characterListPlayer2;
	
	private enum PlayerStatus {PLAYER_ONE_START, PLAYER_TWO_START, PLAYER_ONE_TURN, PLAYER_TWO_TURN};
	private PlayerStatus playerStatus = PlayerStatus.PLAYER_ONE_START;
	
	private static final int NUMBER_OF_SERGEANT = 8;
	private static final int NUMBER_OF_FLAG = 1;
	
	
	public StrategoController(int width, int height) {
		setField(width, height);
		initCharacterLists();
	}
	
	private void initCharacterLists() {
		characterListPlayer1 = new ArrayList<>();
		characterListPlayer2 = new ArrayList<>();
		// create Sergeant
//		addNumberOfChar(characterListPlayer1,
//						new Sergeant(Character.PLAYER_ONE),
//						numberOfSergeant);
//		addNumberOfChar(characterListPlayer2,
//				new Sergeant(Character.PLAYER_TWO),
//				numberOfSergeant);
		
		addToCharList(new Sergeant(Character.PLAYER_ONE),
					  new Sergeant(Character.PLAYER_TWO),
					  NUMBER_OF_SERGEANT);
		
		addToCharList(new Flag(Character.PLAYER_ONE),
				  new Flag(Character.PLAYER_TWO),
				  NUMBER_OF_FLAG);
		
		// OLD--------
//		for (int i = 0; i < numberOfSergeant; i++) {
//			characterListPlayer1.add(new Sergeant(Character.PLAYER_ONE));
//		}
//		// create Flag
//		characterListPlayer1.add(new Flag(Character.PLAYER_ONE));
	}
	
	private void addToCharList(Character ch1, Character ch2, int number){
	
		addNumberOfChar(characterListPlayer1,
				ch1,
				number);
		addNumberOfChar(characterListPlayer2,
			ch2,
			number);
	}
	
	private void addNumberOfChar(List<Character> charlist,
								 Character ch,
								 int number) {
		for (int i = 0; i < number; i++) {
			charlist.add(ch);
		}
	}
	
	
	public String toStringCharacterList(int player) {
		List<Character> characterList = null;
		if (player == Character.PLAYER_ONE) {
			characterList = characterListPlayer1;
		} else if (player == Character.PLAYER_TWO) {
			characterList = characterListPlayer2;
		} else {
			return  "?";
		}
		
		StringBuilder sb = new StringBuilder("|");
		for (Character c : characterList) {
			sb.append(c.getRank() + "|");
		}
		return sb.toString();
	}
	
	public void changePlayerSetup() {
		if (playerStatus == PlayerStatus.PLAYER_ONE_START) {
			playerStatus = PlayerStatus.PLAYER_TWO_START;
		} else if (playerStatus== PlayerStatus.PLAYER_TWO_START) {
			playerStatus = PlayerStatus.PLAYER_ONE_TURN;
		}
		notifyObservers();
	}
	
	public void changePlayerTurn() {
		if (playerStatus == PlayerStatus.PLAYER_ONE_TURN) {
			playerStatus = PlayerStatus.PLAYER_TWO_TURN;
		} else if (playerStatus == PlayerStatus.PLAYER_TWO_TURN) {
			playerStatus = PlayerStatus.PLAYER_ONE_TURN;
		}
	}
	
	public GameStatus getStatus() {
		return status;
	}
	
	public String toStringPlayerStatus() {
		if (playerStatus == PlayerStatus.PLAYER_ONE_START) {
			return "Set your characters, player 1!";
		} else if (playerStatus == PlayerStatus.PLAYER_TWO_START) {
			return "Set your characters, player 2!";
		} else if (playerStatus == PlayerStatus.PLAYER_ONE_TURN) {
			return "It's your turn, player 1!";
		} else if (playerStatus == PlayerStatus.PLAYER_TWO_TURN) {
			return "It's your turn, player 2!";
		}
		return null;
	}
	
	public Field getField() {
		return field;
	}
	
	public void setField(int width, int height) {
		this.field = new Field(width,height);
		//TODO: abfragen von illegalen gr��en -1 etc.S
	}
	
	public void fillField() {
		add(1, 1, 0);
		add(1, 2, 4);
	}
	
	public void moveChar(int fromX, int fromY, int toX,
			int toY) {
		//TODO: check is move inside of Field 
		//get Cells and get Characters
		Cell fromCell = field.getCell(fromX, fromY);
		Cell toCell = field.getCell(toX, toY);
		Character fromCharacter = fromCell.getCharacter();
		Character toCharacter = toCell.getCharacter();
		
		//Conditions of fromCharacter
		//TODO: is selected character != null ;
		if (fromCharacter == null) {
			notifyObservers();
			return;
		}
		
		//TODO: is Char moveable 
		if (!fromCharacter.isMoveable()) {
			notifyObservers();
			return;
		}
		
		//TODO: is character a char of the player
		if (fromCharacter.getPlayer() == Character.PLAYER_ONE) {
			if (!(playerStatus == PlayerStatus.PLAYER_ONE_TURN)) {
				notifyObservers();
				return;
			}
		} else if (fromCharacter.getPlayer() == Character.PLAYER_TWO) {
			if (!(playerStatus == PlayerStatus.PLAYER_TWO_TURN)) {
				notifyObservers();
				return;
			}
		} else {
			notifyObservers();
			return;
		}
		
		// correct range of move
		int dx = Math.abs(fromX - toX);
		int dy = Math.abs(fromY - toY);
		if (dx > 1 || dy > 1 || dx == dy) {
			//TODO
			notifyObservers();
			return;
		}
		
		//Conditions of toCharacter
		if (toCharacter == null) {
			// if Cell is empty move Character to new position
			fromCell.setCharacter(null);
			toCell.setCharacter(fromCharacter);
		} else {
			// if Cell is not empty fight with toCharacter
			// only if toCharacter.getPlayer() is not equal to fromCharacter.getPlayer() 
			if (toCharacter.getPlayer() == fromCharacter.getPlayer()) {
				notifyObservers();
				return;
			}
			fight(fromCell, toCell);
		}
		System.out.println("ende von move");
		
		changePlayerTurn();
		notifyObservers();
	}
	
	private void fight(Cell c1, Cell c2) {
		//TODO g�ltige Zellen �berpr�fen
		//TODO sind auf beiden Zellen Characters
		// get Character rank
		int r1 = c1.getCharacter().getRank();
		int r2 = c2.getCharacter().getRank();
		
		if (r1 > r2) {
			//success
			remove(c2.getX(),c2.getY());
		} else if (r1 < r2) {
			// lost
			remove(c1.getX(),c1.getY());
		} else {
			// equal both lose
			remove(c1.getX(),c1.getY());
			remove(c2.getX(),c2.getY());
			
		}
	}
	
	public void add(int x, int y, int rank) {
		List<Character> characterList = null;
		
		if (playerStatus == PlayerStatus.PLAYER_ONE_START) {
			characterList = characterListPlayer1;
		} else if (playerStatus == PlayerStatus.PLAYER_TWO_START) {
			characterList = characterListPlayer2;
		} else {
			//TODO
			return;
		}
		
		Character character = null;
		for (Character c: characterList) {
			if (c.getRank() == rank) {
				character = c;
			}
		}
		
		if (character == null) {
			return;
		}
		
		Cell cell = field.getCell(x, y);
		if (cell.getCharacter() != null) {
			return;
		}
		
		characterList.remove(character);
		cell.setCharacter(character);
		
		notifyObservers();
	}
	
	public void removeNotify(int x, int y) {
		remove(x, y);
		notifyObservers();
	}
	
	private Character remove(int x, int y) {
		List<Character> characterList = null;
		
//		if (playerStatus == PlayerStatus.PLAYER_ONE_START ||
//				playerStatus == PlayerStatus.PLAYER_ONE_TURN) {
//			characterList = characterListPlayer1;
//		} else if (playerStatus == PlayerStatus.PLAYER_TWO_START ||
//				playerStatus == PlayerStatus.PLAYER_TWO_TURN) {
//			characterList = characterListPlayer2;
//		} else {
//			//TODO
//			return null;
//		}
		
		Character c = field.getCell(x, y).getCharacter();
		
		if (c.getPlayer() == Character.PLAYER_ONE) {
			characterList = characterListPlayer1;
		} else if (c.getPlayer() == Character.PLAYER_TWO) {
			characterList = characterListPlayer2;
		}
		
		field.getCell(x, y).setCharacter(null);
		if (c != null) {
			characterList.add(c);
		}
		
		return c;
	}
	
	public String getFieldString() {
		return field.toString();
	}
	
}
