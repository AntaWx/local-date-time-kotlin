package com.surya_yasa_antariksa.localdatetime.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class DateTimeEntity(

    @PrimaryKey(autoGenerate = true)
    val uid:Int,

    @ColumnInfo
    val name: String?,

    @ColumnInfo
    val localDateTime: String?,

    @ColumnInfo
    val kegiatan: String?
)