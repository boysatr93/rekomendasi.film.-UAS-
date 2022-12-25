package com.boysatria.rekomendasifilm.data.local.room.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//FavoriteEntity merupakan data table untuk favorit di room
@Entity(tableName = "favorite")
@Parcelize
data class FavoriteEntity(
        @ColumnInfo(name = "title") var title: String = "",
        @ColumnInfo(name = "overview") var overview: String = "",
        @ColumnInfo(name = "poster_path") var poster_path: String = "",
        @ColumnInfo(name = "backdrop_path") var backdrop_path: String = "",
        @ColumnInfo(name = "vote_average") var vote_average: Double = 0.0,
        @ColumnInfo(name = "release_date") var release_date: String = "",
        @ColumnInfo(name = "type") var type: String = "",
        @PrimaryKey @ColumnInfo(name = "id") var id: String = ""
) : Parcelable