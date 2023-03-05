package com.example.roomnotes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)         // this database will be having only one table for now -> Note.class
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;              // instance of our database

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){       // lets initialize our database
        if(instance == null){           // singleton pattern wala shit koi aur dusra thread database nai bana sakta agar 1 thread already database bana chuka hai.
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")       // lets build our database.
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)          // oh shit so we are populating our database too along the way
                    .build();
        }
        return instance;            // return created NoteDatabase instance.
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){        // stuff to populate our database with data

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {           // onCreate() called when database is created/
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();            // using this callback we will populate our database too.
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db){           // apan ko database populate karna hai to database hi denge na input me .........
            noteDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));
            return null;
        }
    }
}
