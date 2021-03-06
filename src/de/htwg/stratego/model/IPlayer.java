package de.htwg.stratego.model;

import java.util.List;

public interface IPlayer {

	List<ICharacter> getCharacterList();
	String getCharacterListAsString();
	
	void addCharacter(ICharacter c);
	void addCharacters(int rank, int number);
	boolean removeCharacter(ICharacter c);
	ICharacter removeCharacter(int index);
	ICharacter getCharacter(int rank);
	boolean hasCharacter(int rank);
	
	void setName(String name);
	String getName();
	void setSymbol(String symbol);
	String getSymbol();
	boolean hasSetupFinished();
	void setSetupFinished(boolean setupFinished);
	boolean getSetupFinished();

	String toStringAll();
}
