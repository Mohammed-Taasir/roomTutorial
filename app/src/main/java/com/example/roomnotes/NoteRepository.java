package com.example.roomnotes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();           // abstract method in our database class    // we use noteDao to make database operations.

        allNotes = noteDao.getAllNotes();         // as noteDao is constructed now, we can call all methods related to it.
    }

    public void insert(Note note){               // this all insert(), update(), delete() will run on main thread but room wont allow to run on main thread hence we define asynctask to define new methods.
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes(){
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes(){          // this operation will run on background thread
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }

    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }

    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }

    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void>{

        private NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }

    }
}
