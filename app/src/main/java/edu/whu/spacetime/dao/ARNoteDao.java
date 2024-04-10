package edu.whu.spacetime.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.whu.spacetime.domain.ARNote;

@Dao
public interface ARNoteDao {
    @Insert
    List<Long> insertARNotes(ARNote... arNotes);

    @Delete
    void deleteARNotes(ARNote... arNotes);

    @Update
    void updateARNotes(ARNote... arNotes);

    @Query("SELECT * FROM arnote WHERE userId=:userId")
    List<ARNote> queryARNotesByUserId(int userId);
}
