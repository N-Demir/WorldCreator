package edu.stanford.cs108.worldcreator;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Player extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //Assume a database exists cause we've gotten here
        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null);

    }
}
