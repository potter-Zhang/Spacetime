package edu.whu.spacetime.domain;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.time.LocalDateTime;

import edu.whu.spacetime.domain.typeConverter.MyTypeConverter;
import edu.whu.spacetime.domain.typeConverter.TypeConverter_todo;

@Entity
public class Todo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int todoId;

    private int userId;

    private String title;

    private String addr;

    private boolean checked;

    @TypeConverters(TypeConverter_todo.class)
    private LocalDateTime createTime;

    @Ignore
    public Todo(){}

    public Todo(int userId, String title, String addr, LocalDateTime createTime,boolean checked){
        this.userId = userId;
        this.title = title;
        this.addr = addr;
        this.createTime = createTime;
        this.checked = checked;
    }

    public int getTodoId(){ return this.todoId;}
    public void setTodoId(int todoId){ this.todoId = todoId;}
    public int getUserId(){ return this.userId;}
    public void setUserId(int userId){ this.userId = userId;}
    public String getTitle(){ return this.title;}
    public void setTitle(String title){this.title = title;}
    public String getAddr(){ return this.addr;}
    public void setAddr(){ this.addr = addr;}
    public LocalDateTime getCreateTime(){ return this.createTime;}
    public void setCreateTime(LocalDateTime time){ this.createTime = time;}

    public boolean getChecked(){
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
