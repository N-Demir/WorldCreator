package edu.stanford.cs108.worldcreator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

public class Shape {
	private static final int defaultWidth = 20;
	private static final int defaultHeight = 20;

	static{}; //TODO: INITIALIZE PAINTS AND THINGS STATIC TO ALL SHAPE OBJECTS
	
	private boolean hidden;
	private boolean moveable;
	private int x;
	private int y;
	private int width;
	private int height;
	private String name;
	private String text;
	private Script script;
	private String imageName;
	private Page page;
	private boolean inventoryItem;

	public Shape (String sName, int xCord, int yCord, int h, int w,
				  int move, int hide, String img,
				  String txt, String scrpt){
		name = sName;
		x = xCord;
		y = yCord;
		height = h;
		width = w;
		if (move == 0) moveable = false;
		else moveable = true;
		if (hide == 0) hidden = false;
		else hidden = true;
		imageName = img;
		text = txt;
		script =  new Script(scrpt);
	}

	public Shape(String shapeName, int xCord, int yCord) {
		inventoryItem = false;
		name = shapeName;
		x = xCord;
		y = yCord;
		hidden = false;
		moveable = true;
		text = "";
		imageName = "";
		width = defaultWidth;
		height = defaultHeight;
	}
	
	public boolean getHidden() { return hidden;}
	public boolean getMoveable() { return moveable;}
	public int getX() { return x;}
	public int getY() { return y;}
	public int getWidth() { return width;}
	public int getHeight() { return height;}
	public String getName() { return name;}
	public String getText() { return text;}
	public Script getScript() { return script;}
	public String getImage() { return imageName;}
	public boolean inInventory() { return inventoryItem;}
	public Page getPage() { return page;}
	
	public void setHidden(boolean bool) { hidden = bool;}
	public void setMoveable(boolean bool) { moveable = bool;}
	public void setX(int val) { x = val;}
	public void sety(int val) { y = val;}
	public void setWidth(int val) { width = val;}
	public void setHeight(int val) { height = val;}
	public void setName(String str) { name = str;}
	public void setText(String str) { text = str;}
	public void setImageName(String str) { imageName = str;}
	public void setScript(Script temp) { script = temp;}
	public void setInventoryStatus(boolean bool) { inventoryItem = bool;}
	public void setPage(Page temp) { page = temp;}
	
	public void draw(Canvas canvas) {
		if(text != "") {
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setTextSize(16);
			canvas.drawText(text, x, y, paint);
			canvas.drawRect(x,y,width,height,paint);
		} else if(imageName != "") {
			//TODO: DOES THIS WORK
			Context context = MainActivity.curContext;
			int imageID = context.getResources().getIdentifier(imageName, "drawable",
					context.getPackageName());
			BitmapDrawable image = (BitmapDrawable) context.getResources().getDrawable(imageID,
					context.getTheme());
			canvas.drawBitmap(image.getBitmap(), x, y, null); //TODO: If shape is not visible and in editor then use opaque paint
		} else {
			/* Default is a grey rectangle*/
			Paint paint = new Paint(); //TODO: THIS SHOULD BE DONE ONCE AND STATICALLY FOR EFFICIENCY

			paint.setColor(Color.LTGRAY);
			canvas.drawRect(x, y, x + width, y + height, paint);
			//canvas.drawRect(50.0f, 50.0f, 200.0f, 200.0f, paint);
			//Log.d("DEBUG: ", "I GOT HEREEEE");
		}
	}
}
