package edu.whu.spacetime.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ARNote {
    @PrimaryKey(autoGenerate = true)
    private long arNoteId;

    private int userId;

    private String title;

    private byte[] img;

    public long getArNoteId() {
        return arNoteId;
    }

    public void setArNoteId(long arNoteId) {
        this.arNoteId = arNoteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
