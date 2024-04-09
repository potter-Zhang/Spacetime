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
    List<Long> insertNotebook(Notebook... notebooks);

    // TODO: 删除笔记本时需要级联删除笔记本中的所有笔记
    @Delete
    void deleteNotebook(Notebook... notebooks);

    @Update
    void updateNotebook(Notebook... notebooks);

    @Query("SELECT * FROM notebook WHERE userId=:userId")
    List<Notebook> getNotebooksByUserId(int userId);

    @Query("SELECT * FROM notebook WHERE userId = :userId")
    List<Notebook> getNotebookByUserId(int userId);
}
