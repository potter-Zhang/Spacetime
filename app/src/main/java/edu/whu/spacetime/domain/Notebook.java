package edu.whu.spacetime.domain;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Notebook {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private int userId;

    @Ignore
    public Notebook() {
    }

    public Notebook(String name, int userId) {
        this.name = name;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
