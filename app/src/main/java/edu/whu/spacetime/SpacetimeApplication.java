package edu.whu.spacetime;

import android.app.Application;
import android.provider.DocumentsContract;

import androidx.room.Room;

import edu.whu.spacetime.database.NoteDatabase;
import edu.whu.spacetime.database.NotebookDatabase;
import edu.whu.spacetime.database.UserDatabase;

public class SpacetimeApplication  extends Application {
    private static SpacetimeApplication app;

    // 单例
    public static SpacetimeApplication getIntance() {
        return app;
    }

    // 用户数据库对象
    private UserDatabase userDatabase;

    // 笔记本数据库对象
    private NotebookDatabase notebookDatabase;

    // 笔记数据库对象
    private NoteDatabase noteDatabase;

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public NotebookDatabase getNotebookDatabase() {
        return notebookDatabase;
    }

    public NoteDatabase getNoteDatabase() {
        return noteDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        // 构建数据库实例
        userDatabase = Room.databaseBuilder(this, UserDatabase.class, "user")
                .allowMainThreadQueries()
                .build();
        notebookDatabase = Room.databaseBuilder(this, NotebookDatabase.class, "notebook")
                .allowMainThreadQueries()
                .build();
        noteDatabase = Room.databaseBuilder(this, NoteDatabase.class, "note")
                .allowMainThreadQueries()
                .build();
    }
}
