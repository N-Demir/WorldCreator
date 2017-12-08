package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Vector;

public class Game {
	/* Entryway into all current game related things */
	public static Game curGame;

	public static final String INITIAL_PAGE_NAME = "page1";
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
        //currentShape = new Shape ("default", 10, 10, 200, 200, 1, 0, "carrot", "", "");
		//currentPage.addShape(currentShape);
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
		//currentShape =  new Shape ("default", 0, 0, 200.0f, 200.0f, 1, 1, "carrot", "", "");
	}

	public String getGameName() {return gameName;}
	public void setCurrentShape (Shape newShape){currentShape = newShape;}
	public Shape getCurrentShape (){return currentShape;}
	public Vector<Page> getPages(){return pages;}
	public String getCurPageName() {return currentPage.getName();}
	public Page getCurrentPage(){ return currentPage;}
	
	public Page getPage(String name) {
		for(Page page: pages) if(page.getName().equals(name)) return page;
		return null;
	}

	public void addPage (Page p){
		pages.add(p);
	}

	public MediaPlayer getSound(String name) {
		Context context = MainActivity.curContext;
		int soundID = context.getResources().getIdentifier(name, "raw", context.getPackageName());
		MediaPlayer mp = MediaPlayer.create(context, soundID);
		return mp;
	}
	
	public Shape getShape(String name) {
		for(Page page: pages) {
			for(Shape shape: page.getShapes()) {
				if(shape.getName().equals(name)) return shape;
			}
		}
		return null;
	}
	
	public Vector<Shape> getInventory(){
		return inventory;
	}

	public void changePage(Page page) {
		currentPage = page;
		for(Shape shape: page.getShapes()){
			shape.executeOnEnter();
		}
	}

	public void changePageEditor(Page page) {currentPage = page;}


	public void addToInventory(Shape shape) {
		Log.d("MESSAGE", "addtoI");
		inventory.add(shape);
		Game.curGame.getCurrentPage().removeShape(shape);
	}
	public void removeFromInventory(Shape shape) {
        Log.d("MESSAGE", "removefromI");
		inventory.remove(shape);
		Game.curGame.getCurrentPage().addShape(shape);
	}

	public Shape getShapeAtCoords(float x, float y) {
		Shape shape = currentPage.getShapeAtCoords(x, y); //TODO:Remove redundencies? or keep Modularity?
		if (shape != null) return shape;
		for (int i = inventory.size() - 1; i >= 0; i--) {
			shape = inventory.get(i);
			if (shape.isContained(x, y)) return shape;
		}
		return null;
	}

	public Shape getShapeUnder(float x, float y, Shape shape) {
		//TRICK: Remove shape from appropriate collection and search
		Shape underShape;
		boolean doAdd;
		if (y >= PlayerGameView.height - PlayerGameView.SEPARATOR_HEIGHT) {
			//Inside inventory
			doAdd = inventory.remove(shape);
			for (int i = inventory.size() - 1; i >= 0; i--) {
				underShape = inventory.get(i);
				if (underShape.isContained(x, y)) return underShape;
			}
			if (doAdd) inventory.add(shape);
		} else {
			//Inside regular page
			doAdd = currentPage.getShapes().remove(shape);
			underShape = currentPage.getShapeAtCoords(x, y);
			if (doAdd) currentPage.addShape(shape);
			if (underShape != null) return underShape;
		}
		return null;
	}

	public void drawPage(Canvas canvas) {
		currentPage.draw(canvas);
	}

	public void drawInventory(Canvas canvas) {
		float curX = INVENTORY_SPACING;
		for(Shape shape: inventory) {
			shape.setX(curX); //TODO:DOES THIS STUFF WORK
			shape.setY(PlayerGameView.height - PlayerGameView.SEPARATOR_HEIGHT + INVENTORY_SPACING / 2.0f);
			shape.draw(canvas);
			curX += shape.getWidth() + INVENTORY_SPACING;
		}
	}
	
	
}
