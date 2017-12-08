package edu.stanford.cs108.worldcreator;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Editor extends AppCompatActivity {
    private static final int TOAST_LENGTHS = Toast.LENGTH_SHORT;
    private static final int DEFAULT_FONT_SIZE = 30;
    SQLiteDatabase db;
    private int count = 2; //TODO: But what about database storage
    private int shapeCount =1;
    private int fontSize = DEFAULT_FONT_SIZE;
    public static Vector<String> imageNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        final Spinner pageSpinner = (Spinner) findViewById(R.id.page_spinner);
        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("MESSAGE", "onItemSelected: PAGE LISTENER ");
                String yew = pageSpinner.getSelectedItem().toString();
                Game.curGame.changePage(Game.curGame.getPage(yew));
                if (Game.curGame.getCurrentPage().getShapes().size() != 0) Game.curGame.setCurrentShape(Game.curGame.getCurrentPage().getShapes().elementAt(0));
                updateShapeSpinner();
                findViewById(R.id.EditorView).invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // I think we just do nothing now
            }
        });

        final Spinner shapeSpinner  = (Spinner) findViewById(R.id.shape_spinner);
        shapeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("MESSAGE", "onItemSelected: LISTENER");
                String shapeName = shapeSpinner.getSelectedItem().toString();
                Game.curGame.setCurrentShape(Game.curGame.getCurrentPage().getShape(shapeName));
                setShapeFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // I think we also do nothing here
            }
        });

        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null);
        updatePageSpinner();
        //setDefaultShapeFields(); //TODO:UNNECESSARY?
        setTitle("Editing: " + Game.curGame.getGameName());
    }

    public void onIncreaseFont(View view){
        fontSize += 2;
    }

    public void onDecreaseFont(View view){
        if(fontSize > 2) fontSize -= 2;
    }


 //TODO implement create
    public void onCreatePage(View view){
        EditText editText = (EditText) findViewById(R.id.pageName);
        String newGame = editText.getText().toString();
        String checker = newGame.toLowerCase();
        if (newGame.isEmpty() || Game.curGame.getPage(checker) != null) {
            Vector<String> myNames = new Vector<String>();
            for (Page page : Game.curGame.getPages()) {
                myNames.add(page.getName().toLowerCase());
            }
            for (int i = 1; i <= myNames.size() + 1; i++){
                newGame = "page" + i;
                if (!myNames.contains(newGame)) break;
            }
        }
        Game.curGame.changePage(new Page(newGame, ""));
        Game.curGame.addPage(Game.curGame.getCurrentPage());
        updatePageSpinner();
        editText.setText(newGame);
        findViewById(R.id.EditorView).invalidate();
    }

    public void onDeletePage(View view){
        String pageName = Game.curGame.getCurPageName();
        if (pageName.equals(Game.INITIAL_PAGE_NAME)) {
            Toast.makeText(getApplicationContext(), "Can't delete starting page " + Game.INITIAL_PAGE_NAME,
                    TOAST_LENGTHS).show();
            return;
        }
        for (Page p : Game.curGame.getPages()){
            if (pageName.equals(p.getName())){
                Game.curGame.getPages().remove(p);
                break;
            }
        }
        Game.curGame.changePage(Game.curGame.getPages().elementAt(Game.curGame.getPages().size() - 1));
        updatePageSpinner();
        findViewById(R.id.EditorView).invalidate();
    }

    public void onUpdatePage(View view) {
        String backgroundImageName = ((EditText)findViewById(R.id.backgroundImage)).getText().toString();
        if (imageNames.contains(backgroundImageName)) Game.curGame.getCurrentPage().setBackgroundImage(backgroundImageName);
        else if (!backgroundImageName.isEmpty()) Toast.makeText(getApplicationContext(),
                "Couldn't find background image with name: " + backgroundImageName, TOAST_LENGTHS).show();
        else Game.curGame.getCurrentPage().setBackgroundImage("fdsa"); //TODO WhaT??
        findViewById(R.id.EditorView).invalidate();
        if (Game.curGame.getCurPageName().equals(Game.INITIAL_PAGE_NAME)) {
            Toast.makeText(getApplicationContext(), "Can't rename starting page "
                    + Game.INITIAL_PAGE_NAME, TOAST_LENGTHS).show();
            return;
        }
        String newName = ((EditText)findViewById(R.id.pageName)).getText().toString();
        if (Game.curGame.getPage(newName) != null) {
            Toast.makeText(getApplicationContext(), "Page name " + newName
                    + " already in use", TOAST_LENGTHS).show();
            return;
        }
        Game.curGame.getCurrentPage().setName(newName);
        updatePageSpinner();
    }

    private void updatePageSpinner(){
        Vector<String> pageNames = new Vector<String>();
        for (Page cur : Game.curGame.getPages()) pageNames.add(cur.getName());
        Spinner spinner = (Spinner) findViewById(R.id.page_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pageNames);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(Game.curGame.getCurPageName()));
    }

    public void onCreateShape(View view) {
        String shapeName = (((EditText)findViewById(R.id.shapeName)).getText().toString());
        String checker = shapeName.toLowerCase();
        if (shapeName.isEmpty() || Game.curGame.getShape(checker) != null) { //TODO: DOES THIS WORK RIGHT WITH LOWERCASE?
            Set<String> allShapeNames = new HashSet<String>();
            for (Page p : Game.curGame.getPages())
                for (Shape shape : p.getShapes())
                    allShapeNames.add(shape.getName());

            for (int i = 1; i <= allShapeNames.size() + 1; i++) {
                shapeName = "shape" + i;
                if (!allShapeNames.contains(shapeName)) break;
            }
        }

        Game.curGame.setCurrentShape(new Shape(shapeName));
        Game.curGame.getCurrentPage().addShape(Game.curGame.getCurrentShape());
        updateShapeSpinner();
        Log.d("MESSAGE", "onCreateShape: FONTSIZE: " + fontSize);
    }

    public void onDeleteShape(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.shape_spinner);
        String  shapeName = spinner.getSelectedItem().toString();
        if (Game.curGame.getCurrentPage().getShapes().size() == 1){
            Game.curGame.getCurrentPage().removeShape(Game.curGame.getCurrentShape());
            Game.curGame.setCurrentShape(null);
            setDefaultShapeFields();
        } else {
            Game.curGame.getCurrentPage().removeShape(Game.curGame.getCurrentShape());
            Game.curGame.setCurrentShape(Game.curGame.getCurrentPage().getShapes().elementAt(0));
        }
        updateShapeSpinner();
        findViewById(R.id.EditorView).invalidate();
    }

    public void onUpdateShape(View view) {
        //read in all shape EditTexts and update curShape with their values
        Shape curShape = Game.curGame.getCurrentShape();
        if (curShape == null) {
            Toast.makeText(getApplicationContext(), "No selected shape", TOAST_LENGTHS).show();
            return;
        }
        Log.d("MESSAGE", Game.curGame.getCurrentShape().getName());
        String shapeName = ((EditText)findViewById(R.id.shapeName)).getText().toString();
        if (Game.curGame.getShape(shapeName) != null && !shapeName.equals(curShape.getName())) { //TODO:LOWERCASE
            Toast.makeText(getApplicationContext(), "Shape with name " + shapeName + " already exists",
                    TOAST_LENGTHS).show();
            return;
        } else if (shapeName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Can't update" + " shape with empty name", TOAST_LENGTHS).show();
            return;
        }
        curShape.setName(shapeName);
        curShape.setX(Float.parseFloat(((EditText)findViewById(R.id.xCord)).getText().toString()));
        curShape.setY(Float.parseFloat(((EditText)findViewById(R.id.yCord)).getText().toString()));
        curShape.setWidth(Float.parseFloat(((EditText) findViewById(R.id.width)).getText().toString()));
        curShape.setHeight(Float.parseFloat(((EditText) findViewById(R.id.height)).getText().toString()));
        String string = (String)((EditText) findViewById(R.id.imageName)).getText().toString();
        if (imageNames.contains(string)) curShape.setImageName(string);
        else {
            curShape.setImageName("");
            Toast.makeText(getApplicationContext(), "Couldn't find " + string + " image", TOAST_LENGTHS).show();
        }
        Log.d("MESSAGE", "onUpdateShape: FONTSIZE: " + fontSize);
        curShape.setFontSize(fontSize);
        curShape.setText(((EditText)findViewById(R.id.displayText)).getText().toString());
        curShape.setScriptText(((EditText)findViewById(R.id.scriptText)).getText().toString()); //TODO: Error checking toasts in here
        curShape.setMovable(((RadioButton)findViewById(R.id.movable)).isChecked());
        curShape.setHidden(((RadioButton)findViewById(R.id.notVisible)).isChecked());
        
        updateShapeSpinner();

        findViewById(R.id.EditorView).invalidate(); //TODO:IMPLEMENT EVERYWHERE?
    }

    public void setShapeFields(){
        Shape shape = Game.curGame.getCurrentShape();
        ((EditText) findViewById(R.id.xCord)).setText(Float.toString(shape.getX()));
        ((EditText) findViewById(R.id.yCord)).setText(Float.toString(shape.getY()));
        ((EditText) findViewById(R.id.width)).setText(Float.toString(shape.getWidth()));
        ((EditText) findViewById(R.id.height)).setText(Float.toString(shape.getHeight()));
        ((EditText) findViewById(R.id.shapeName)).setText(shape.getName());
        ((EditText) findViewById(R.id.imageName)).setText(shape.getImage());
        ((EditText) findViewById(R.id.displayText)).setText(shape.getText());
        ((EditText) findViewById(R.id.scriptText)).setText(shape.getScriptText());
        fontSize = shape.getFontSize();

        if (shape.getHidden()) ((RadioGroup) findViewById(R.id.visibleGroup)).check(R.id.notVisible);
        else ((RadioGroup) findViewById(R.id.visibleGroup)).check(R.id.isVisible);
        if (shape.getMovable()) ((RadioGroup) findViewById(R.id.moveGroup)).check(R.id.movable);
        else ((RadioGroup) findViewById(R.id.moveGroup)).check(R.id.notMovable);

        findViewById(R.id.EditorView).invalidate();
    }

    private int toInt(boolean input){
        if (input) return 1;
        return 0;
    }

    public void updateShapeSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.shape_spinner);
        Vector<String> shapeNames = new Vector<String>();
        for (Shape cur : Game.curGame.getCurrentPage().getShapes()) shapeNames.add(cur.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shapeNames);
        spinner.setAdapter(adapter);
        if(shapeNames.size() == 0 || Game.curGame.getCurrentShape() == null) return;
        spinner.setSelection(adapter.getPosition(Game.curGame.getCurrentShape().getName()));
    }

    public void setDefaultShapeFields() {
        ((EditText) findViewById(R.id.xCord)).setText("");
        ((EditText) findViewById(R.id.yCord)).setText("");
        ((EditText) findViewById(R.id.width)).setText("");
        ((EditText) findViewById(R.id.height)).setText("");
        ((EditText) findViewById(R.id.shapeName)).setText("");
        ((EditText) findViewById(R.id.imageName)).setText("");
        ((EditText) findViewById(R.id.displayText)).setText("");
        ((EditText) findViewById(R.id.scriptText)).setText("");
        fontSize = DEFAULT_FONT_SIZE;

        ((RadioGroup)findViewById(R.id.visibleGroup)).clearCheck();
        ((RadioGroup) findViewById(R.id.moveGroup)).clearCheck();
    }

    ///////////////////////////DATABASE RELATED STUFF/////////////////////////
    public void saveGame(View view){
        clearDataBase();
        updateDataBase();
    }
    private void clearDataBase(){
        String sDelete = "DELETE FROM shapes WHERE game='" + Game.curGame.getGameName() + "'";
        db.execSQL(sDelete);
        Log.d("MESSAGE" , "COMP1");
        String pDelete = "DELETE FROM pages WHERE game='" + Game.curGame.getGameName() + "'";
        db.execSQL(pDelete);
        Log.d("MESSAGE" , "COMP2");
        String gDelete = "DELETE FROM games WHERE name='" + Game.curGame.getGameName() + "'";
        db.execSQL(gDelete);
        Log.d("MESSAGE" , "COMP3");
    }

    private void updateDataBase(){
        for (Page page : Game.curGame.getPages()){
            addPage(page);
            for (Shape shape : page.getShapes()) addShape(page, shape);
        }
        String gameStr = "INSERT INTO games VALUES ('" + Game.curGame.getGameName() + "',NULL);";
        db.execSQL(gameStr);
    }

    private void addPage(Page page){
        Log.d("MESSAGER", page.getName());
        String pageStr = "INSERT INTO pages VALUES " +
                "('" + page.getName() + "','" + page.getBackgroundImage() + "','" + Game.curGame.getGameName() + "',NULL);";
        db.execSQL(pageStr);
    }

    private  void addShape(Page page, Shape shape){
        Log.d("MESSAGER", shape.getName());
        String shapeStr = "INSERT INTO shapes VALUES " +
                "('" +shape.getName() + "','" + Game.curGame.getGameName() + "','"
                + page.getName() + "','" + shape.getX() + "','" + shape.getY()
                + "','" + shape.getHeight() + "','" + shape.getWidth() + "','" + toInt(shape.getMovable())
                + "','" + toInt(shape.getHidden())+ "','" + shape.getImage() + "','" + shape.getScriptText() + "','" +
                shape.getText() + "','" + shape.getFontSize() +"',NULL);";
        db.execSQL(shapeStr);
    }
}
