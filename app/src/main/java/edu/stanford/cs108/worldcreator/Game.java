package edu.stanford.cs108.worldcreator;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Vector;

public class Game {
	/* Entryway into all current game related things */
	public static Game curGame;

	private static final String INITIAL_PAGE_NAME = "page1";

	private float nextInventoryXPos = 50; //TODO: FIX THESE
	private static final int inventoryYPos = 500;

	/* Private IVARS */
	private String gameName;
	private Vector<Page> pages;
	private Vector<Shape> inventory;
	private Page currentPage;

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
		currentPage = getPage("page1");
		Log.d("MESSAGE", currentPage.getName());
	}

	public String getGameName() {return gameName;}
	public Vector<Page> getPages(){return pages;}
	public String getCurPageName() {return currentPage.getName();}
	
	public Page getPage(String name) {
		for(Page page: pages) {
			if(page.getName().equals(name)){
				Log.d("MESSAGE", "found page");
				return page;
			}
			Log.d("MESSAGE", "Page not found");
		}
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
	
	public Vector<Shape> getInvetory(){
		return inventory;
	}
	
	public void changePage(Page page) {
		currentPage = page;
	}
	
	public void addToInventory(Shape shape, float x, float y) {
		inventory.add(shape);
		shape.setInventoryStatus(true);
		Page page = shape.getPage();
		page.removeShape(shape);
		shape.setPage(null);
		moveShape(shape, nextInventoryXPos + 50, inventoryYPos);
		nextInventoryXPos += 50 + shape.getWidth();
	}

	public void removeFromInventory(Shape shape, float x, float y) {
		inventory.remove(shape);
		currentPage.addShape(shape);
		moveShape(shape, x, y);
		nextInventoryXPos = 0;
		for(Shape s: inventory){
			moveShape(s, nextInventoryXPos + 50, inventoryYPos);
			nextInventoryXPos += 50 + s.getWidth();
		}
	}

	public void moveShape(Shape shape, float x, float y) {
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
