package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

public class Shape {
	private static final int defaultWidth = 20;
	private static final int defaultHeight = 20;

	static{}; //TODO: INITIALIZE PAINTS AND THINGS STATIC TO ALL SHAPE OBJECTS
	
	private boolean hidden;
	private boolean movable;
	private float x;
	private float y;
	private float width;
	private String scriptName;
	private float height;
	private String name;
	private String text;
	private Script script;
	private String imageName;
	private Page page;
	private boolean inventoryItem;

	public Shape(String sName){
		name = sName;
		hidden = false;
		movable = false;
		x = 10;
		y =10;
		height = defaultHeight;
		width = defaultWidth;
		imageName = "";
		text = "";
		scriptName = "";
		script = new Script("");
	}

	public Shape (String sName, float xCord, float yCord, float h, float w,
				  int move, int hide, String img,
				  String txt, String scrpt){
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
		scriptName = scrpt;
		script =  new Script(scrpt);
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
		scriptName = "";
	}
	
		public void executeOnClick() {
		Vector<Object>[] commands = script.getOnClickActions();
		executeCommands(commands);
	}
	
	public void executeOnEnter() {
		Vector<Object>[] commands = script.getOnEnterActions();
		executeCommands(commands);
	}
	
	public void executeOnDrop() {
		Vector<Object>[] commands = script.getOnDropActions();
		executeCommands(commands);
	}
	
	private void executeCommands(Vector<Object>[] commands) {
		Vector<Object> goToTargets = commands[0];
		for(Object page: goToTargets)  Game.curGame.changePage((Page) page);	
		
		Vector<Object> playTargets = commands[1];
		for(Object mp: playTargets) ((MediaPlayer) mp).play();
		
		Vector<Object> hideTargets = commands[2];
		for(Object shape: hideTargets) ((Shape) shape).setHidden(true);
		
		Vector<Object> showTargets = commands[3];
		for(Object shape: showTargets) ((Shape) shape).setHidden(false);
		
		//can't redraw without canvas to pass as parameter
		//Game.curGame.drawPage();
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
	public String getScriptName(){return scriptName;}
	public String getImage() { return imageName;}
	public boolean inInventory() { return inventoryItem;}
	public Page getPage() { return page;}
	
	public void setHidden(boolean bool) { hidden = bool;}
	public void setMovable(boolean bool) { movable = bool;}
	public void setX(float val) { x = val;}
	public void sety(float val) { y = val;}
	public void setWidth(float val) { width = val;}
	public void setHeight(float val) { height = val;}
	public void setName(String str) { name = str;}
	public void setText(String str) { text = str;}
	public void setImageName(String str) { imageName = str;}
	public void setScript(Script temp) { script = temp;}
	public void setInventoryStatus(boolean bool) { inventoryItem = bool;}
	public void setPage(Page temp) { page = temp;}

	public boolean isContained(float x, float y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
	}
	public void move(float xAmount, float yAmount) {
		this.x += xAmount;
		this.y += yAmount;
	}


	public void runScript_OnClick() {
		//TODO: IS this just supposed to be implemented in the script class? Thats fine but need stuff written
	}
	public void runScript_onDrop() {}
	public void runScript_onEnter() {} //TODO: necessary just for page 1 entering from game start

	
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
