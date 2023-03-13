package com.surya_yasa_antariksa.localdatetime.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): Long? {
        return localDateTime?.atZone(DateTimeFormatter.ISO_OFFSET_DATE_TIME.zone)?.toEpochSecond()
    }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, null) }
    }
}
