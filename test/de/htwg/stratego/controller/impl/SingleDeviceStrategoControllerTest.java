package de.htwg.stratego.controller.impl;

import org.junit.BeforeClass; //erstellt nur eine Klasse und verwendet diese weiter
import org.junit.Test;

import de.htwg.stratego.controller.state.GameState;
import de.htwg.stratego.controller.state.impl.PlayerStart;
import de.htwg.stratego.controller.state.impl.PlayerTurn;
import de.htwg.stratego.model.ICell;
import de.htwg.stratego.model.ICharacter;
import de.htwg.stratego.model.IField;
import de.htwg.stratego.model.IPlayer;
import de.htwg.stratego.model.impl.Field;
import de.htwg.stratego.model.impl.PlayerFactory;
import de.htwg.stratego.model.impl.Rank;
import junit.framework.TestCase;

public class SingleDeviceStrategoControllerTest extends TestCase {
	private SingleDeviceStrategoController sc;
	private IField field;

	private IPlayer playerOne;
	private IPlayer playerTwo;
	
	private GameState playerOneStart;
	private GameState playerTwoStart;
	private GameState playerOneTurn;
	private GameState playerTwoTurn;

	@BeforeClass
	public void setUp() {
		sc = new SingleDeviceStrategoController(new Field(10, 10), new PlayerFactory(), null);
		field = sc.getIField();

		playerOne = sc.getPlayerOne();
		playerTwo = sc.getPlayerTwo();
		
		playerOneStart = new PlayerStart(playerOne, sc);
		playerOneTurn = new PlayerTurn(playerOne, sc);
		playerTwoStart = new PlayerStart(playerTwo, sc);
		playerTwoTurn = new PlayerTurn(playerTwo, sc);

		// add characters for player one
		sc.setState(playerOneStart);
		sc.addWithoutRule(5, 1, Rank.FLAG);
		sc.addWithoutRule(1, 0, Rank.SERGEANT);

		// add characters for player two
		sc.setState(playerTwoStart);
		sc.setCurrentPlayer(1);
		sc.addWithoutRule(2, 1, Rank.FLAG);
		sc.addWithoutRule(0, 2, Rank.SERGEANT);
		sc.addWithoutRule(5, 2, Rank.SERGEANT);
	}

	@Test
	public void testReset() {
		sc.reset();
		
		assertEquals(sc.getCurrentPlayer(), sc.getPlayer()[0]);
		for (int x = 0; x < field.getWidth(); x++) {
			for (int y = 0; y < field.getHeight(); y++) {
				assertFalse(sc.getIField().getCell(x, y).containsCharacter());
			}
		}
	}
	
	@Test
	public void testGetField() {
		assertEquals(field, sc.getIField());
	}

	@Test
	public void testGetFieldString() {
		assertEquals(field.toString(), sc.getFieldString());
	}

	@Test
	public void testUndo() {
		sc.setState(playerOneStart);
		sc.add(9, 0, 2);
		sc.undo();
		assertFalse(sc.getIField().getCell(9, 0).containsCharacter());
		assertEquals(sc.getStatusString(), "Undo.");
	}
	
	@Test
	public void testMove() {
		// illegal state, move not allowed
		sc.setState(playerOneStart);
		sc.setCurrentPlayer(0);
		assertFalse(sc.move(1, 0, 2, 0));
		assertFalse(sc.getIField().getCell(2, 0).containsCharacter());
		assertEquals("Move is not allowed.",
				sc.getStatusString());

		// correct move
		sc.setState(playerOneTurn);
		sc.setCurrentPlayer(0);
		assertTrue(sc.move(1, 0, 1, 1));
		assertTrue(sc.getIField().getCell(1, 1).containsCharacter());

		// wrong move
		sc.setState(playerTwoTurn);
		sc.setCurrentPlayer(1);
		assertFalse(sc.move(0, 2, 0, 5));
//		assertEquals("Your move was not possible. Try again.",
//				sc.getStatusString());

		// correct move, game over, player one win
		sc.setState(playerOneTurn);
		sc.setCurrentPlayer(0);
		assertTrue(sc.move(1, 1, 2, 1));
		assertEquals("GAME OVER!", sc.getStatusString());

		// correct move, game over, player two win
		sc.setCurrentPlayer(1);
		sc.setState(playerTwoTurn);
		assertTrue(sc.move(5, 2, 5, 1));
		assertEquals("GAME OVER!", sc.getStatusString());
	}

