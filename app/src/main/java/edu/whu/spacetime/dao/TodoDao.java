package edu.whu.spacetime.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.whu.spacetime.domain.Todo;

@Dao
public interface TodoDao {
    @Insert
    void insertTodo(Todo... todos);

    @Delete
    void deleteTodo(Todo... todos);

    @Update
    void updateTodo(Todo... todos);

    @Query("SELECT * FROM todo WHERE todoId=:todoId LIMIT 1")
    Todo getTodoById(int todoId);
    @Query("SELECT * FROM todo WHERE userId=:userId")
    List<Todo> getAllTodo(int userId);
    @Query("SELECT * FROM todo WHERE checked=1 AND userId=:userId")
    List<Todo> getCheckedTodo(int userId);
    @Query("SELECT * FROM todo WHERE checked=0 AND userId=:userId")
    List<Todo> getUnCheckedTodo(int userId);
}
