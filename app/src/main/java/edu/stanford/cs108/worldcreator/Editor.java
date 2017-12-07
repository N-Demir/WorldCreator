package edu.stanford.cs108.worldcreator;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Vector;

public class Editor extends AppCompatActivity {
    SQLiteDatabase db;
    private Page p;
    private static int count = 2;
    private static int shapeCount =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        final Spinner spinner = (Spinner) findViewById(R.id.page_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("MESSAGE", "boobs");
                String yew = spinner.getSelectedItem().toString();
                Game.curGame.changePage(Game.curGame.getPage(yew));
                updateShapeSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("MESSAGE", "fuck");
            }
        });
        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null); //TODO: BUGGY? BAD STYLE?
        updateSpinner();
        setTitle("Editing: " + Game.curGame.getGameName());
    }

    public void createPage(View view){
        EditText editText = (EditText) findViewById(R.id.npage);
        String newGame = editText.getText().toString();
        int prevPage = count - 1;
        if (newGame.equals("") || newGame.equals("page" + prevPage)){
            newGame = "page" + count;
            editText.setText(newGame);
            count++;
        }
        Game.curGame.changePage(new Page(newGame));
        Game.curGame.addPage(Game.curGame.getCurrentPage());
        updateSpinner();
    }

    // TODO Figure out how to set the selected spinner item
    private void updateSpinner(){
        Vector<String> pageNames = new Vector<String>();
        for (Page cur : Game.curGame.getPages()) pageNames.add(cur.getName());
        Spinner spinner = (Spinner) findViewById(R.id.page_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pageNames);
        spinner.setAdapter(adapter);
    }

    //TODO There is a bug when you try to delete the first item in the list when there are other items Could be the array adapter stuff\
    //TODO Determine if root page vant be deleted
    public void deletePage(View view){
        Spinner spinner = (Spinner) findViewById(R.id.page_spinner);
        String pageName = spinner.getSelectedItem().toString();
        if (Game.curGame.getPages().size() == 1) return;
        for (Page p : Game.curGame.getPages())if (pageName.equals(p.getName())) Game.curGame.getPages().remove(p);
        Game.curGame.changePage(Game.curGame.getPages().elementAt(0));
        updateSpinner();
    }

    private boolean checkEmpty(String input){
        return input.equals("");
    }

    // TODO there is no way to get a script image or text label It also crashes on the script object creation
    public void newShape (View view){
        Spinner pages = (Spinner) findViewById(R.id.page_spinner);
        String currentPage = pages.getSelectedItem().toString();
        Game.curGame.changePage(Game.curGame.getPage(currentPage));
        String xStr = ((EditText) findViewById(R.id.xCord)).getText().toString();
        String yStr = ((EditText) findViewById(R.id.yCord)).getText().toString();
        String widthStr = ((EditText) findViewById(R.id.width)).getText().toString();
        String heightStr = ((EditText) findViewById(R.id.height)).getText().toString();
        String shapeName  = ((EditText) findViewById(R.id.shapeName)).getText().toString();
        boolean moveable = ((RadioButton) findViewById(R.id.moveable)).isChecked();
        boolean visible = ((RadioButton) findViewById(R.id.isVisible)).isChecked();
        int prevNum = shapeCount- 1;
        if (checkEmpty(shapeName) || shapeName.equals("shape" + prevNum)){
            shapeName = "shape" +  shapeCount;
            shapeCount++;
        }
        createShape (shapeName, xStr, yStr, heightStr, widthStr, visible, moveable);
    }
    private void createShape(String name, String xStr, String yStr, String height, String width, boolean visible, boolean toMove){
        Shape newShape = new Shape(name, toFloat(xStr), toFloat(yStr), toFloat(height), toFloat(width), toInt(toMove), toInt(visible), "","","");
        Game.curGame.getCurrentPage().addShape(newShape);
        Game.curGame.setCurrentShape(newShape);
        updateShapeSpinner();
    }

    private float toFloat(String input){
        if (!checkEmpty(input)) Float.parseFloat(input);
        return 0;
    }

    private int toInt(boolean input){
        if (input) return 1;
        return 0;
    }

    private void updateShapeSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.shape_spinner);
        Vector<String> shapeNames = new Vector<String>();
        for (Shape cur : Game.curGame.getCurrentPage().getShapes()) shapeNames.add(cur.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shapeNames);
        spinner.setAdapter(adapter);
    }

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
        Log.d("MESSAGE" , Game.curGame.getGameName());
        for (Page page : Game.curGame.getPages()){
            Log.d("MESSAGE",page.getName());
            addPage(page);
            for (Shape shape : page.getShapes()) addShape(page, shape);
        }
        String gameStr = "INSERT INTO games VALUES ('" + Game.curGame.getGameName() + "',NULL);";
        db.execSQL(gameStr);
    }

    private void addPage(Page page){
        String pageStr = "INSERT INTO pages VALUES " +
                "('" + page.getName() + "','" + Game.curGame + "',NULL);";
        db.execSQL(pageStr);
    }

    private  void addShape(Page page, Shape shape){
        Log.d("MESSAGE", shape.getName());
        String shapeStr = "INSERT INTO shapes VALUES " +
                "('" +shape.getName() + "','" + Game.curGame + "','"
                + page.getName() + "','" + shape.getX() + "','" + shape.getY()
                + "','" + shape.getHeight() + "','" + shape.getWidth() + "','" + toInt(shape.getMoveable())
                + "','" + toInt(shape.getHidden())+ "','" + shape.getImage() + "','" + shape.getScriptName() + "','" + shape.getText() + "',NULL);";
        db.execSQL(shapeStr);
    }
}
