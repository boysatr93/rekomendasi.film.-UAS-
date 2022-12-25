package com.boysatria.rekomendasifilm.data.local.room.repo

import com.boysatria.rekomendasifilm.data.local.room.MainRoomDatabase
import com.boysatria.rekomendasifilm.data.local.room.entity.FavoriteEntity
import io.reactivex.Observable

//RoomRepo merupakan jembatan untuk menghubungkan ui dengan room
class RoomRepo(private val db: MainRoomDatabase) {

    //Get
    fun getFavorites() : Observable<List<FavoriteEntity>>{
        return Observable.fromCallable { db.moviesDao().getFavorites() }
    }

    fun getFavorite(id: String) : Observable<FavoriteEntity>{
        return Observable.fromCallable { db.moviesDao().getFavorite(id) }
    }

    //Add
    fun addFavorite(favoriteEntity: FavoriteEntity): Observable<Unit> {
        return Observable.fromCallable { db.moviesDao().insert(favoriteEntity) }
    }

    //Delete
    fun deleteFavorite(id: String): Observable<Unit> {
        return Observable.fromCallable { db.moviesDao().delete(id) }
    }
}