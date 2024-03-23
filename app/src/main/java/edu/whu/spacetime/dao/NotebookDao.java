package edu.whu.spacetime.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.domain.Notebook;

@Dao
public interface NotebookDao {
    @Insert
    void insertNotebook(Notebook... notebook);
    @Update
    void updateNotebook(Notebook... notebook);
    @Delete
    void deleteNotebook(Notebook... notebook);

    @Query("SELECT * FROM notebook WHERE userId = :userId")
    List<Notebook> getNotebookByUserId(int userId);
}
