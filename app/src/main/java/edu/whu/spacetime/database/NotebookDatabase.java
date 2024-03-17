package edu.whu.spacetime.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import edu.whu.spacetime.dao.NotebookDao;
import edu.whu.spacetime.domain.Notebook;

@Database(entities = {Notebook.class}, version = 1, exportSchema = true)
public abstract class NotebookDatabase extends RoomDatabase {
    public abstract NotebookDao getNotebookDao();
}
