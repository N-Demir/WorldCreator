package edu.stanford.cs108.worldcreator;

import android.graphics.Canvas;
import android.media.MediaPlayer;

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
	
	public void draw(Canvas canvas) {
		for(Shape shape: shapes) {
			shape.draw(canvas);
		}
	}
}
