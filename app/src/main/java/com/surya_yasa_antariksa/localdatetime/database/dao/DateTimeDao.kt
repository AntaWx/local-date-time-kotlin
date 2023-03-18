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

    @Query("SELECT * FROM dateTimeEntity ORDER BY localDateTime DESC")
    fun getAllByLatest(): List<DateTimeEntity>

    @Query("SELECT * FROM dateTimeEntity ORDER BY localDateTime ASC")
    fun getAllByOldest(): List<DateTimeEntity>

    @Update
    fun update(dateTimeEntity: DateTimeEntity)

}