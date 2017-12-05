package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    /* So that other classes can access resources */
    public static Context curContext;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.curContext = getApplicationContext();


        /* Database setup stuff */
        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null);

        /*//DEBUGGING, CLEARS DB
        String gamestr = "DROP TABLE IF EXISTS games;";
        String pagestr = "DROP TABLE IF EXISTS pages;";
        String shapestr = "DROP TABLE IF EXISTS shapes;";
        db.execSQL(gamestr);
        db.execSQL(pagestr);
        db.execSQL(shapestr);*/

        Cursor tablesCursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'" +
                " AND name='games';", null);
        if (tablesCursor.getCount() == 0) {
            /* No database previously existed, create new one */
            setupDBTables();
        }

        /* If games table empty, disable buttons */
        Cursor gamesCursor = db.rawQuery("SELECT * FROM games;", null); //SHOULD BE WORKING?
        if (tablesCursor.getCount() == 0) setButtonsEnabled(false);
        else setButtonsEnabled(true);

        updateGameSpinner();
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
                    + "name TEXT, game TEXT,"
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                    + ");";
            db.execSQL(pageTable);
            // geometry 4 ints, name, movable, visible, image-name, text, associatedscript
            String shapesTable = "CREATE TABLE shapes ("
                    + "name TEXT, game TEXT,"
                    + "page TEXT, x INTEGER, y INTEGER,"
                    + "height INTEGER, width INTEGER, move INTEGER,"
                    + "visible INTEGER, image TEXT, script TEXT, label TEXT,"
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
                "('" + Game.curGame.getCurPageName() + "','" + newGameName + "',NULL);";
        db.execSQL(pageStr);

        updateGameSpinner();

        setButtonsEnabled(/*Boolean enabled*/ true);

        Intent intent = new Intent(this, Editor.class);
        startActivity(intent);
    }

    public void onGameRemove(View view) {
        //TODO: DO Database stuff
        //TODO: If games table is empty, setButtonsEnabled(false);
    }


    /**
     * Button on click methods that load up intents and send them to a shared method gotoActivity
     * that actually does the launching of the activity stuff.
     * @param view
     */
    public void onGotoPlayer(View view) {
        Intent intent = new Intent(this,Player.class);
        gotoActivity(intent);
    }
    public void onGotoEditor(View view) {
        Intent intent = new Intent(this, Editor.class);
        gotoActivity(intent);
    }

    /**
     * Actually does the work of launching activity. First must get user selected game
     * and load it into the curGame static field in Game class.
     * @param intent
     */
    private void gotoActivity(Intent intent) {
        //TODO:FIX BUGS HERE
        /*Vector<Page> document = new Vector<Page>();
        Spinner spinner = (Spinner) findViewById(R.id.game_spinner);
        String gameName = spinner.getSelectedItem().toString();
        Cursor pCursor = db.rawQuery("SELECT * FROM pages WHERE game='" + gameName + "'", null);
        while (pCursor.moveToNext()){
            String pageName = pCursor.getString(0);
            Page cur = new Page(pCursor.getString(0));
            // check if queries are correct
            Cursor sCursor = db.rawQuery("SELECT * FROM shapes WHERE game='" + gameName + "' AND page='" + pageName + "'", null);
            while (sCursor.moveToNext()){
                String name = sCursor.getString(0);
                int xCord = sCursor.getInt(3);
                int height = sCursor.getInt(5);
                int width = sCursor.getInt(6);
                int yCord = sCursor.getInt(4);
                int vis = sCursor.getInt(8);
                boolean visiblity = false;
                boolean mover = false;
                if (vis == 1) visiblity = true;
                int move = sCursor.getInt(7);
                if (move == 1) mover = true;
                String image = sCursor.getString(9);
                String script = sCursor.getString(10);
                Script nscript = new Script(script);
                String label = sCursor.getString(11);
                Shape s = new Shape(name, xCord, yCord);
                s.setHeight(height);
                s.setWidth(width);
                s.setImageName(image);
                s.setHidden(visiblity);
                s.setMoveable(mover);
                s.setText(label);
                s.setScript(nscript);
                cur.addShape(s);
            }
            document.add(cur);
        }
       Game newGame = new Game(document, null, gameName);
       Game.curGame = newGame;*/
        startActivity(intent);
    }
}
