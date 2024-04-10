package edu.whu.spacetime.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.whu.spacetime.domain.ARNote;

@Dao
public interface ARNoteDao {
    @Insert
    List<Long> insertARNotes(ARNote... arNotes);

    @Query("SELECT * FROM arnote WHERE userId=:userId")
    List<ARNote> queryARNotesByUserId(int userId);
}
