package com.example.notaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ParentActivity {
    private ArrayList<Note> notes = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private RecyclerView notesRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.all_notes);
        getAllNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllNotes();
    }

    public void openNoteDetailsActivity(View view) {
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        startActivity(intent);
    }

    public void getAllNotes() {
        notes.clear();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM note", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            notes.add(new Note(id, title, description));
        }
        listNotes();
    }

    private void listNotes() {
        View noNotesLayout = findViewById(R.id.layout_no_notes);
        if (notes.size() == 0) {
            noNotesLayout.setVisibility(View.VISIBLE);
        } else {
            noNotesLayout.setVisibility(View.INVISIBLE);
            notesRV = findViewById(R.id.notesRV);
            noteAdapter = new NoteAdapter(notes, this);
            notesRV.setAdapter(noteAdapter);
            swipeToDelete();
        }
    }

    private void swipeToDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                showDeleteDialog(position);
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(notesRV);
    }

    private void deleteFromDB(int position) {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] args = {"" + notes.get(position).getId()};
        int deleted = db.delete("note", "_id==?", args);
        if (deleted != 0) {
            Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(R.string.delete_dialog_positive, (dialog, which) -> {
                    deleteFromDB(position);
                    notes.remove(position);
                    noteAdapter.notifyDataSetChanged();
                    if (notes.size() == 0) {
                        View noNotesLayout = findViewById(R.id.layout_no_notes);
                        noNotesLayout.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton(R.string.delete_dialog_negative, (dialog, which) ->
                        noteAdapter.notifyItemChanged(position))
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}