
public class Shape {
	public static final int defaultWidth = 20;
	public static final int defaultHeight = 20;
	
	public boolean hidden;
	public boolean moveable;
	public int x;
	public int y;
	public int width;
	public int height;
	public String name;
	public String text;
	public Script script;
	public String imageName;

	public Shape(String shapeName, int xCord, int yCord) {
		script = new Script(this);
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
}
