package edu.whu.spacetime.domain.typeConverter;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyTypeConverter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public String dateToString(LocalDateTime time) {
        if (time == null) {
            return "";
        } else {

            return time.format(formatter);
        }
    }

    @TypeConverter
    public LocalDateTime fromTimestamp(String stamp) {
        return LocalDateTime.parse(stamp, formatter);
    }
}
