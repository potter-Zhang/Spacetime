package edu.whu.spacetime.database;


import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import edu.whu.spacetime.dao.ARNoteDao;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.dao.NotebookDao;
import edu.whu.spacetime.dao.TodoDao;
import edu.whu.spacetime.dao.UserDao;
import edu.whu.spacetime.domain.ARNote;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.domain.Todo;
import edu.whu.spacetime.domain.User;

@Database(entities = {Notebook.class, User.class, Note.class, Todo.class, ARNote.class},
        version = 4, exportSchema = true
)
public abstract class SpacetimeDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract NotebookDao getNotebookDao();
    public abstract NoteDao getNoteDao();
    public abstract TodoDao getTodoDao();
    public abstract ARNoteDao getARNoteDao();
}
