import java.util.*;

public class Page {
	private String pName;
	private int pageID;
	private ArrayList<Shape> shapes; 
	
	public Page(String name) {
		this.pName = name; 
		shapes = new ArrayList<Shape>();
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
	
	public void addShape(Shape s) {
		shapes.add(s); 
	}
	
	public void draw() {
		for(Shape shape: shapes) {
			shape.draw();
		}
	}
}
