package de.htwg.stratego.model.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.htwg.stratego.model.IField;

public class Field implements IField {

	private int width;
	private int height;

	private Cell[][] cells;
	
	@Inject
	public Field(@Named("fieldWidth") int width,
				 @Named("fieldWidth") int height) {
		this.width = width;
		this.height = height;

		// field initializing
		cells = new Cell[width][height];

		// each cell initialized
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				cells[x][y] = new Cell(x, y);
			}
		}
		
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Cell getCell(int x, int y) {
		return cells[x][y];
	}
	
	@Override
	public boolean equals(Object o) {
		Field field = (Field) o;
		if (width == field.getWidth() && height == field.getHeight()) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder lineSb = new StringBuilder("  +");
		StringBuilder mainSb = new StringBuilder("   ");		

		for (int i = 0; i < width; i++) {
			mainSb.append(" " + i + "  ");
			lineSb.append("---+");
		}
		lineSb.append("\n");
		mainSb.append("\n");
		String lineString = lineSb.toString();
		
		for (int j = 0; j < height; j++) {
			mainSb.append(lineString);
			mainSb.append(j + " ");
			for (int i = 0; i < width; i++) {
				mainSb.append("|" + cells[i][j]);
			}
			mainSb.append("|\n");
		}
		mainSb.append(lineString);
		
		return mainSb.toString();
	}

}
