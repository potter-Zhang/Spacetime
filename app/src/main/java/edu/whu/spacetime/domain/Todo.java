package edu.whu.spacetime.domain;

import androidx.room.Ignore;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;

import edu.whu.spacetime.domain.typeConverter.MyTypeConverter;

public class Todo {
    private int id;

    private int userId;

    private String title;

    private String addr;

    @TypeConverters(MyTypeConverter.class)
    private LocalDateTime createTime;

    @Ignore
    Todo(){}

    public Todo(int id, int userId, String title, String addr, LocalDateTime time){
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.addr = addr;
        this.createTime = time;
    }

    public int getId(){ return this.id;}
    public void setId(int id){ this.id = id;}
    public int getUserId(){ return this.userId;}
    public void setUserId(int userId){ this.userId = userId;}
    public String getTitle(){ return this.title;}
    public void setTitle(String title){this.title = title;}
    public String getAddr(){ return this.addr;}
    public void setAddr(){ this.addr = addr;}
    public LocalDateTime getCreateTime(){ return this.createTime;}
    public void setCreateTime(LocalDateTime time){ this.createTime = time;}
}
