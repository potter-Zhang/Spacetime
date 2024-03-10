package edu.whu.spacetime.domain.typeConverter;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyTypeConverter {
    @TypeConverter
    public String dateToString(LocalDateTime time) {
        if (time == null) {
            return "";
        } else {
            return time.toString();
        }
    }

    @TypeConverter
    public LocalDateTime fromTimestamp(String stamp) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss"; // 日期时间的格式模式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(stamp, formatter);
    }
}
