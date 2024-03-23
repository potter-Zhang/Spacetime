package edu.whu.spacetime.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.whu.spacetime.domain.Notebook;

@Dao
public interface NotebookDao {
    @Insert
    void insertNotebook(Notebook... notebooks);

    @Delete
    void deleteNotebook(Notebook... notebooks);

    @Update
    void updateNotebook(Notebook... notebooks);

    @Query("SELECT * FROM notebook WHERE userId=:userId")
    List<Notebook> getNotebooksByUserId(int userId);

    @Query("SELECT * FROM notebook WHERE userId = :userId")
    List<Notebook> getNotebookByUserId(int userId);
}
