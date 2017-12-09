package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Field;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    /* So that other classes can access resources */
    public static Context curContext;
    public static boolean loadingFlag;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vector<String> canDraw = new Vector<String>();
        Vector<String> canPlay = new Vector<String>();
        Field[] drawables = edu.stanford.cs108.worldcreator.R.drawable.class.getFields();
        for (Field f : drawables) {
            try {
                canDraw.add(f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Editor.imageNames = canDraw;
        Field[] playable = edu.stanford.cs108.worldcreator.R.raw.class.getFields();
        for (Field f : playable){
            try {
                canPlay.add(f.getName());
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
        Script.media = canPlay;

        /* Database setup stuff */
        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null);

        //DEBUGGING, CLEARS DB
//        String gamestr = "DROP TABLE IF EXISTS games;";
//        String pagestr = "DROP TABLE IF EXISTS pages;";
//        String shapestr = "DROP TABLE IF EXISTS shapes;";
//        db.execSQL(gamestr);
//        db.execSQL(pagestr);
//        db.execSQL(shapestr);
        Cursor tablesCursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'" +
                " AND name='games';", null);
        if (tablesCursor.getCount() == 0) {
            /* No database previously existed, create a fresh one */
            setupDBTables();
        }
        /* If games table empty, disable buttons */
        Cursor gamesCursor = db.rawQuery("SELECT * FROM games;", null); //SHOULD BE WORKING?
        if (gamesCursor.getCount() == 0) setButtonsEnabled(false);
        else setButtonsEnabled(true);
        updateGameSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        curContext = getApplicationContext();
        loadingFlag = true;
    }

    /**
     * Creates 3 new tables in SQDatabase: Games, Pages, and Shapes
     */
    private void setupDBTables() {
            String gameTable = "CREATE TABLE games ("
                    + "name TEXT,"
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ");";
            db.execSQL(gameTable);
            String pageTable = "CREATE TABLE pages ("
                    + "name TEXT, image TEXT, game TEXT,"
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ");";
            db.execSQL(pageTable);
            // geometry 4 ints, name, movable, visible, image-name, text, associatedscript
            String shapesTable = "CREATE TABLE shapes ("
                    + "name TEXT, game TEXT,"
                    + "page TEXT, x REAL, y REAL,"
                    + "height REAL, width REAL, move INTEGER,"
                    + "visible INTEGER, image TEXT, script TEXT, label TEXT, textSize INTEGER,"
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ");";
            db.execSQL(shapesTable);
    }

    private void updateGameSpinner() {
        String[] fromArray = {"name"};
        int[] toArray = {android.R.id.text1};
        Cursor cursor = db.rawQuery("SELECT * FROM games",null);
        Spinner spinner = (Spinner) findViewById(R.id.game_spinner);
        CursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                cursor, fromArray, toArray, 0);
        spinner.setAdapter(adapter);
    }

    /**
     * Sets the buttons that can be switched off if there are no games and user has to create
     * a game first to be off or on.
     * @param enabled
     */
    private void setButtonsEnabled(Boolean enabled) {
        Button removeButton = (Button) findViewById(R.id.removeButton);
        removeButton.setEnabled(enabled);
        Button playerButton = (Button) findViewById(R.id.playerButton);
        playerButton.setEnabled(enabled);
        Button editorButton = (Button) findViewById(R.id.editorButton);
        editorButton.setEnabled(enabled);
    }

    public void onCreateGame(View view){
        //Get game name, create new game, set buttons to enabled, launch editor
        EditText editText = (EditText) findViewById(R.id.gname);
        String newGameName = editText.getText().toString();
        if (newGameName.equals("")) return;
        editText.setText(""); //NECESSARY?
        Game.curGame = new Game(newGameName); /* Calls the fresh constructor */

        /* Adds game to the database */
        //TODO: IS THIS HOW WE DO IT?
        String gameStr = "INSERT INTO games VALUES "+
                "('" + newGameName + "',NULL);";
        db.execSQL(gameStr);
        String pageStr = "INSERT INTO pages VALUES " +
                "('" + Game.curGame.getCurPageName() + "','','" + newGameName + "',NULL);";
        db.execSQL(pageStr);
        updateGameSpinner();
        setButtonsEnabled(/*Boolean enabled*/ true);
        Intent intent = new Intent(this, Editor.class);
        startActivity(intent);
    }

    // TODO Make sure that this removes everything correctly from the data bse
    public void onGameRemove(View view) {
        Vector<String> pageNames = new Vector<String>();
        String gameName= ((Cursor)((Spinner) findViewById(R.id.game_spinner)).getSelectedItem()).getString(0);
        Cursor pCursor = db.rawQuery("SELECT * FROM pages WHERE game='" + gameName + "'", null);
        while (pCursor.moveToNext()) pageNames.add(pCursor.getString(0));
        for (int i =0; i < pageNames.size(); i++){
            String sDelete = "DELETE FROM shapes WHERE game='" + gameName + "' AND page='" + pageNames.get(i) + "'";
            db.execSQL(sDelete);
        }
        String pDelete = "DELETE FROM pages WHERE game='" + gameName + "'";
        db.execSQL(pDelete);
        String gDelete = "DELETE FROM games WHERE name='" + gameName + "'";
        db.execSQL(gDelete);
        updateGameSpinner();
        if(((Spinner) findViewById(R.id.game_spinner)).getCount() == 0) setButtonsEnabled(false);
    }


    /**
     * Button on click methods that load up intents and send them to a shared method gotoActivity
     * that actually does the launching of the activity stuff.
     * @param view
     */
    public void onGotoPlayer(View view) {
        Intent intent = new Intent(this,Player.class);
        gotoActivity(intent, view);
    }
    public void onGotoEditor(View view) {
        Intent intent = new Intent(this, Editor.class);
        gotoActivity(intent,view);
    }

    /**
     * Actually does the work of launching activity. First must get user selected game
     * and load it into the curGame static field in Game class.
     * @param intent
     */
    private void gotoActivity(Intent intent, View view) {
        //TODO:FIX BUGS HERE

        Vector<Page> document = new Vector<Page>();
        String gameName = ((Cursor) ((Spinner) findViewById(R.id.game_spinner)).getSelectedItem()).getString(0);
         //TODO:FIGURE OUT HOW TO DO THIS
        Cursor pCursor = db.rawQuery("SELECT * FROM pages WHERE game='" + gameName + "'", null);
        if (pCursor.getCount() == 0) return; //Todo
        Game.curGame = new Game(gameName);
        Log.d("MESSAGE" , "yew");
        while (pCursor.moveToNext()){
            Log.d("MESSAGE" , "yew2");
            Page cur = new Page(pCursor.getString(0), pCursor.getString(1));
            Log.d("NIKITASLOG:", "PCursor2: " + pCursor.getString(1));
            Cursor sCursor = db.rawQuery("SELECT * FROM shapes WHERE game='" + gameName + "' AND page='" + pCursor.getString(0) + "'", null);
            while (sCursor.moveToNext()){
                // constructor goes name, x, y, height, width, move, visible, img, script, label, fontSize
                Shape s = new Shape(sCursor.getString(0), sCursor.getFloat(3), sCursor.getFloat(4),
                        sCursor.getFloat(5), sCursor.getFloat(6), sCursor.getInt(7),sCursor.getInt(8),
                        sCursor.getString(9), sCursor.getString(11), sCursor.getString(10), sCursor.getInt(12));
                cur.addShape(s);
           }
            document.add(cur);
        }
        Game.curGame = new Game(document, gameName);
        for(Page page: Game.curGame.getPages()){
            for(Shape shape: page.getShapes()){
                shape.setScriptText(shape.getScriptText());
            }
        }

        startActivity(intent);
    }
}