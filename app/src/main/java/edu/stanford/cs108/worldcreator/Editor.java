package edu.stanford.cs108.worldcreator;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Editor extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Assume a database exists cause we've gotten here
        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null); //TODO: BUGGY? BAD STYLE?

        setTitle("Editing: " + Game.curGame.getGameName());
    }
}
