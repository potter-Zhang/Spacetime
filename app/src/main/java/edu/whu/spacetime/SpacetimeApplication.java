package edu.whu.spacetime;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import edu.whu.spacetime.database.SpacetimeDatabase;
import edu.whu.spacetime.domain.User;

public class SpacetimeApplication  extends Application {
    private static SpacetimeApplication app;

    // 单例
    public static SpacetimeApplication getInstance() {
        return app;
    }

    // 数据库对象
    private SpacetimeDatabase database;

    // 当前登录的用户
    private User currentUser;

    public SpacetimeDatabase getDatabase() {
        return database;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        Migration migration3_4 = new Migration(3, 4) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `ARNote` (`arNoteId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `title` TEXT, `img` BLOB)");
            }
        };

        database = Room.databaseBuilder(this, SpacetimeDatabase.class, "spacetime")
                .addMigrations(migration3_4)
                .allowMainThreadQueries()
                .build();
    }
}
