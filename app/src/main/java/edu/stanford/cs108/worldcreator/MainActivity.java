package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    List<String> adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Database setup stuff */
//        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null);
//        Cursor tablesCursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'" +
//                " AND name='games';", null);
//        if (tablesCursor.getCount() == 0) {
//            String gameTable = "CREATE TABLE games ("
//                    + "name TEXT,"
//                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
//                    + ");";
//            db.execSQL(gameTable);
//            String pageTable = "CREATE TABLE pages ("
//                    + "name TEXT, game TEXT,"
//                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
//                    + ");";
//            db.execSQL(pageTable);
//            // geometry 4 ints, name, movable, visible, image-name, text, associatedscript
//            String shapesTable = "CREATE TABLE shapes ("
//                    + "name TEXT, game TEXT,"
//                    + "page TEXT, x INTEGER, y INTEGER,"
//                    + "height INTEGER, width INTEGER, move INTEGER,"
//                    + "visible INTEGER, image TEXT, script TEXT, label TEXT"
//                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
//                    + ");";
//            db.execSQL(shapesTable);
//        }
        //TODO:FINISH DATABASE STUFF

        adapt = new ArrayList<String>();
        //TODO: IF GAMES > 0 ENABLE BUTTONS
        Spinner spinner = (Spinner) findViewById(R.id.game_spinner); //ISSUES??

        //TODO: This stuff is temporary for testing, need to replace with a database adapter
        //String[] fromArray = {"name"}; //Or whatever
        //String[] toArray =
        //SpinnerAdapter adapter = new SimpleCursorAdapter() //TODO: Base on database
        String[] testArray = {"BunnyWorld", "SherlockWorld"};
        SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                testArray);
        spinner.setAdapter(adapter);

    }


    public void onGameRemove(View view) {
        //TODO: DO STUFF
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

    public void createGame(View view){
        Vector<Page> document = new Vector<Page>();
        EditText editText = (EditText) findViewById(R.id.gname);
        String newGame = editText.getText().toString();
        editText.setText("");
        adapt.add(newGame);
        Spinner spinner = (Spinner) findViewById(R.id.game_spinner);
        ArrayAdapter<String> nadapt = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, adapt);
        nadapt.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(nadapt);
        Page page = new Page("page1");
        document.add(page);
        Game game = new Game(document,null,newGame);
        Game.curGame = game;
        // create new page one add that game and page one to
        //
    }

    /**
     * Actually does the work of launching activity. First must get user selected game
     * and load it into the curGame static field in Game class.
     * @param intent
     */
    private void gotoActivity(Intent intent) {
        Vector<Page> document = new Vector<Page>();
        Spinner spinner = (Spinner) findViewById(R.id.game_spinner);
        String gameName = spinner.getSelectedItem().toString();
        Cursor pCursor = db.rawQuery("SELECT * FROM pages WHERE game =" + gameName, null);
        while (pCursor.moveToNext()){
            String pageName = pCursor.getString(0);
            Page cur = new Page(pCursor.getString(0));
            // check if queries are correct
            Cursor sCursor = db.rawQuery("SELECT * FROM shapes WHERE game='" + gameName + "' AND page = " + pageName, null);
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
       Game.curGame = newGame;
        startActivity(intent);
    }
}
