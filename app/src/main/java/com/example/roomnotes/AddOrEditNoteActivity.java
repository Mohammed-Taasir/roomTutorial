package com.example.roomnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddOrEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.roomnotes.EXTRA_ID";     // added this for update operation

    public static final String EXTRA_TITLE = "com.example.roomnotes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.roomnotes.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.roomnotes.EXTRA_PRIORITY";

    private EditText txtTitle;
    private EditText txtDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        txtTitle = findViewById(R.id.edit_text_title);
        txtDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_cancel);  // matlab agar wo cancel button ko dabayenge action bar me to home pohoch jayenge

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            txtTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            txtDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));       // after this we want to make changes in saveNote() method.
        }else{
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {           // ye activity ke option wala part hota hai na actionbar ke deep right me wo hai ye. to usme apan ko ye menu rakhna hai to usi ke inflation ka code hai yeh.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please insert title and description", Toast.LENGTH_LONG).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);     // ye mainactivity se aya hua intent hai toh id extract karre idhar. also why -1 becos we wont have any entry in database that has id = -1 so when id is -1 we can know id is invalid.
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);             // this kind of intent automatically sends result back to activity which calls this intent.
        finish();
    }
}