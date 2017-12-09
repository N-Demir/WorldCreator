package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Vector;

public class Shape {
	private static final float OUTLINE_SELECTED_WIDTH = 10.0f;
	private static final int OUTLINE_SELECTED_COLOR = Color.GREEN;
	private static final Paint.Style OUTLINE_SELECTED_STYLE = Paint.Style.STROKE;
	private static final Paint outlineSelectedPaint;

	private static final int defaultWidth = 20;
	private static final int defaultHeight = 20;
	private static final int defaultFontSize = 30;

	static{
		outlineSelectedPaint = new Paint();
		outlineSelectedPaint.setColor(OUTLINE_SELECTED_COLOR);
		outlineSelectedPaint.setStyle(OUTLINE_SELECTED_STYLE);
		outlineSelectedPaint.setStrokeWidth(OUTLINE_SELECTED_WIDTH);
	} //TODO: INITIALIZE PAINTS AND THINGS STATIC TO ALL SHAPE OBJECTS


	private boolean hidden;
	private boolean movable;
	private float x;
	private float y;
	private float width;
	private String scriptText;
	private float height;
	private String name;
	private String text;
	private Script script;
	private String imageName;
	private Page page;
	private boolean inventoryItem;
	private int fontSize;

	public Shape(String sName){
		name = sName;
		hidden = false;
		movable = true;
		x = 10;
		y = 10;
		height = defaultHeight;
		width = defaultWidth;
		imageName = "";
		text = "";
		scriptText = "";
		fontSize = defaultFontSize;
		script = new Script("");
	}

	public Shape (String sName, float xCord, float yCord, float h, float w,
				  int move, int hide, String img,
				  String txt, String script, int fSize){
		name = sName;
		x = xCord;
		y = yCord;
		height = h;
		width = w;
		if (move == 0) movable = false;
		else movable = true;
		if (hide == 0) hidden = false;
		else hidden = true;
		imageName = img;
		text = txt;
		scriptText = script;
		fontSize = fSize;
		this.script =  new Script(script);
	}

	public Shape(String shapeName, float xCord, float yCord) {
		inventoryItem = false;
		name = shapeName;
		x = xCord;
		y = yCord;
		hidden = false;
		movable = true;
		text = "";
		imageName = "";
		width = defaultWidth;
		height = defaultHeight;
		script = new Script("");
		scriptText = "";
	}
	
	public void executeOnClick() {
        Log.d("MESSAGE", "executeOnClick: ");
        Vector<Vector<Object>> commands = script.getOnClickActions();
		executeCommands(commands);
	}
	
	public void executeOnEnter() {
        Log.d("MESSAGE", "executeOnEnter");
		Vector<Vector<Object>> commands = script.getOnEnterActions();
		executeCommands(commands);
	}
	
	public void executeOnDrop(Shape beingDropped) {
        Log.d("MESSAGE", "executeOnDrop: " + beingDropped.getName());
        Vector<Vector<Object>> commands = script.getOnDropActions(beingDropped);
        if(commands == null) Log.d("MESSAGE", "executeOnDrop: COMMANDS IS NULL");
        executeCommands(commands);
	}

	public boolean canDropOn(Shape beingDropped){
        return script.droppable(beingDropped);
    }
	
	private void executeCommands(Vector<Vector<Object>> commands) {
		Log.d("MESSAGE", "executeCommands: ");
		Vector<Object> playTargets = commands.elementAt(1);
		for(Object mp: playTargets) {
            if(mp != null) ((MediaPlayer) mp).start();
        }
		
		Vector<Object> hideTargets = commands.elementAt(2);
		for(Object shape: hideTargets){
			Log.d("MESSAGE", "executeCommands: HIDE: " + ((Shape)shape).getName());
            if(shape != null) ((Shape) shape).setHidden(true);
        }
		
		Vector<Object> showTargets = commands.elementAt(3);
		for(Object shape: showTargets){
			Log.d("MESSAGE", "executeCommands: SHOW: " + ((Shape)shape).getName());
            if(shape != null)((Shape) shape).setHidden(false);
		}

		Vector<Object> goToTargets = commands.elementAt(0);
		for(Object page: goToTargets){
			Log.d("MESSAGE", "executeCommands: GOTO: " + ((Page)page).getName());
			if(page != null) Game.curGame.changePage((Page) page);
		}
	}
	
	public boolean getHidden() { return hidden;}
	public boolean getMovable() { return movable;}
	public float getX() { return x;}
	public float getY() { return y;}
	public float getWidth() { return width;}
	public float getHeight() { return height;}
	public String getName() { return name;}
	public String getText() { return text;}
	public Script getScript() { return script;}
	public String getScriptText(){return scriptText;}
	public String getImage() { return imageName;}
	public boolean inInventory() { return inventoryItem;}
	public Page getPage() { return page;}
	public int getFontSize() { return fontSize;}

	public void setFontSize(int size){ fontSize = size;}
	public void setHidden(boolean bool) { hidden = bool;}
	public void setMovable(boolean bool) { movable = bool;}
	public void setX(float val) { x = val;}
	public void setY(float val) { y = val;}
	public void setWidth(float val) { width = val;}
	public void setHeight(float val) { height = val;}
	public void setName(String str) { name = str;}
	public void setText(String str) { text = str;}
	public void setImageName(String str) { imageName = str;}
	public void setScript(Script temp) { script = temp;}
	public void setInventoryStatus(boolean bool) { inventoryItem = bool;}
	public void setPage(Page temp) { page = temp;}
	public void setScriptText(String text) {
		scriptText = text;
		script = new Script(scriptText);
	}

	public boolean isContained(float x, float y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
	}
	public void move(float xAmount, float yAmount) {
		this.x += xAmount;
		this.y += yAmount;
	}
	
	public void draw(Canvas canvas, boolean editor) {
		if (hidden && !editor) return;
		if (PlayerGameView.drawOutline && PlayerGameView.selectedShapeName.equals(name)) {
			canvas.drawRect(x, y, x + width, y + height, outlineSelectedPaint);
		}
		Paint paint = null;
		if(!text.isEmpty()) {
			Log.d("MESSAGE", "draw: FONTSIZE: " + fontSize);
			paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setTextSize(fontSize);
			if(editor && hidden) paint.setAlpha(64);
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			width = bounds.width();
			height = bounds.height();
			canvas.drawText(text, x, y + height, paint);
		} else if(!imageName.isEmpty()) {
            Context context = MainActivity.curContext;
			int imageID = context.getResources().getIdentifier(imageName, "drawable",
					context.getPackageName());
			BitmapDrawable image = (BitmapDrawable) context.getResources().getDrawable(imageID,
					context.getTheme());
			if(editor && hidden) {
				paint = new Paint();
				paint.setAlpha(64);
			}
			canvas.drawBitmap(image.getBitmap(), null, new RectF(x, y, x + width, y + height), paint);
		} else {
			paint = new Paint(); //TODO: THIS SHOULD BE DONE ONCE AND STATICALLY FOR EFFICIENCY
			paint.setColor(Color.LTGRAY);
			if(editor && hidden) paint.setAlpha(64);
			canvas.drawRect(x, y, x + width, y + height, paint);
		}
	}
}