	@Test
	public void testAdd() {
		// illegal state, add not allowed
		sc.setState(playerOneTurn);
		assertFalse(sc.add(9, 9, Rank.BOMB));
		assertFalse(sc.getIField().getCell(9, 9).containsCharacter());

		// add is not in proper zone
		sc.setState(playerOneStart);
		sc.setCurrentPlayer(0);
		assertFalse(sc.add(4, 8, 2));
		
		sc.setState(playerTwoStart);
		sc.setCurrentPlayer(1);
		assertFalse(sc.add(4, 2, 2));
		
		// character not in player list
		assertFalse(sc.add(9, 9, Rank.FLAG));
		assertFalse(sc.getIField().getCell(9, 9).containsCharacter());

		// cell contains already a character
		sc.setState(playerOneStart);
		sc.setCurrentPlayer(0);
		assertFalse(sc.add(1, 0, Rank.BOMB));
		assertEquals(sc.getIField().getCell(1, 0).getCharacter().getRank(),
				Rank.SERGEANT);
		
		// cell not passable
		assertFalse(sc.add(2, 4, Rank.BOMB));
		assertFalse(sc.getIField().getCell(2, 4).containsCharacter());

		// correct add
		sc.setState(playerTwoStart);
		sc.setCurrentPlayer(1);
		assertTrue(sc.add(9, 9, Rank.BOMB));
		assertEquals(sc.getIField().getCell(9, 9).getCharacter().getRank(),
				Rank.BOMB);
	}

	@Test
	public void testSwap() {
		sc.setCurrentPlayer(0);
		
		// illegal state, swap not allowed
		sc.setState(playerOneTurn);
		assertFalse(sc.swap(5, 1, 1, 0));

		// swap not in proper zone
		assertFalse(sc.swap(5, 1, 9, 9));
		assertFalse(sc.containsCharacter(9, 9));

		// cell contains no character
		sc.setState(playerOneStart);
		assertFalse(sc.swap(9, 9, 8, 8));
		
		// cells are euqal
		assertFalse(sc.swap(5, 1, 5, 1));
		
		// one or both character do not belong to current player
		assertFalse(sc.swap(5, 1, 2, 1));
		assertFalse(sc.swap(0, 2, 2, 1));
		
		// correct swap
		assertTrue(sc.swap(5, 1, 1, 0));
		assertEquals(sc.getCharacter(5, 1).getRank(), Rank.SERGEANT);
		assertEquals(sc.getCharacter(1, 0).getRank(), Rank.FLAG);
		
		assertTrue(sc.swap(5, 1, 1, 1));
		assertEquals(sc.getCharacter(1, 1).getRank(), Rank.SERGEANT);
		assertFalse(sc.containsCharacter(5, 1));
	}
	
	@Test
	public void testRemove() {
		// illegal state, remove not allowed
		sc.setState(playerOneTurn);
		sc.setCurrentPlayer(0);
		assertFalse(sc.remove(5, 1));
		assertTrue(sc.getIField().getCell(5, 1).containsCharacter());

		// character belongs not to current player
		sc.setState(playerOneStart);
		sc.setCurrentPlayer(0);
		assertFalse(sc.remove(0, 2));
		assertTrue(sc.getIField().getCell(0, 2).containsCharacter());

		// cell contains no character
		sc.setState(playerOneStart);
		sc.setCurrentPlayer(0);
		assertFalse(sc.remove(9, 9));

		// correct remove
		assertTrue(sc.remove(5, 1));
		assertFalse(sc.getIField().getCell(5, 1).containsCharacter());
	}

	@Test
	public void testLost() {
		sc.setState(playerOneStart);
		sc.setCurrentPlayer(0);
		sc.remove(5, 1);
		assertTrue(sc.lost(sc.getCurrentPlayer()));
	}

	@Test
	public void testToggleVisibilityOfCharacters() {
		sc.toggleVisibilityOfCharacters(sc.getCurrentPlayer(), true);

		for (int y = 0; y < field.getHeight(); y++) {
			for (int x = 0; x < field.getWidth(); x++) {
				ICell cell = field.getCell(x, y);
				if (!cell.containsCharacter()) {
					continue;
				}
				ICharacter character = cell.getCharacter();
				if (character.belongsTo(sc.getCurrentPlayer())) {
					assertTrue(character.isVisible());
				} else {
					assertFalse(character.isVisible());
				}
			}
		}
	}

	@Test
	public void testSetVisibilityOfAllCharacters() {
		sc.setVisibilityOfAllCharacters(false);

		for (int y = 0; y < field.getHeight(); y++) {
			for (int x = 0; x < field.getWidth(); x++) {
				ICell cell = field.getCell(x, y);
				if (!cell.containsCharacter()) {
					continue;
				}
				assertFalse(cell.getCharacter().isVisible());
			}
		}
	}

	@Test
	public void testToStringPlayerStatus() {
		sc.setState(playerOneStart);
		assertEquals(sc.getPlayerStatusString(),
				playerOneStart.toStringPlayerStatus());
	}

	@Test
	public void testToStringCharacterList() {
		assertEquals(sc.getCharacterListString(sc.getCurrentPlayer()), sc
				.getCurrentPlayer().getCharacterListAsString());
	}
	
	@Test
	public void testFinished() {
		sc.setState(playerOneStart);
		sc.setCurrentPlayer(0);
		sc.finish();
		assertTrue(sc.getGameState() instanceof PlayerStart);
	}
	
	@Test
	public void testGetFieldWidth() {
		assertEquals(sc.getFieldWidth(), 10);
	}
	
	@Test
	public void testGetFieldHeight() {
		assertEquals(sc.getFieldHeight(), 10);
	}

}
