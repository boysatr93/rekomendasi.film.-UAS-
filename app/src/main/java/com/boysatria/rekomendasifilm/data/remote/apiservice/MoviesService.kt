package com.boysatria.rekomendasifilm.data.remote.apiservice

import com.boysatria.rekomendasifilm.data.remote.response.DetailMovieResponse
import com.boysatria.rekomendasifilm.data.remote.response.DetailTvSeriesResponse
import com.boysatria.rekomendasifilm.data.remote.response.MovieResponse
import com.boysatria.rekomendasifilm.data.remote.response.TvSeriesResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

//MovieService merupakan class interface berisi kumpulan request ke REST API
interface MoviesService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "69d66957eebff9666ea46bd464773cf0",
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("movie/{id}")
    suspend fun getDetailPopularMovies(
        @Path("id") id: String,
        @Query("api_key") apiKey: String = "69d66957eebff9666ea46bd464773cf0"
    ): Response<DetailMovieResponse>

    @GET("tv/popular")
    suspend fun getPopularTvSeries(
        @Query("api_key") apiKey: String = "69d66957eebff9666ea46bd464773cf0",
        @Query("page") page: Int
    ): Response<TvSeriesResponse>

    @GET("tv/{id}")
    suspend fun getDetailPopularTvSeries(
        @Path("id") id: String,
        @Query("api_key") apiKey: String = "69d66957eebff9666ea46bd464773cf0"
    ): Response<DetailTvSeriesResponse>
}