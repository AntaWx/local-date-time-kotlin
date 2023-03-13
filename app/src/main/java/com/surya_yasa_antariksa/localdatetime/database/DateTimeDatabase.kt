package com.surya_yasa_antariksa.localdatetime.database

import android.content.Context
import androidx.room.*
import com.surya_yasa_antariksa.localdatetime.converter.LocalDateTimeConverter
import com.surya_yasa_antariksa.localdatetime.database.dao.DateTimeDao
import com.surya_yasa_antariksa.localdatetime.database.entity.DateTimeEntity

@Database(
    entities = [DateTimeEntity::class],
    version = 2
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class DateTimeDatabase: RoomDatabase() {
    abstract fun getDateTimeDao(): DateTimeDao

    companion object{
        private var instance: DateTimeDatabase? = null

        fun getInstance(context: Context): DateTimeDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(context, DateTimeDatabase::class.java, "date-time-database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}