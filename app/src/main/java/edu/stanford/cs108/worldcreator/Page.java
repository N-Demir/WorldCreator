package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.*;

public class Page {
	private String pName;
	private String backgroundImage;
	private int pageID;
	private Vector<Shape> shapes; 
	
	public Page(String name, String imageName) {
		this.pName = name;
		backgroundImage = imageName;
		shapes = new Vector<Shape>();
	}

	public String getBackgroundImage() { return backgroundImage;}
	public void setBackgroundImage(String imageName) { backgroundImage = imageName;}
	public String getName() {
		return pName; 
	}
	public void setName(String newName) { pName = newName;}
	
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
			if (shape.isContained(x, y)) return shape;
		}
		return null;
	}
	
	public void draw(Canvas canvas, boolean editor) {
		if(!backgroundImage.equals("")){
			Context context = MainActivity.curContext;
			int imageID = context.getResources().getIdentifier(backgroundImage, "drawable",
					context.getPackageName());
			BitmapDrawable image = (BitmapDrawable) context.getResources().getDrawable(imageID,
					context.getTheme());
			if(editor) canvas.drawBitmap(image.getBitmap(), null, new RectF(0, 0, EditorGameView.width, EditorGameView.height), null);
			else canvas.drawBitmap(image.getBitmap(), null, new RectF(0, 0, PlayerGameView.width, PlayerGameView.height - PlayerGameView.SEPARATOR_HEIGHT), null);
		} else canvas.drawColor(Color.WHITE);
		for(Shape shape: shapes) {
			shape.draw(canvas, editor);
		}
	}
}
