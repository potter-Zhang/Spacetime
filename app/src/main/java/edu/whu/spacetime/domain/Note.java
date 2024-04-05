package edu.whu.spacetime.domain;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.time.LocalDateTime;

import edu.whu.spacetime.domain.typeConverter.MyTypeConverter;

@Entity
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int noteId;

    private String title;

    private int userId;

    /**
     * 所处的笔记本
    */
    private int notebookId;

    /**
     * 笔记的富文本内容，包含html标签
     */
    private String content;

    /**
     * 笔记的纯本文形式
     */
    private String plainText;

    @TypeConverters(MyTypeConverter.class)
    private LocalDateTime createTime;

    @Ignore
    public Note() {}

    public Note(String title, int userId, int notebookId, String content, LocalDateTime createTime) {
        this.title = title;
        this.userId = userId;
        this.notebookId = notebookId;
        this.content = content;
        this.createTime = createTime;
    }

    @Ignore
    public Note(int id, String title, int userId, int notebookId, String content, LocalDateTime createTime) {
        this.noteId = id;
        this.title = title;
        this.userId = userId;
        this.notebookId = notebookId;
        this.content = content;
        this.createTime = createTime;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(int notebookId) {
        this.notebookId = notebookId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }
}
