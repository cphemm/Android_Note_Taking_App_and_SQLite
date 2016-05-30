package com.example.carolynhemmings.savemynotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import data.DatabaseHandler;
import model.Notes;


public class MainActivity extends Activity {

    private EditText title;
    private EditText content;
    private Button saveButton;
    private DatabaseHandler dba;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dba = new DatabaseHandler(MainActivity.this);

        title = (EditText) findViewById(R.id.titleEditText);
        content = (EditText) findViewById(R.id.noteEditText);
        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
            }
        });

    }

    private void saveToDB() {

        Notes notes = new Notes();
        notes.setTitle(title.getText().toString().trim());
        notes.setContent(content.getText().toString().trim());

        dba.addNotes(notes);
        dba.close();

        //clear
        title.setText("");
        content.setText("");

        Intent i = new Intent(MainActivity.this, DisplayNotesActivity.class);
        startActivity(i);

    }


}
