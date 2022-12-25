package com.boysatria.rekomendasifilm.data.local.room.dao

import androidx.room.*
import com.boysatria.rekomendasifilm.data.local.room.entity.FavoriteEntity

//MoviesDao merupakan class interface yang berisi perintah-perintah query ke room
@Dao
interface MoviesDao {
    @Query("SELECT * from favorite")
    fun getFavorites(): List<FavoriteEntity>

    @Query("SELECT * from favorite WHERE id=:id limit 1")
    fun getFavorite(id: String): FavoriteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteEntity: FavoriteEntity)

    @Update
    fun update(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE id=:id")
    fun delete(id: String)
}