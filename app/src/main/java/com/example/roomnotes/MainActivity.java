package com.example.roomnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}