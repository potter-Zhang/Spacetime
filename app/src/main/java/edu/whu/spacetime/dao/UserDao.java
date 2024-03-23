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
    void insertUser(User... users);

    @Delete
    void deleteUser(User... users);

    @Update
    void updateUser(User... users);

    @Query("SELECT * FROM user WHERE userId=:userId LIMIT 1")
    User getUserById(int userId);

    @Query("SELECT * FROM user WHERE username=:username")
    User getUserByName(String username);

    // 用于直接进入app(仅调试时使用！)
    @Query("SELECT * FROM user LIMIT 1")
    User getSingleUser();
}
