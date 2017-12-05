import android.graphics.Canvas;

public class Shape {
	private static final int defaultWidth = 20;
	private static final int defaultHeight = 20;
	
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

	public Shape(String shapeName, int xCord, int yCord, Page location) {
		script = new Script(this);
		inventoryItem = false;
		page = location;
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
	public int getWdith() { return width;}
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
			//print text
		} else if(imageName != "") {
			//draw image
		} else {
			//draw grey rect
		}
	}
}
