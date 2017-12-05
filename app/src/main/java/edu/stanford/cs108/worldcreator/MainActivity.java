package edu.stanford.cs108.worldcreator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Database setup stuff */
        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null);
        Cursor tablesCursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'" +
                " AND name='games';", null);
        if (tablesCursor.getCount() == 0) {
            /* Database has not been created, setup the tables */
            //TODO FILL OUT
        }
        //TODO:FINISH DATABASE STUFF


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

    /**
     * Actually does the work of launching activity. First must get user selected game
     * and load it into the curGame static field in Game class.
     * @param intent
     */
    private void gotoActivity(Intent intent) {
        Spinner spinner = (Spinner) findViewById(R.id.game_spinner);
        String gameName = spinner.getSelectedItem().toString();

        //TODO: Fill out curGame static field in game

        startActivity(intent);
    }
}
