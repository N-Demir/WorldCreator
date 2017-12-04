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
            /* Database was just created, setup the tables */
            //TODO FILL OUT
        }
        //TODO:FINISH DATABASE STUFF


        Spinner spinner = (Spinner) findViewById(R.id.game_spinner); //ISSUES??
        //String[] fromArray = {"name"}; //Or whatever
        //String[] toArray =
        //SpinnerAdapter adapter = new SimpleCursorAdapter() //TODO: Base on database
        String[] testArray = {"BunnyWorld", "SherlockWorld"};
        SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                testArray);
        spinner.setAdapter(adapter);

    }

    public void onGameSelect(View view) {
        //TODO: DO STUFF
    }

    public void onGameRemove(View view) {
        //TODO: DO STUFF
    }

    public void onGotoPlayer(View view) {
        Intent intent = new Intent(this,Player.class);
        startActivity(intent);
    }

    public void onGotoEditor(View view) {
        gotoEditor();
    }

    private void gotoEditor() {
        Intent intent = new Intent(this, Editor.class);
        startActivity(intent);
    }
}
