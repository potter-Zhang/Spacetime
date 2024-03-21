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

        Migration migration1_2 = new Migration(1, 2) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Todo` (`todoId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `title` TEXT, `addr` TEXT, `checked` INTEGER NOT NULL, `createTime` TEXT)");
            }
        };

        database = Room.databaseBuilder(this, SpacetimeDatabase.class, "spacetime")
                .addMigrations(migration1_2)
                .allowMainThreadQueries()
                .build();
    }
}
