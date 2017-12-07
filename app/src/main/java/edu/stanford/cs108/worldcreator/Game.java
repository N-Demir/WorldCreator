package edu.stanford.cs108.worldcreator;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Vector;

public class Game {
	/* Entryway into all current game related things */
	public static Game curGame;

	private static final String INITIAL_PAGE_NAME = "page1";
	private static final float INVENTORY_SPACING = 50.0f; //x and y spacing

	/* Private IVARS */
	private String gameName;
	private Vector<Page> pages;
	private Vector<Shape> inventory;
	private Page currentPage;
	private Shape currentShape;

	/**
	 * Fresh constructor. Sets up brand new fresh game with provided gameName
	 */
	public Game(String gameName) {
		this.gameName = gameName;
		pages = new Vector<Page>();
		inventory = new Vector<Shape>();
		currentPage = new Page(INITIAL_PAGE_NAME);
		//currentPage.addShape(new Shape("ball", 50, 50)); //DEBUGGING
		pages.add(currentPage);

		// TODO not sure this is the best way to construct a first object
		currentShape = new Shape("default", (float)0, (float)0);
	}

	/**
	 * Reading in from database constructor
	 * @param pageVec
//	 * @param initialInventory
	 * @param gameName
	 */
	public Game(Vector<Page> pageVec, String gameName) {
		pages = pageVec;
		inventory = new Vector<Shape>();
		this.gameName = gameName;
		currentPage = pages.elementAt(0);
		//TODO not sure this is the best way to construct a default first current objecy
		currentShape = new Shape("default", (float)0, (float)0);
	}

	public String getGameName() {return gameName;}
	public void setCurrentShape (Shape newShape){currentShape = newShape;}
	public Shape getCurrentShape (){return currentShape;}
	public Vector<Page> getPages(){return pages;}
	public String getCurPageName() {return currentPage.getName();}
	public Page getCurrentPage(){return currentPage;}
	
	public Page getPage(String name) {
		for(Page page: pages) if(page.getName().equals(name)) return page;
		return null;
	}

	public void addPage (Page p){
		pages.add(p);
	}

//	public MediaPlayer getSound(String name) {
//		int soundID = getResources().getIdentifier(name, null, null);
//		MediaPlayer mp = MediaPlayer.create(getActivityContext(), soundID);
//		return mp;
//	}
	
	public Shape getShape(String name) {
		for(Page page: pages) {
			for(Shape shape: page.getShapes()) {
				if(shape.getName() == name) return shape;
			}
		}
		return null;
	}
	
	public Vector<Shape> getInventory(){
		return inventory;
	}
	public void changePage(Page page) {
		currentPage = page;
	}


	public void addToInventory(Shape shape) {
		inventory.add(shape);
		Game.curGame.getCurrentPage().removeShape(shape);
	}
	public void removeFromInventory(Shape shape) {
		inventory.remove(shape);
		Game.curGame.getCurrentPage().addShape(shape);
	}


	public void drawPage(Canvas canvas) {
		currentPage.draw(canvas);
	}

	public void drawInventory(Canvas canvas) {
		float curX = INVENTORY_SPACING;
		for(Shape shape: inventory) {
			shape.setX(curX);
			shape.sety(PlayerGameView.height - PlayerGameView.SEPARATOR_HEIGHT + INVENTORY_SPACING/2.0f);
			shape.draw(canvas);
			curX += shape.getWidth() + INVENTORY_SPACING;
		}
	}
	
	
}
