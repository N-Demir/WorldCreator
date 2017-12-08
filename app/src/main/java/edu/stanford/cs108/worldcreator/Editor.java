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

import java.util.Vector;

public class Editor extends AppCompatActivity {
    SQLiteDatabase db;
    private int count = 2; //TODO: But what about database storage
    private int shapeCount =1;

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



 //TODO Allows me to create the same page twice and it inherits its  shape Objects
    public void onCreatePage(View view){
        EditText editText = (EditText) findViewById(R.id.pageName);
        String newGame = editText.getText().toString();

        int prevPage = count - 1; //TODO: THIS NEEDS TO BE CHANGED
        if (newGame.equals("") || newGame.equals("page" + prevPage)){
            newGame = "page" + count;
            count++;
        }
        if (Game.curGame.getPage(newGame) != null) return; //TODO: TOAST!!!!

        Game.curGame.changePage(new Page(newGame));
        Game.curGame.addPage(Game.curGame.getCurrentPage());
        updatePageSpinner();
        editText.setText(newGame);
        findViewById(R.id.EditorView).invalidate();
    }

    //TODO There is a bug when you try to delete the first item in the list when there are other items Could be the array adapter stuff\
    //TODO Determine if root page vant be deleted
    public void onDeletePage(View view){
        String pageName = Game.curGame.getCurPageName();
        if (pageName.equals(Game.INITIAL_PAGE_NAME)) return;
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

    public void onRenamePage(View view) {
        if (Game.curGame.getCurPageName().equals(Game.INITIAL_PAGE_NAME)) return;
        String newName = ((EditText)findViewById(R.id.pageName)).getText().toString();
        if (Game.curGame.getPage(newName) != null) return; //TODO:TOAST!!!!
        Game.curGame.getCurrentPage().setName(newName);
        updatePageSpinner();
    }

    // TODO Figure out how to set the selected spinner item
    private void updatePageSpinner(){
        Vector<String> pageNames = new Vector<String>();
        for (Page cur : Game.curGame.getPages()) pageNames.add(cur.getName());
        Spinner spinner = (Spinner) findViewById(R.id.page_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pageNames);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(Game.curGame.getCurPageName()));
    }

    // TODO It also crashes on the script object creation //Nikita: does this still happen Russ?
    public void onCreateShape(View view) {
        //TODO IS this necessarY?
        /*Spinner pages = (Spinner) findViewById(R.id.page_spinner);
        String currentPage = pages.getSelectedItem().toString();
        Game.curGame.changePage(Game.curGame.getPage(currentPage));*/

        int prevNum = shapeCount- 1;
        String shapeName = (((EditText)findViewById(R.id.shapeName)).getText().toString());
        if (shapeName.isEmpty() || shapeName.equals("shape" + prevNum)){
            shapeName = "shape" +  shapeCount; //TODO:BETTER WAY TO DO THIS USING THE VECTOR OF SHAPES
            shapeCount++;
        }
        if (Game.curGame.getShape(shapeName) != null) return; //TODO:TOAST!!!!!

        Game.curGame.setCurrentShape(new Shape(shapeName));
        Game.curGame.getCurrentPage().addShape(Game.curGame.getCurrentShape());
        updateShapeSpinner();
        //Figure out what default shape name to give it, base that on number of shapes?
        //update that field with it, create new shape, add it to page shapes, call onUpdate to set its fields
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
<<<<<<< HEAD
        updateShapeSpinner();
        findViewById(R.id.EditorView).invalidate();

    } //TODO:IMPLEMENTTTTTT
=======
    }
