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
import java.util.StringTokenizer;
import java.util.Vector;

public class Editor extends AppCompatActivity {
    public static final int TOAST_LENGTHS = Toast.LENGTH_SHORT;
    private static final int DEFAULT_FONT_SIZE = 30;
    private static final int FONT_SIZE_CHANGE = 2;
    SQLiteDatabase db;
    private int fontSize = DEFAULT_FONT_SIZE;
    public static Vector<String> imageNames;

    private String prevPages;
    private String prevShapes;
    private String prevCurShape;
    private String prevCurPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        setPrevGameState();

        final Spinner pageSpinner = (Spinner) findViewById(R.id.page_spinner);
        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("MESSAGE", "onItemSelected: PAGE LISTENER ");
                String yew = pageSpinner.getSelectedItem().toString();
                Game.curGame.changePage(Game.curGame.getPage(yew));
                if (Game.curGame.getCurrentPage().getShapes().size() != 0) Game.curGame.setCurrentShape(Game.curGame.getCurrentPage().getShapes().elementAt(0));
                ((EditText)findViewById(R.id.pageName)).setText(Game.curGame.getCurPageName());
                ((EditText)findViewById(R.id.backgroundImage)).setText(Game.curGame.getCurrentPage().getBackgroundImage());
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

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.curContext = getApplicationContext();
        MainActivity.loadingFlag = false;
    }

    public void onIncreaseFont(View view){
        fontSize += FONT_SIZE_CHANGE;
        Toast.makeText(getApplicationContext(), "Font size increased", TOAST_LENGTHS).show();
    }
    public void onDecreaseFont(View view){
        if(fontSize > FONT_SIZE_CHANGE) fontSize -= FONT_SIZE_CHANGE;
        Toast.makeText(getApplicationContext(), "Font size decreased", TOAST_LENGTHS).show();
    }

    private boolean checkPageNameChars(String name) {
        if (name.contains(" ") || name.contains(",")) {
            Toast.makeText(getApplicationContext(), "Page name cannot have spaces or commas",
                    TOAST_LENGTHS).show();
            return false;
        }
        return true;
    }
    private boolean checkPageNameLowerCase(String name) {return true;}//TODO:IMPLEMENT: return true if it is a dup


    public void onCreatePage(View view){
        EditText editText = (EditText) findViewById(R.id.pageName);
        String newPage = editText.getText().toString();
        if (!checkPageNameChars(newPage)) return;
        boolean duplicate = checkPageNameLowerCase(newPage);
        if (newPage.isEmpty() || duplicate) {
            Vector<String> myNames = new Vector<String>();
            for (Page page : Game.curGame.getPages()) {
                myNames.add(page.getName().toLowerCase());
            }
            for (int i = 1; i <= myNames.size() + 1; i++){
                newPage = "page" + i;
                if (!myNames.contains(newPage)) break;
            }
        }
        Game.curGame.changePage(new Page(newPage, ""));
        Game.curGame.addPage(Game.curGame.getCurrentPage());
        updatePageSpinner();
        editText.setText(newPage);
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
        else Game.curGame.getCurrentPage().setBackgroundImage("");
        findViewById(R.id.EditorView).invalidate();

        String newName = ((EditText)findViewById(R.id.pageName)).getText().toString();
        if (newName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Can't update page with empty name", TOAST_LENGTHS).show();
            return;
        } else {
            if (newName.equals(Game.curGame.getCurPageName())) return;
            if (Game.curGame.getCurPageName().equals(Game.INITIAL_PAGE_NAME)) {
                Toast.makeText(getApplicationContext(), "Can't rename starting page "
                        + Game.INITIAL_PAGE_NAME, TOAST_LENGTHS).show();
                return;
            }
            if (!checkPageNameChars(newName)) return;
            if (checkPageNameLowerCase(newName)) {
                Toast.makeText(getApplicationContext(), "Page name " + newName
                        + " already in use", TOAST_LENGTHS).show();
                return;
            }
            Game.curGame.getCurrentPage().setName(newName);
        }
        updatePageSpinner();
    }

    private void updatePageSpinner(){
        Vector<String> pageNames = new Vector<String>();
        for (Page cur : Game.curGame.getPages()) pageNames.add(cur.getName());
        Spinner spinner = (Spinner) findViewById(R.id.page_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pageNames);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(Game.curGame.getCurPageName()));
        ((EditText) findViewById(R.id.pageName)).setText(Game.curGame.getCurrentPage().getName());
    }

    private boolean checkShapeNameChars(String name) {
        if (name.contains(" ") || name.contains(",")) {
            Toast.makeText(getApplicationContext(), "Shape name cannot have spaces or commas",
                    TOAST_LENGTHS).show();
            return false;
        }
        return true;
    }
    private boolean checkShapeNameLowerCase(String name) {return true;} //TODO:IMPLEMENT :true if dup

    public void onCreateShape(View view) {
        String shapeName = (((EditText)findViewById(R.id.shapeName)).getText().toString());
        if (!checkShapeNameChars(shapeName)) return;

        boolean duplicate = checkShapeNameLowerCase(shapeName);
        if (shapeName.isEmpty() || duplicate) {
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
        if (Game.curGame.getCurrentPage().getShapes().isEmpty()) {
            Toast.makeText(getApplicationContext(), "No shape selected", TOAST_LENGTHS).show();
        } else if (Game.curGame.getCurrentPage().getShapes().size() == 1){
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
        boolean success = true;
        Shape curShape = Game.curGame.getCurrentShape();
        if (curShape == null) {
            Toast.makeText(getApplicationContext(), "No selected shape", TOAST_LENGTHS).show();
            return;
        }
        //Log.d("MESSAGE", Game.curGame.getCurrentShape().getName());
        String shapeName = ((EditText)findViewById(R.id.shapeName)).getText().toString();

        if (shapeName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Can't update shape with empty name", TOAST_LENGTHS).show();
            return;
        } else if (!checkShapeNameChars(shapeName)) {
            return;
        } else if (Game.curGame.getShape(shapeName) != null && !shapeName.equals(curShape.getName())) { //TODO:LOWERCASE DUPLICATES
            Toast.makeText(getApplicationContext(), "Shape with name " + shapeName + " already exists",
                    TOAST_LENGTHS).show();
            return;
        }

        Float x = 0.0f;
        Float y = 0.0f;
        Float width = 0.0f;
        Float height = 0.0f;

        String xStr = ((EditText)findViewById(R.id.xCord)).getText().toString();
        if (xStr.isEmpty()) {
            success = false;
            Toast.makeText(getApplicationContext(), "X must have a float value", TOAST_LENGTHS).show();
        } else x = Float.parseFloat(xStr);

        String yStr = ((EditText)findViewById(R.id.yCord)).getText().toString();
        if (yStr.isEmpty()) {
            success = false;
            Toast.makeText(getApplicationContext(), "Y must have a float value", TOAST_LENGTHS).show();
        } else y = Float.parseFloat(yStr);

        String widthStr = ((EditText) findViewById(R.id.width)).getText().toString();
        if (widthStr.isEmpty()) {
            success = false;
            Toast.makeText(getApplicationContext(), "Width must have a float value", TOAST_LENGTHS).show();
        } else width = Float.parseFloat(widthStr);

        String heightStr = ((EditText) findViewById(R.id.height)).getText().toString();
        if (heightStr.isEmpty()) {
            success = false;
            Toast.makeText(getApplicationContext(), "Height must have a float value", TOAST_LENGTHS).show();
        } else height = Float.parseFloat(heightStr);

        String imageName = ((EditText) findViewById(R.id.imageName)).getText().toString();
        if (!imageName.isEmpty() && !imageNames.contains(imageName)){
            success = false;
            Toast.makeText(getApplicationContext(), "Couldn't find " + imageName + " image", TOAST_LENGTHS).show();
        }
        String textString = ((EditText)findViewById(R.id.displayText)).getText().toString();
        String setScript = ((EditText)findViewById(R.id.scriptText)).getText().toString();
        Script temp = new Script(setScript);
        if (!temp.getValid()) success = false;

        if (success) {
            //Set everything in current shape
            curShape.setName(shapeName);
            curShape.setX(x);
            curShape.setY(y);
            curShape.setWidth(width);
            curShape.setHeight(height);
            curShape.setImageName(imageName);
            curShape.setText(textString);
            curShape.setScript(temp);
            curShape.setFontSize(fontSize);
            curShape.setMovable(((RadioButton)findViewById(R.id.movable)).isChecked());
            curShape.setHidden(((RadioButton)findViewById(R.id.notVisible)).isChecked());

            updateShapeSpinner();
            findViewById(R.id.EditorView).invalidate();
        }
        //Log.d("MESSAGE", "onUpdateShape: FONTSIZE: " + fontSize);
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
        ((EditText) findViewById(R.id.xCord)).setText("0.0");
        ((EditText) findViewById(R.id.yCord)).setText("0.0");
        ((EditText) findViewById(R.id.width)).setText("0.0");
        ((EditText) findViewById(R.id.height)).setText("0.0");
        ((EditText) findViewById(R.id.shapeName)).setText("");
        ((EditText) findViewById(R.id.imageName)).setText("");
        ((EditText) findViewById(R.id.displayText)).setText("");
        ((EditText) findViewById(R.id.scriptText)).setText("");
        fontSize = DEFAULT_FONT_SIZE;

        ((RadioGroup) findViewById(R.id.visibleGroup)).check(R.id.isVisible);
        ((RadioGroup) findViewById(R.id.moveGroup)).check(R.id.movable);
    }

    public void onUndo(View view){
        String gameName = Game.curGame.getGameName();
        Game.curGame = new Game(gameName);
        Vector<Page> oldPages =  new Vector<Page>();
        StringTokenizer st = new StringTokenizer(prevPages);
        Log.d("MESSAGES", "onUndo: prevPagesStr: " +prevPages);
        Log.d("MESSAGES", "onUndo: prevShapesStr: " +prevShapes);
        while(st.hasMoreTokens()){
            String pageString = st.nextToken();
            String[] pageArgs = pageString.split(",", -1);
            Page page = new Page(pageArgs[0], pageArgs[1]);
            oldPages.add(page);
        }

        st = new StringTokenizer(prevShapes);
        while(st.hasMoreTokens()){
            String shapeString = st.nextToken();
            String[] shapeArgs = shapeString.split(",", -1);
            String parentName = shapeArgs[2];
            Shape shape = new Shape(shapeArgs[0], Float.parseFloat(shapeArgs[3]),
                    Float.parseFloat(shapeArgs[4]), Float.parseFloat(shapeArgs[5]),
                    Float.parseFloat(shapeArgs[6]), Integer.parseInt(shapeArgs[7]),
                    Integer.parseInt(shapeArgs[8]), shapeArgs[9],
                    shapeArgs[11], shapeArgs[10], Integer.parseInt(shapeArgs[12]));
            for(Page page: oldPages){
                if(parentName.equals(page.getName())) page.addShape(shape);
            }
        }
        Game.curGame = new Game(oldPages, gameName);

        //reseting scripts to accomodate references created after scripts initially ingested
        for(Page page: Game.curGame.getPages()){
            for(Shape shape: page.getShapes()){
                shape.setScriptText(shape.getScriptText());
            }
        }
        Game.curGame.setCurrentShape(Game.curGame.getShape(prevCurShape));
        Game.curGame.changePage(Game.curGame.getPage(prevCurPage));
        if(Game.curGame.getCurrentShape() == null) setDefaultShapeFields();
        else setShapeFields();
        updatePageSpinner();
        updateShapeSpinner();
        findViewById(R.id.EditorView).invalidate();
    }

    private void setPrevGameState(){
        prevCurPage = Game.curGame.getCurrentPage().getName();
        if(Game.curGame.getCurrentShape() != null) prevCurShape = Game.curGame.getCurrentShape().getName();
        prevPages = "";
        prevShapes = "";
        for(Page page: Game.curGame.getPages()){
            prevPages += serializePage(page);
            for(Shape shape: page.getShapes()) prevShapes += serializeShape(page, shape);
        }
    }

    ///////////////////////////DATABASE RELATED STUFF/////////////////////////

    public void onSaveGame(View view){
        MainActivity.loadingFlag = true;
        setPrevGameState();
        clearDataBase();
        updateDataBase();
        MainActivity.loadingFlag = false;
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

    public String serializePage(Page page){
        String pageStr = page.getName() + "," + page.getBackgroundImage() + " ";
        return pageStr;
    }

    public String serializeShape(Page page, Shape shape){
        String shapeStr = shape.getName() + "," + Game.curGame.getGameName() + ","
                + page.getName() + "," + shape.getX() + "," + shape.getY()
                + "," + shape.getHeight() + "," + shape.getWidth() + "," + toInt(shape.getMovable())
                + "," + toInt(shape.getHidden())+ "," + shape.getImage() + "," + shape.getScriptText() + "," +
                shape.getText() + "," + shape.getFontSize() + " ";
        return shapeStr;
    }
}
