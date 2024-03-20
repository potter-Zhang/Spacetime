package edu.whu.spacetime;

import android.app.Application;

import androidx.room.Room;

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

        database = Room.databaseBuilder(this, SpacetimeDatabase.class, "spacetime")
                .addMigrations()
                .allowMainThreadQueries()
                .build();
    }
}
