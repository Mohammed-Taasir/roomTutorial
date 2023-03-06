package com.example.roomnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2; // added this for edit operation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnAddNote = findViewById(R.id.btn_add_note);
        btnAddNote.setOnClickListener((view -> {
            Intent intent = new Intent(MainActivity.this, AddOrEditNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);                       // this intent will get the result from AddNoteActivity.
        }));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);


        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);       // get your noteViewModel from your java class.
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {      // get AllNotes on onCreate().
            @Override
            public void onChanged(List<Note> notes) {           // called everytime when our LiveData changes
                // hence pass list of notes and update recyclerView
//                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_LONG).show();
                adapter.setNotes(notes);
            }
        });
                                                            // we dont wanna support drag and drop but we wanna support left and right swipe.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {      // this thing makes our recyclerview touchable
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(), "Note Deleted .. ", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddOrEditNoteActivity.class);
                intent.putExtra(AddOrEditNoteActivity.EXTRA_ID, note.getId());      // we pass the id to receiving class for editing purposes. // agar already hai koi entry to uski id automatically set hogi and it will be != -1.
                intent.putExtra(AddOrEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddOrEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddOrEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {       // handle the obtained result via intent.
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddOrEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddOrEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddOrEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);

            Toast.makeText(getApplicationContext(), "Note saved successfully !", Toast.LENGTH_LONG).show();
        } else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddOrEditNoteActivity.EXTRA_ID, -1);
            if(id == -1){
                Toast.makeText(getApplicationContext(), "Note can't be updated", Toast.LENGTH_LONG).show();
                return;         // we want to exit this method because something went wrong.
            }

            String title = data.getStringExtra(AddOrEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddOrEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddOrEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            note.setId(id);     // update operation wont be succeeded until you set the id.
            noteViewModel.update(note);

            Toast.makeText(getApplicationContext(), "Note updated successfully !", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Note failed to save !", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     // inflate our action bar with menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {      // menu ke options ki mohmaaya ko handle karra hai below code.
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(getApplicationContext(), "All notes deleted ...", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}