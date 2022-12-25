package com.boysatria.rekomendasifilm.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boysatria.rekomendasifilm.data.local.room.dao.MoviesDao
import com.boysatria.rekomendasifilm.data.local.room.entity.FavoriteEntity
import com.boysatria.rekomendasifilm.utils.Converter


@Database(
        entities = [FavoriteEntity::class],
        version = 1,
        exportSchema = false)
@TypeConverters(Converter::class)
abstract class MainRoomDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        private var INSTANCE: MainRoomDatabase? = null

        fun getInstance(context: Context): MainRoomDatabase?{
            if (INSTANCE == null) {
                synchronized(MainRoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            MainRoomDatabase::class.java, "rekomendasifilm")
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE
        }
    }
}