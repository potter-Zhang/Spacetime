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

        Migration migration4_5 = new Migration(4, 5) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("ALTER TABLE ARNote ADD createTime TEXT");
            }
        };

        Migration migration5_6 = new Migration(5, 6) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE User;");
                supportSQLiteDatabase.execSQL("CREATE TABLE User (`password` TEXT, `gender` INTEGER NOT NULL, `phone` TEXT, `createTime` TEXT, `avatar` BLOB, `region` TEXT, `userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `username` TEXT)");
            }
        };

        Migration migration6_7 = new Migration(6, 7) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("ALTER TABLE Note ADD editTime TEXT;");
            }
        };

        database = Room.databaseBuilder(this, SpacetimeDatabase.class, "spacetime")
                .addMigrations(migration3_4, migration4_5, migration5_6, migration6_7)
                .allowMainThreadQueries()
                .build();
    }
}
