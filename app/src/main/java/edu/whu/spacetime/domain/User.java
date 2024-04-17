package edu.whu.spacetime.domain;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;

import edu.whu.spacetime.domain.typeConverter.MyTypeConverter;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String username;

    private String password;

    private byte[] avatar;

    private boolean gender;

    private String phone;

    private String region;

    @TypeConverters(MyTypeConverter.class)
    private LocalDateTime createTime;

    @Ignore
    public User() {
    }

    public User(int userId, String username, String password, byte[] avatar, boolean gender, String phone, String region, LocalDateTime createTime) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.gender = gender;
        this.phone = phone;
        this.region = region;
        this.createTime = createTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
