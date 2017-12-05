package edu.stanford.cs108.worldcreator;

import android.graphics.Canvas;

import java.util.Vector;

public class Game {
	public static Game curGame;

	private Vector<Page> pages;
	private Vector<Shape> inventory;
	private Page currentPage;
	private String gameName;
	private int nextInventoryXPos = 50;
	private static final int inventoryYPos = 500;
	
	public Game(Vector<Page> pageVec, Vector<Shape> initialInventory, String gameName) {
		pages = pageVec;
		inventory = initialInventory;
		this.gameName = gameName;
		//TODO: currentPage = get page1 from pages
	}

	public String getGameName() {return gameName;}
	
	public Vector<Shape> getInvetory(){
		return inventory;
	}
	
	public void changePage(Page page) {
		currentPage = page;
	}
	
	public void addToInventory(Shape shape, int x, int y) {
		inventory.add(shape);
		shape.setInventoryStatus(true);
		Page page = shape.getPage();
		page.removeShape(shape);
		shape.setPage(null);
		moveShape(shape, nextInventoryXPos + 50, inventoryYPos);
		nextInventoryXPos += 50 + shape.getWidth();
	}

	public void removeFromInventory(Shape shape, int x, int y) {
		inventory.remove(shape);
		currentPage.addShape(shape);
		moveShape(shape, x, y);
	}

	public void moveShape(Shape shape, int x, int y) {
		shape.setX(x);
		shape.sety(y);
	}

	public void drawPage(Canvas canvas) {
		currentPage.draw(canvas);
	}

	public void drawInventory(Canvas canvas) {
		for(Shape shape: inventory) {
			shape.draw(canvas);
		}
	}
}
