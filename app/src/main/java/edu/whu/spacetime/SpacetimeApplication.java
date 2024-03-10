package edu.whu.spacetime;

import android.app.Application;
import android.provider.DocumentsContract;

import androidx.room.Room;

import edu.whu.spacetime.database.NoteDatabase;

public class SpacetimeApplication  extends Application {
    private static SpacetimeApplication app;

    // 笔记数据库对象
    private NoteDatabase noteDatabase;

    public static SpacetimeApplication getIntance() {
        return app;
    }

    public NoteDatabase getNoteDatabase() {
        return noteDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        // 构建数据库实例
        noteDatabase = Room.databaseBuilder(this, NoteDatabase.class, "note").build();
    }
}
