import java.util.Vector;

public class Game {
	public static Game curGame;

	private Vector<Page> pages;
	private Vector<Shape> inventory;
	private Page currentPage;
	
	public Game(Vector<Page> pageVec, Vector<Shape> initialInventory) {
		pages = pageVec;
		inventory = initialInventory;
	}
	
	public Vector<Shape> getInvetory(){
		return inventory;
	}
	
	public void changePage(Page page) {
		currentPage = page;
	}
	
	public void addToInventory(Shape shape, int x, int y) {
		inventory.add(shape);
		shape.setInventoryStatus(true);
		Page page = shape.getPage();
		page.removeShape(shape);
		shape.setPage(null);
		moveShape(shape, x, y);
	}
	
	public void removeFromInventory(Shape shape, int x, int y) {
		inventory.remove(shape);
		currentPage.addShape(shape);
		moveShape(shape, x, y);
	}
	
	public void moveShape(Shape shape, int x, int y) {
		shape.setX(x);
		shape.sety(y);
	}
	
	public void onDraw() {
		currentPage.draw();
		for(Shape shape: inventory) {
			shape.draw();
		}
	}
}
