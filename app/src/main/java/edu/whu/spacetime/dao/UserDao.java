package edu.whu.spacetime.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import edu.whu.spacetime.domain.User;

@Dao
public interface UserDao {
    @Insert
    void InsertUser(User... users);

    @Delete
    void deleteUser(User... users);

    @Update
    void updateUser(User... users);

    @Query("SELECT * FROM user WHERE userId=:userId LIMIT 1")
    User getUserById(int userId);
}
