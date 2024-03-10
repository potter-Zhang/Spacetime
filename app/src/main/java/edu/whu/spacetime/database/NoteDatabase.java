package edu.whu.spacetime.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.domain.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = true)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();
}
