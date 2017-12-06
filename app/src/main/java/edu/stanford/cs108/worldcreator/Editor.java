package edu.stanford.cs108.worldcreator;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Vector;

public class Editor extends AppCompatActivity {
    SQLiteDatabase db;
    private Page p;
    private static int count = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Assume a database exists cause we've gotten here
        db = openOrCreateDatabase("WorldCreatorDB", MODE_PRIVATE, null); //TODO: BUGGY? BAD STYLE?
        updateSpinner();
        setTitle("Editing: " + Game.curGame.getGameName());
    }

    public void createPage(View view){
        EditText editText = (EditText) findViewById(R.id.npage);
        String newGame = editText.getText().toString();
        int prevPage = count - 1;
        String prevName = "page" + prevPage;
        if (newGame.equals("") || newGame.equals(prevName)){
            newGame = "page" +count;
            editText.setText("page" + count);
            count++;
        }
        p = new Page(newGame);
        Game.curGame.addPage(p);
        Vector<Page> ps = Game.curGame.getPages();
        updateSpinner();
    }

    // TODO Figure out how to set the selected spinner item
    private void updateSpinner(){
        Vector<String> pageNames = new Vector<String>();
        Vector<Page> myPages = Game.curGame.getPages();
        for (Page cur : myPages){
            pageNames.add(cur.getName());
            Log.d("MESSAGE", cur.getName());
        }
        Spinner spinner = (Spinner) findViewById(R.id.page_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pageNames);
        spinner.setAdapter(adapter);

    }
    //TODO There is a bug when you try to delete the first item in the list when there are other items Could be the array adapter stuff
    public void deletePage(View view){
        Spinner spinner = (Spinner) findViewById(R.id.page_spinner);
        String pageName = spinner.getSelectedItem().toString();
        for (Page p : Game.curGame.getPages()){
            if (pageName.equals(p.getName())) Game.curGame.getPages().remove(p);
        }
        updateSpinner();
    }

    public void newShape (View view){
        EditText xText = (EditText) findViewById(R.id.xCord);
        EditText yText = (EditText) findViewById(R.id.yCord);
        EditText wText = (EditText) findViewById(R.id.width);
        EditText hText = (EditText) findViewById(R.id.height);
        RadioButton moveable = (RadioButton) findViewById(R.id.moveable);
        RadioButton visible = (RadioButton) findViewById(R.id.isVisible);
        boolean canMove = moveable.isChecked();
        boolean isVisible = visible.isChecked(); 
        int x = Integer.parseInt(xText.getText().toString());
        int y = Integer.parseInt(yText.getText().toString());
        int height = Integer.parseInt(hText.getText().toString());
        int width = Integer.parseInt(wText.getText().toString());

    }

    public void savePage(View view){

    }
}
