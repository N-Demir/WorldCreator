package edu.stanford.cs108.worldcreator;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Player extends AppCompatActivity {
    SQLiteDatabase db;
    //Game curGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //Assume a database exists cause we've gotten here
        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null);

        // Get game
        Intent intent = getIntent();
        String gameName = intent.getStringExtra("game_name");
        //TODO FILL OUT curGame

        setTitle(gameName);

    }
}
