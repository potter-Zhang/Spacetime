package edu.whu.spacetime.domain.typeConverter;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TypeConverter_todo {
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd   HH:mm");
    @TypeConverter
    public String dateToString(LocalDateTime time) {
        if (time == null) {
            return "";
        } else {
            return time.format(df);
        }
    }

    @TypeConverter
    public LocalDateTime fromTimestamp(String stamp) {
        return LocalDateTime.parse(stamp, df);
    }
}
