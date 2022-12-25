package com.boysatria.rekomendasifilm.data.repo

import com.boysatria.rekomendasifilm.data.remote.apiservice.MoviesService
import com.google.gson.JsonObject

//MoviesRepo merupakan jembatan untuk menghubungkan ui dengan retrofit
class MoviesRepo (private val moviesService: MoviesService){

    suspend fun getPopularMovies(page: Int = 1) = moviesService.getPopularMovies(page = page)

    suspend fun getDetailPopularMovies(id: String) = moviesService.getDetailPopularMovies(id = id)

    suspend fun getPopularTvSeries(page: Int = 1) = moviesService.getPopularTvSeries(page = page)

    suspend fun getDetailPopularTvSeries(id: String) = moviesService.getDetailPopularTvSeries(id = id)
}

