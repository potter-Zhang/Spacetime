package edu.whu.spacetime.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;

import edu.whu.spacetime.domain.typeConverter.MyTypeConverter;

@Entity
public class ARNote {
    @PrimaryKey(autoGenerate = true)
    private long arNoteId;

    private int userId;

    private String title;

    private byte[] img;

    @TypeConverters(MyTypeConverter.class)
    private LocalDateTime createTime;

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
