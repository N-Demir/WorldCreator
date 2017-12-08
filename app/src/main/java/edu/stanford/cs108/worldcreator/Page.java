package edu.stanford.cs108.worldcreator;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.*;

public class Page {
	private String pName;
	private int pageID;
	private Vector<Shape> shapes; 
	
	public Page(String name) {
		this.pName = name; 
		shapes = new Vector<Shape>();
	}
	
	public String getName() {
		return pName; 
	}
	public void setName(String newName) {pName = newName;}
	
	public int removeShape(Shape s) {
		if (shapes.contains(s)) {
			shapes.remove(s);
			return 1;
		}
		return 0; 
	}

	public Shape getShape(String shapeName){
		for (Shape shape : shapes){
			if (shape.getName().equals(shapeName)) return shape;
		}
		return null;
	}
	
	public Vector<Shape> getShapes(){
		return shapes;
	}
	public void addShape(Shape s) {
		shapes.add(s); 
	}

	public Shape getShapeAtCoords(float x, float y) {
		// This has to be backwards so that we select the top shape

		for (int i = shapes.size() - 1; i >= 0; i--) {
			Shape shape = shapes.get(i);
			Log.d("MESSAGE", "getShapeAtCoords: "+ shape.getName() +" " + shape.isContained(x, y));
			if (shape.isContained(x, y)) return shape;
		}
		return null;
	}
	
	public void draw(Canvas canvas) {
		Log.d("MESSAGE", "draw: drawing current page");
		for(Shape shape: shapes) {
			Log.d("MESSAGE", "PAGE FOUND A SHAPE");
			shape.draw(canvas);
		}
	}
}
