package com.example.notaapp;

import androidx.annotation.NonNull;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteDetailsActivity extends ParentActivity {
    private EditText titleET;
    private EditText descET;
    private int receivedId;
    private boolean openFromUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        titleET = findViewById(R.id.et_title);
        descET = findViewById(R.id.et_description);
        receivedId = getIntent().getIntExtra("id", -1);
        if (receivedId != -1) {
            setTitle(R.string.update_note);
            titleET.setText(getIntent().getStringExtra("title"));
            descET.setText(getIntent().getStringExtra("description"));
            Button updatebtn = findViewById(R.id.btn_update);
            updatebtn.setVisibility(View.VISIBLE);
            openFromUpdate = true;
        } else
            setTitle(R.string.add_note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (openFromUpdate) {
            return false;
        } else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.save_notes_menu, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_save_menu) {
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title = titleET.getText().toString();
        String description = descET.getText().toString();
        if (title.isEmpty()) {
            titleET.setError(getString(R.string.required_field));
        } else {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("description", description);

            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            long id = db.insert("note", null, values);
            if (id != -1) {
                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void updateOnClick(View view) {
        String title = titleET.getText().toString();
        String description = descET.getText().toString();
        if (title.isEmpty()) {
            titleET.setError(getString(R.string.required_field));
        } else {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("description", description);

            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            String[] whereArgs = {String.valueOf(receivedId)};
            long id = db.update("note", values, "_id==?", whereArgs);
            if (id != 0) {
                Toast.makeText(this, R.string.note_updated, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}