package com.surya_yasa_antariksa.localdatetime.database.dao

import androidx.room.*
import com.surya_yasa_antariksa.localdatetime.database.entity.DateTimeEntity

@Dao
interface DateTimeDao {

    @Query("SELECT * from dateTimeEntity")
    fun getAll(): List<DateTimeEntity>

    @Insert
    fun addAll(vararg dateTimeEntity: DateTimeEntity)

    @Delete
    fun delete(dateTimeEntity: DateTimeEntity)
}