>>>>>>> Nikita's-workspace

    public void onUpdateShape(View view) {
        //read in all shape EditTexts and update curShape with their values
        Shape curShape = Game.curGame.getCurrentShape();
        Log.d("MESSAGE", Game.curGame.getCurrentShape().getName() );
        if (curShape == null) return;
        Log.d("MESSAGE", Game.curGame.getCurrentShape().getName() );
        String shapeName = ((EditText)findViewById(R.id.shapeName)).getText().toString();
        if (Game.curGame.getShape(shapeName) != null && !shapeName.equals(curShape.getName())) return;
        curShape.setName(shapeName);
        curShape.setX(Float.parseFloat(((EditText)findViewById(R.id.xCord)).getText().toString()));
        curShape.setY(Float.parseFloat(((EditText)findViewById(R.id.yCord)).getText().toString()));
        curShape.setWidth(Float.parseFloat(((EditText) findViewById(R.id.width)).getText().toString()));
        curShape.setHeight(Float.parseFloat(((EditText) findViewById(R.id.height)).getText().toString()));
        curShape.setImageName(((EditText)findViewById(R.id.imageName)).getText().toString());
        curShape.setText(((EditText)findViewById(R.id.displayText)).getText().toString());
        curShape.setScript(new Script(((EditText)findViewById(R.id.scriptText)).getText().toString()));

        curShape.setMovable(((RadioButton)findViewById(R.id.movable)).isChecked());
        curShape.setHidden(((RadioButton)findViewById(R.id.notVisible)).isChecked());
        
        updateShapeSpinner();

        findViewById(R.id.EditorView).invalidate(); //TODO:IMPLEMENT EVERYWHERE?
    }

    public void setShapeFields(){
        Log.d("MESSAGE", "setShapeFields: RESETING FIELDS");
        Shape shape = Game.curGame.getCurrentShape();
        ((EditText) findViewById(R.id.xCord)).setText(Float.toString(shape.getX()));
        ((EditText) findViewById(R.id.yCord)).setText(Float.toString(shape.getY()));
        ((EditText) findViewById(R.id.width)).setText(Float.toString(shape.getWidth()));
        ((EditText) findViewById(R.id.height)).setText(Float.toString(shape.getHeight()));
        ((EditText) findViewById(R.id.shapeName)).setText(shape.getName());
        ((EditText) findViewById(R.id.imageName)).setText(shape.getImage());
        ((EditText) findViewById(R.id.displayText)).setText(shape.getText());
        ((EditText) findViewById(R.id.scriptText)).setText(shape.getScript().getScriptString());

        if (shape.getHidden()) ((RadioGroup) findViewById(R.id.visibleGroup)).check(R.id.notVisible);
        else ((RadioGroup) findViewById(R.id.visibleGroup)).check(R.id.isVisible);
        if (shape.getMovable()) ((RadioGroup) findViewById(R.id.moveGroup)).check(R.id.movable);
        else ((RadioGroup) findViewById(R.id.moveGroup)).check(R.id.notMovable);

        findViewById(R.id.EditorView).invalidate();
    }


    /*private void createShape(String name, String xStr, String yStr, String height, String width, boolean visible, boolean toMove){
        Shape newShape = new Shape(name, toFloat(xStr), toFloat(yStr), toFloat(height), toFloat(width), toInt(toMove), toInt(visible), "","","");
        Game.curGame.getCurrentPage().addShape(newShape);
        Game.curGame.setCurrentShape(newShape);
        onUpdateShape(null); //TODO:Is this an issue?
    }*/

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
        String pageStr = "INSERT INTO pages VALUES " +
                "('" + page.getName() + "','" + Game.curGame.getGameName() + "',NULL);";
        db.execSQL(pageStr);
    }

    private  void addShape(Page page, Shape shape){
        String shapeStr = "INSERT INTO shapes VALUES " +
                "('" +shape.getName() + "','" + Game.curGame.getGameName() + "','"
                + page.getName() + "','" + shape.getX() + "','" + shape.getY()
                + "','" + shape.getHeight() + "','" + shape.getWidth() + "','" + toInt(shape.getMovable())
                + "','" + toInt(shape.getHidden())+ "','" + shape.getImage() + "','" + shape.getScriptName() + "','" + shape.getText() + "',NULL);";
        db.execSQL(shapeStr);
    }
}
