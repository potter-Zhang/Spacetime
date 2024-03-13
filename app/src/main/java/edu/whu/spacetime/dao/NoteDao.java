package edu.whu.spacetime.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.whu.spacetime.domain.Note;

@Dao
public interface NoteDao {
    @Insert
    void insertNote(Note... note);

    @Delete
    void deleteNote(Note... note);

    @Update
    void updateNote(Note... note);

    @Query("SELECT * FROM note")
    List<Note> queryAll();

    @Query("SELECT * FROM note WHERE notebookId=:notebookId")
    List<Note> queryAllInNotebook(int notebookId);
}
