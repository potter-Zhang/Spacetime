package edu.whu.spacetime.domain;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId"),
        indices = {@Index("notebookId"), @Index("userId")})
public class Notebook {
    @PrimaryKey(autoGenerate = true)
    private int notebookId;

    private int userId;

    private String name;

    @Ignore
    public Notebook() {
    }

    public Notebook(String name, int userId) {
        this.name = name;
        this.userId = userId;
    }

    public int getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(int notebookId) {
        this.notebookId = notebookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
