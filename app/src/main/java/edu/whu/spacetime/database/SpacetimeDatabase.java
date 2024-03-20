package edu.whu.spacetime.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.dao.NotebookDao;
import edu.whu.spacetime.dao.UserDao;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.domain.User;

@Database(entities = {Notebook.class, User.class, Note.class}, version = 1, exportSchema = true)
public abstract class SpacetimeDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract NotebookDao getNotebookDao();
    public abstract NoteDao getNoteDao();
}
