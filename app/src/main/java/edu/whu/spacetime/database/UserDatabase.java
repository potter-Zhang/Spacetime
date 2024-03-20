package edu.whu.spacetime.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import edu.whu.spacetime.dao.UserDao;
import edu.whu.spacetime.domain.User;

@Database(entities = {User.class}, version = 1, exportSchema = true)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
